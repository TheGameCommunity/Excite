package com.thegamecommunity.excite.modding.util;

public class ClassLoaderHelper {

	public static final ClassLoader getCurrentClassLoader() {
		return ClassLoaderHelper.class.getClassLoader();
	}
	
	public static final String getClassLoaderType(ClassLoader loader) {
		if(loader == null) {
			return "system classloader";
		}
		return loader.getClass().getCanonicalName();
	}
	
}
