package org.example.test4;

import lombok.Data;
import org.example.test1.Animal;
import org.example.test1.Dog;
import org.example.test3.Per;


public class Student extends Person {

    private Integer age;
//    private final Animal animal;

//    public Student(Animal animal) {
//        this.animal = animal;
//    }
    public Student(Integer age){
        this.age = age;
    }


    public static void main(String[] args) {
        Student s = new Student(1);

    }
}
