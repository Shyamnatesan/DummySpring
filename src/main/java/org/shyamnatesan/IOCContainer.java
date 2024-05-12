package org.shyamnatesan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Parameter;
import java.util.*;

public class IOCContainer {
    private static final String packageName = "org.shyamnatesan";
    private final Set<String> allClasses;
    private final Map<Class<?>, Object> allInstances;

    public IOCContainer() {
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
            if (!line.endsWith("Main.class") && !line.endsWith("iocContainer.class") && !line.endsWith("Bun.class") && !line.endsWith("Dependency.class")) {
                String className = packageName + "." + line.substring(0, line.length() - 6);
                this.allClasses.add(className);
            }
        }
    }

    private void initializeBeans() {
        for(String className : this.allClasses) {
            try {
               Class<?> loadedClass = Class.forName(className);
                System.out.println("class name is " + loadedClass.getName());
                Annotation annotation = loadedClass.getAnnotation(Bun.class);
                if (annotation != null && !this.allInstances.containsKey(loadedClass)) {
                    resolveDependencies(loadedClass);
                }

            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void resolveDependencies(Class<?> loadedClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("resolving dependencies for " + loadedClass.getName());
        Constructor[] constructors = loadedClass.getConstructors();
        int i = 0;
        for (Constructor c : constructors) {
            Annotation an = c.getAnnotation(Dependency.class);
            if (an != null) {
                break;
            }
            i++;
        }
        if(i == constructors.length) {
            // no dependencies, since no Annotation @Dep was found in the constructors
            System.out.println("No dependency for " + loadedClass.getName() + " so resolved");
            this.allInstances.put(loadedClass, loadedClass.getDeclaredConstructor().newInstance());
            return ;
        }

        // Resolve dependencies and instantiate them
        List<Object> resolvedDependencies = new ArrayList<>();
        System.out.println("dependencies are ");
        for (Parameter p : constructors[i].getParameters()) {
            Class<?> dependencyType = p.getType();
            System.out.println(dependencyType);
            if (!this.allInstances.containsKey(dependencyType)) {
                System.out.println("this dependency is not instantiated yet. so lets do that");
                resolveDependencies(p.getType());
            }
            Object dependencyInstance = this.allInstances.get(dependencyType);
            resolvedDependencies.add(dependencyInstance);
        }

        Object[] constructorArgs = resolvedDependencies.toArray(new Object[resolvedDependencies.size()]);
        Object targetInstance = constructors[i].newInstance(constructorArgs);

        // Store the instance of the target class in the IOC container
        this.allInstances.put(loadedClass, targetInstance);

        System.out.println("Dependencies resolved for " + loadedClass.getName());

        System.out.println("dependencies listed above");
    }

    public Object getClassInstance(Class<?> classObj) {
        if (this.allInstances.containsKey(classObj)) {
            return this.allInstances.get(classObj);
        }
        return null;
    }
}
