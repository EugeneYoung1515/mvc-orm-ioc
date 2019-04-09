package com.smart.core.ioc;

import com.smart.core.ioc.annotations.Controller;
import com.smart.core.ioc.annotations.Repository;
import com.smart.core.ioc.annotations.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ClassUtil {
    public static Map<String,Class> repositoryMap = new HashMap<>(16);
    public static Map<String,Class> serviceMap = new HashMap<>(16);
    public static Map<String,Class> controllerMap = new HashMap<>(16);

    public static void getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        for (File directory : dirs) {
            findClasses(directory, packageName);
        }
    }

     private static void findClasses(File directory, String packageName) throws ClassNotFoundException {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(!file.getAbsolutePath().contains("core")) {
                    findClasses(file, packageName + "." + file.getName());
                }
            } else if (file.getName().endsWith(".class")) {
                Class clz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                 if(clz.getAnnotation(Repository.class)!=null) {
                    repositoryMap.put(clz.getSimpleName(),clz);
                }
                if(clz.getAnnotation(Service.class)!=null) {
                    serviceMap.put(clz.getSimpleName(),clz);
                }
                if(clz.getAnnotation(Controller.class)!=null) {
                    controllerMap.put(clz.getSimpleName(),clz);
                }
            }
        }
    }

    /*
    public static void main(String[] args){
        InputStream inputStream = ClassUtil.class.getResourceAsStream("/setting.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            System.out.println(properties.getProperty("topPackageName"));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    */
}
