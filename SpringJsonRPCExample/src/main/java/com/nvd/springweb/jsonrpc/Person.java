package com.nvd.springweb.jsonrpc;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
public class Person {

    private String name;
    private int yearOfBirth;

    public Person(){}
    
    public Person(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Persion{");
        sb.append("name=").append(name);
        sb.append(",yearOfBirth=").append(yearOfBirth);
        sb.append("}");
        return sb.toString();
    }
    
    public static void main(String[] args) {
        Person p = new Person("Solar", 212);
        Person p2 = new Person("City", 123);
        Gson gson = new Gson();
        System.out.println("com.nvd.springweb.jsonrpc.Person.main()" + gson.toJson(p));
        List<Person> persons = Arrays.asList(p, p2);
        System.out.println("com.nvd.springweb.jsonrpc.Person.main()" + gson.toJson(persons));
    }
}
