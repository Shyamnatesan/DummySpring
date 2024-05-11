package org.shyamnatesan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;

import java.util.*;

public class iocContainer {
    private static final String packageName = "org.shyamnatesan";
    private final Set<String> allClasses;
    private final Map<Class<?>, Object> allInstances;

    public iocContainer() {
        try {
            allClasses = new HashSet<>();
            allInstances = new HashMap<>();
            scanBeans();
            initializeBeans();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void scanBeans() throws IOException {
        System.out.println("scanning for classes");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream inputStream = classLoader
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.endsWith("Main.class") && !line.endsWith("iocContainer.class") && !line.endsWith("Bun.class")) {
                String className = packageName + "." + line.substring(0, line.length() - 6);
                this.allClasses.add(className);
            }
        }
    }

    private void initializeBeans() {
        for(String className : this.allClasses) {
            System.out.println("class name is " + className);
            try {
               Class<?> loadedClass = Class.forName(className);
                Annotation annotation = loadedClass.getAnnotation(Bun.class);
                if (annotation != null) {
                    Object instance = loadedClass.getDeclaredConstructor().newInstance();
                    this.allInstances.put(loadedClass, instance);
                }

            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Object getClassInstance(Class<?> classObj) {
        if (this.allInstances.containsKey(classObj)) {
            return this.allInstances.get(classObj);
        }
        return null;
    }
}
