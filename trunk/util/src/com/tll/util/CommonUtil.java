package com.tll.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * CommonUtil - common utility methods.
 * @author jpk
 */
public abstract class CommonUtil {

	/**
	 * Utility method that does an equals comparison with null checks. If both
	 * objects are null, they are considered equal by this method. If only one is
	 * null, they are considered unequal. Otherwise, if neither are null, the call
	 * is delegated to o1.equals(o2).
	 * @param o1 first object to compare
	 * @param o2 second object to compare
	 * @return <code>true</code> if the objects are equal or both
	 *         <code>null</code>, <code>false</code> otherwise.
	 * @see #equals(Object, Object, boolean)
	 */
	public static boolean equals(final Object o1, final Object o2) {
		return equals(o1, o2, false);
	}

	/**
	 * Same as {@link #equals(Object, Object)} with the added consideration of
	 * case sensitivity in the case that the objects are {@link String}s.
	 * @param o1
	 * @param o2
	 * @param isCaseSensitive
	 * @return true if the objects are equal or both null, false otherwise.
	 */
	public static boolean equals(final Object o1, final Object o2, boolean isCaseSensitive) {
		if(o1 == null) {
			if(o2 == null) {
				return true;
			}
			return false;
		}
		if(o1 instanceof String && o2 instanceof String && !isCaseSensitive) {
			((String) o1).equalsIgnoreCase((String) o2);
		}
		return o1.equals(o2);
	}

	/**
	 * Generic cloning method.
	 * @param obj The object to clone
	 * @return A clone of the object or <code>null</code> if the given object is
	 *         <code>null</code>.
	 * @throws IllegalStateException When the cloning fails usu. due to the type
	 *         of the given object.
	 */
	public static Object clone(final Object obj) throws IllegalArgumentException {
		if(obj == null) {
			return null;
		}
		if(obj instanceof String) {
			// no need to clone since it's immutable
			return obj;
		}
		else if(obj instanceof Long) {
			return new Long(((Long) obj).longValue());
		}
		else if(obj instanceof Integer) {
			return new Integer(((Integer) obj).intValue());
		}
		else if(obj instanceof Double) {
			return new Double(((Double) obj).doubleValue());
		}
		else if(obj instanceof Float) {
			return new Float(((Float) obj).floatValue());
		}
		else if(obj instanceof Date) {
			return ((Date) obj).clone();
		}
		else if(obj instanceof Enum) {
			return obj; // no need to clone enums since they are immutable
		}
		throw new IllegalArgumentException(
				"Encountered a non-null obj of a type that is not supported for generic cloning.");
	}

	/**
	 * Retrieves classes in a given package name
	 * @param pckgname package name
	 * @return list of all found classes for a given package name
	 * @throws ClassNotFoundException
	 */
	public static Class<?>[] getClasses(final String pckgname) throws ClassNotFoundException {
		return getClasses(pckgname, null, false, null);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Retrieves classes in a given package name
	 * @param pckgname
	 * @param baseClass filter where only classes deriving from this class are
	 *        considered. May be null.
	 * @param concreteOnly bool flag when if true, only concrete classes are
	 *        considered.
	 * @param nameExclusor exlclude classes containing <code>nameExclusor</code>
	 *        within its name. May be null.
	 * @return array of found Class objects
	 * @throws ClassNotFoundException
	 */
	public static <T> Class<T>[] getClasses(final String pckgname, final Class<T> baseClass, final boolean concreteOnly,
			final String nameExclusor) throws ClassNotFoundException {

		final ArrayList<Class<T>> classes = new ArrayList<Class<T>>();

		// Get a File object for the package
		final List<File> directories = new ArrayList<File>(3);
		try {
			final ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if(cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			final String path = pckgname.replace('.', '/');
			Enumeration<URL> resources;
			try {
				resources = cld.getResources(path);
			}
			catch(final IOException e) {
				resources = null;
			}
			if(resources == null) {
				throw new ClassNotFoundException("No resources found for " + path);
			}

			while(resources.hasMoreElements()) {
				final URL resource = resources.nextElement();
				try {
					final URI uri = resource.toURI();
					directories.add(new File(uri.getPath()));
				}
				catch(final URISyntaxException se) {
					throw new ClassNotFoundException(pckgname + " (" + resource.getPath()
							+ ") does not appear to be a valid package");
				}
			}
		}
		catch(final NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
		}

		for(final File directory : directories) {
			if(directory.exists()) {
				// Get the list of the files contained in the package
				final String[] files = directory.list();
				for(final String element : files) {
					// we are only interested in .class files
					if(element.endsWith(".class")) {
						// removes the .class extension
						final Class claz = Class.forName(pckgname + '.' + element.substring(0, element.length() - 6));
						if(baseClass != null && !baseClass.isAssignableFrom(claz)) continue;
						if(concreteOnly && Modifier.isAbstract(claz.getModifiers())) continue;
						if(nameExclusor != null && nameExclusor.length() > 0 && claz.getName().indexOf(nameExclusor) >= 0)
							continue;
						classes.add(claz);
					}
				}
			}
		}

		final Class[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}

	/**
	 * Retrieves the contents of a file as a string with system default encoding
	 * on the classpath under a given path of a given name.
	 * @param path The path of the file
	 * @param fileName The name of the file.
	 * @return String containing the file contents
	 * @throws IOException
	 */
	public static String getClasspathFileContents(final String path, final String fileName) throws IOException {
		final Resource resource = new ClassPathResource(path);
		return FileUtils.readFileToString(resource.getFile(), System.getProperty("encoding"));
	}

	/**
	 * Retrieves the contents of a file as a string with system default encoding
	 * on the classpath under a given path of a given name.
	 * @param claz The class name from which the path is determined.
	 * @param fileName The name of the file.
	 * @throws IOException
	 * @see #getClasspathFileContents(String, String)
	 */
	public static String getClasspathFileContents(final Class<?> claz, final String fileName) throws IOException {
		final String path = StringUtils.replace(claz.getPackage().getName(), ".", "/") + '/' + fileName;
		return getClasspathFileContents(path, fileName);
	}

}
