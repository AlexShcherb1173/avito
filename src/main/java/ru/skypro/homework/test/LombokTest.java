package ru.skypro.homework.test;

import lombok.Data;

@Data
class TestLombok {
    private String name;
    private Integer value;
}
public class LombokTest {
    public static void main(String[] args) {
        TestLombok test = new TestLombok();
        test.setName("Test");
        test.setValue(123);
        System.out.println(test.getName() + " = " + test.getValue());
        System.out.println("Lombok работает: " + test.toString());
    }
}
