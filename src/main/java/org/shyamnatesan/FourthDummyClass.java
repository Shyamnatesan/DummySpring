package org.shyamnatesan;


@Bun
public class FourthDummyClass {

    private FifthClass fifthClass;

    @Dependency
    public FourthDummyClass(FifthClass fifthClass) {
        this.fifthClass = fifthClass;
    }

    public void dummy() {
        fifthClass.dummy();
        System.out.println("dummy from FourthDummyClass");
    }
}
