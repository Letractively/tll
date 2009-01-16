package com.tll.refdata;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * RefData - Loads ref data from files on disk and monitors these files for
 * change whereby loaded ref data is refreshed.
 * <p>
 * <b>IMPT: files are loaded from the classpath root.</b> <br>
 * App Ref Data File FORMAT:<br>
 * -------------------------<br>
 * {key}{TAB}{value}{NEWLINE}<br>
 * ...<br>
 * @author jpk
 */
public final class RefData {

	private static final Log log = LogFactory.getLog(RefData.class);

	public static final String APPREFDATA_FILEPREFIX = "refdata-";

	private final Map<RefDataType, Map<String, String>> resourceData = new HashMap<RefDataType, Map<String, String>>();

	private final Map<String, Long> lastModifiedTimes = new HashMap<String, Long>();

	private final FilenameFilter filter = new FilenameFilter() {

		public boolean accept(File dir, String name) {
			return name != null && name.startsWith(APPREFDATA_FILEPREFIX);
		}
	};

	private boolean resourcesLoaded = false;

	/**
	 * Provides a map of maps keyed by refdata name of all found ref data.
	 * @return map of ref data maps
	 */
	public Map<RefDataType, Map<String, String>> getAllRefData() {
		loadOrRefresh();
		Map<RefDataType, Map<String, String>> singleMap = new HashMap<RefDataType, Map<String, String>>();
		singleMap.putAll(resourceData);
		return singleMap;
	}

	/**
	 * Provides the refdata for the given type returning <code>null</code> if no
	 * data exists for that type.
	 * @param type the ref data type
	 * @return ref data map whose keys are
	 */
	public Map<String, String> getRefData(RefDataType type) {
		loadOrRefresh();
		if(!resourceData.containsKey(type)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.putAll(resourceData.get(type));
		return map;
	}

	private RefDataType refDataTypeFromFilename(String resourceName) {
		String terse = resourceName;
		int i = terse.indexOf(APPREFDATA_FILEPREFIX);
		if(i >= 0) terse = terse.substring(i + APPREFDATA_FILEPREFIX.length());
		i = terse.indexOf(".");
		if(i >= 0) terse = terse.substring(0, i);
		for(RefDataType rdt : RefDataType.values()) {
			if(rdt.getName().equals(terse)) {
				return rdt;
			}
		}
		return null;
	}

	private void loadOrRefresh() {
		if(!resourcesLoaded) {
			setResourceData();
		}
		else {
			refresh();
		}
	}

	private void refresh() {
		List<File> toRmv = new ArrayList<File>();

		for(String resourceName : lastModifiedTimes.keySet()) {

			// FileSystemResource resource = new FileSystemResource(resourceDir + "/"
			// + resourceName);
			File resource = new File(resourceName);

			if(!resource.exists()) {
				// resource removed
				toRmv.add(resource);
			}
			else {
				Long lmt = lastModifiedTimes.get(resourceName);
				try {
					if(resource.lastModified() > lmt.longValue()) {
						// re-load modified file-based resource
						Map<String, String> rmap = load(resource);
						if(rmap == null) continue;
						log.info("re-loading stale app ref data from file: " + resourceName);
						resourceData.put(refDataTypeFromFilename(resourceName), rmap);
						lastModifiedTimes.put(resourceName, new Long(resource.lastModified()));
					}
				}
				catch(Exception e) {
					log.warn("unexpected error occurred occurred re-loading stale app ref data: '" + e.getMessage()
							+ "'.  Continuing.", e);
				}
			}

		}

		for(Object element : toRmv) {
			String resourceName = (String) element;
			log.info("removing app ref data '" + resourceName + "' (resource no longer exists)...");
			resourceData.remove(refDataTypeFromFilename(resourceName));
			lastModifiedTimes.remove(resourceName);
		}

		// check for new resources
		File[] rFiles = getAppRefDataFiles();
		for(File resource : rFiles) {
			RefDataType rdt = refDataTypeFromFilename(resource.getName());
			if(!resourceData.containsKey(rdt)) {
				try {
					Map<String, String> rmap = load(resource);
					if(rmap == null) continue;
					log.info("adding newly found app ref data from file: " + resource.getName());
					resourceData.put(rdt, rmap);
					lastModifiedTimes.put(resource.getAbsolutePath(), new Long(resource.lastModified()));
				}
				catch(Exception e) {
					log.warn("Unable to add new app ref data from file: " + e.getMessage(), e);
				}
			}
		}

		this.resourcesLoaded = true;
	}

	/**
	 * Loads ref data from a file returning a map of of this ref data where the
	 * map keys are the first column in the ref data file and the values are the
	 * second column.
	 * @param resource the ref data file resource
	 * @return map of the loaded ref data
	 * @throws IOException
	 */
	private Map<String, String> load(File resource) throws IOException {
		log.info("loading app ref data resource '" + resource.getName() + "'...");

		if(!resource.exists()) {
			throw new IllegalArgumentException("Resource '" + resource.getName() + "' was not found.");
		}

		String sres = StringUtils.trim(FileUtils.readFileToString(resource, null));

		sres = StringUtils.trim(sres);
		String[] rows = StringUtils.split(sres, "\r\n");
		if(rows.length < 1) {
			log.warn("No rows found in app ref data resource: " + resource.getName());
			return null;
		}

		// use LinkedHashMap to maintain order of declaration
		Map<String, String> rezMap = new LinkedHashMap<String, String>();

		int num = 0;
		for(int i = 0; i < rows.length; i++) {
			String[] cols = StringUtils.split(rows[i], '\t'); // split on tab
			if(cols.length != 2) {
				// try a comma split
				cols = StringUtils.split(rows[i], ','); // split on tab
				if(cols.length != 2) {
					log.warn("App ref data row format error on row: " + i + ": " + rows[i] + ".  Skipping.");
					continue;
				}
			}
			rezMap.put(cols[0], cols[1]);
			num++;
		}

		log.info(num + " app data resource element(s) added to app data resource serviceMap: '" + resource.getName() + "'");
		return rezMap;
	}

	private File[] getAppRefDataFiles() {

		ClassLoader cld = Thread.currentThread().getContextClassLoader();
		if(cld == null) {
			throw new IllegalStateException("Can't get class loader.");
		}
		String path = ""; // i.e. the root
		URL resource = cld.getResource(path);
		if(resource == null) {
			throw new IllegalStateException("Unable to obtain the classpath root dir");
		}

		try {
			URI uri = resource.toURI();
			File classPathRoot = new File(uri.getPath());
			return classPathRoot.listFiles(filter);
		}
		catch(URISyntaxException se) {
			throw new IllegalArgumentException("Unable to obtain app ref data files under the root classpath dir");
		}
	}

	private void setResourceData() {
		log.info("Setting resource application ref data...");
		resourceData.clear();

		File[] rFiles = getAppRefDataFiles();
		for(File element : rFiles) {
			Map<String, String> rmap;
			try {
				if((rmap = load(element)) == null) continue;
			}
			catch(Exception e) {
				log.error("Unable to load app ref data from file '" + element.getName() + "': " + e.getMessage());
				continue;
			}
			resourceData.put(refDataTypeFromFilename(element.getName()), rmap);

			lastModifiedTimes.put(element.getAbsolutePath(), new Long(element.lastModified()));
		}

		resourcesLoaded = true;
	}

}
