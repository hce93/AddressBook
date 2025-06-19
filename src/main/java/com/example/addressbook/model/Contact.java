package com.example.addressbook.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Contact {
    private int id;
    private String name;
    private String number;
    private String email;
    private String address;
    private List<Group> groups = new ArrayList<>();

    //constructor
    public Contact(String name, String number, String email, String address){
        if (name==null || name.isBlank()) throw new IllegalArgumentException("No name passed");
        if (address==null || address.isBlank()) throw new IllegalArgumentException("No address passed");
        if (!emailIsValid(email)) throw new IllegalArgumentException("Not a valid email" + email);
        if (!numberIsValid(number)) throw new IllegalArgumentException("Not a valid number" + number);

        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
    }

    public Contact(int id, String name, String number, String email, String address){
        this(name, number, email, address);
        this.id = id;
    }

    public int getId() {return id;}

    public String getEmail() {return email;}

    public String getNumber() {return number;}

    public String getAddress() {return address;}

    public String getName() {return name;}

    public List<Group> getGroups() {return groups;}
    public void addGroup(Group group){this.groups.add(group);}

    @Override
    public String toString(){
        return name + " - " + number + " - "  + email + " - " + address;
    }

    private boolean emailIsValid(String email){
        String emailRegex = "^[a-zA-Z0-9_+*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern p = Pattern.compile(emailRegex);
        return email != null & p.matcher(email).matches();
    }

    private boolean numberIsValid(String number){
        String numRegex = "^0[7-9][0-9]{9}";
        Pattern p = Pattern.compile(numRegex);
        return number != null & p.matcher(number).matches();
    }
}