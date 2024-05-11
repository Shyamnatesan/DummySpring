package org.shyamnatesan;


public class Main {
    public static void main(String[] args) {
        iocContainer ioc = new iocContainer();
        DummyClass dummyClass = (DummyClass) ioc.getClassInstance(DummyClass.class);
        dummyClass.dummy();

    }
}