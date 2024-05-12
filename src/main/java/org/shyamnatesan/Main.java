package org.shyamnatesan;


public class Main {
    public static void main(String[] args) {
        iocContainer ioc = new iocContainer();
        FirstClass firstClass = (FirstClass) ioc.getClassInstance(FirstClass.class);
        firstClass.FirstDummy();

    }
}