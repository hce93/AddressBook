package com.example.addressbook.model;

public class Group {

    private int id;
    private String name;

    public Group(String name){
        this.name = name;
    }

    public Group(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {return name;}

    @Override
    public String toString(){return name;}

}
