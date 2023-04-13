package org.example.test4;

import lombok.Data;
import org.example.test3.Per;

@Data
public class Student extends Person {

    private Integer age;

    public Student(Integer age) {
        this.age = age;
    }

    public Student() {
    }

    public static void main(String[] args) {
        Student s = new Student(1);
        s.getAge();
    }
}
