package com.fh.hometask.users;

import java.util.logging.Logger;

public class Administrator {
    static final Logger logger = Logger.getLogger(Administrator.class.getName());
    private int id;
    private String name;
    private int age;
    private String position;
    public Administrator(int id, String name, int age, String position) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        logger.info("Administrator added: ID " + id + ", name: " + name + ", age: " + age + ", position: " + position);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
}
