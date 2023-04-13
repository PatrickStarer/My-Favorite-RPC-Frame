package org.example.test4;


import lombok.Data;

@Data
public abstract class Person {
    private Integer age;

    public  Integer getAge() {
        return age;
    }
}
