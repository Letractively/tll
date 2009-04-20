package com.tll.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

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
	 * @throws IllegalArgumentException When the given object is not clonable
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
		return getClasses(pckgname, null, false, null, null);
	}

	/**
	 * Retrieves classes in a given package name with various filtering options.
	 * @param <T> The class type
	 * @param pckgname The package name for which to retrieve contained classes.
	 * @param baseClass filter where only classes deriving from this class are
	 *        considered. May be <code>null</code>.
	 * @param concreteOnly bool flag when if true, only concrete classes are
	 *        considered.
	 * @param nameExclusor exlclude classes containing <code>nameExclusor</code>
	 *        within its name. May be null.
	 * @param filter Optional filename filter. This is helpful when we have
	 *        multiple filesystem dirs bound to the given package name and we want
	 *        to target a particular subset. May be <code>null</code>.
	 * @return array of found Class objects
	 * @throws ClassNotFoundException When the package name is not resolvable or
	 *         is not bound to at least one filesystem directory.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T>[] getClasses(final String pckgname, final Class<T> baseClass, final boolean concreteOnly,
			final String nameExclusor, FilenameFilter filter) throws ClassNotFoundException {

		final ArrayList<Class<T>> classes = new ArrayList<Class<T>>();

		// Get a File object for the package
		final List<File> directories = new ArrayList<File>();
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
				final String rpath = uri.getPath();
				if(rpath != null) {
					final File dir = new File(rpath);
					if(filter != null) {
						if(!filter.accept(dir, null)) {
							continue;
						}
					}
					directories.add(dir);
				}
			}
			catch(final URISyntaxException se) {
				throw new ClassNotFoundException(pckgname + " (" + resource.getPath()
						+ ") does not appear to be a valid package");
			}
			catch(final NullPointerException e) {
				// ok - continue
			}
		}

		if(directories.size() < 1) {
			throw new ClassNotFoundException("Unable to resolve package: " + pckgname
					+ " to any filesystem/classpath resource.");
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
}
