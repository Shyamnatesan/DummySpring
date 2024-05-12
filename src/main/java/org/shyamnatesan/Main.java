package org.shyamnatesan;


public class Main {
    public static void main(String[] args) {
        IOCContainer ioc = new IOCContainer();
        FirstClass firstClass = (FirstClass) ioc.getClassInstance(FirstClass.class);
        firstClass.FirstDummy();

    }
}