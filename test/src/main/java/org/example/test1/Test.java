package org.example.test1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        Animal[]a = {new Dog("dog"),new Cat("cat")};
        String  s = new ObjectMapper().writeValueAsString(a);
        Animal[]a1 = new ObjectMapper().readValue(s,a.getClass());
        System.out.println(a1);


    }
}
