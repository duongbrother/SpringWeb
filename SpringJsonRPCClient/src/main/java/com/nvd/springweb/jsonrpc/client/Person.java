package com.nvd.springweb.jsonrpc.client;
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
}
