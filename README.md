A very simple Dependency Injection Framework or an IOC Container using Annotations and Reflection API.

For now, it supports only
1. Singletons (Scope).
2. Constructor Injection (No field or setter injection).
3. Assumes the @Dependency annotation in only 1 constructor.
     For example if a class has 3 constructors, and only 1 with the @Dependency annotation, then it takes this constructor.
