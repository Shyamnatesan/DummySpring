package org.shyamnatesan;

@Bun
public class DummyClass {

    private FourthDummyClass fourthDummyClass;

    @Dependency
    public DummyClass(FourthDummyClass fourthDummyClass) {
        this.fourthDummyClass = fourthDummyClass;
    }

    public void dummy() {
        this.fourthDummyClass.dummy();
        System.out.println("dummy from DummyClass");
    }
}
