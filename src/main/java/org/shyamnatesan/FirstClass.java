package org.shyamnatesan;


@Bun
public class FirstClass {
    private SecondClass secondClass;
    private ThirdClass thirdClass;

    @Dependency
    public FirstClass(SecondClass secondClass, ThirdClass thirdClass) {
        this.secondClass = secondClass;
        this.thirdClass = thirdClass;
    }

    public void FirstDummy() {
        System.out.println("Dummy from FirstClass");
        secondClass.SecondDummy();
        thirdClass.ThirdDummy();
    }
}
