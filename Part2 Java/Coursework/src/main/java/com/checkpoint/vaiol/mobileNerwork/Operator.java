package com.checkpoint.vaiol.mobileNerwork;

import java.util.*;

public class Operator {
    private String name;
    private Map<User, Package> users;
    private Set<Tower> towers;
    private Set<Package> packages;
    private Set<Package> oldPackages;

    public Operator(String name) {
        this.name = name;
        users = new HashMap<User, Package>();
        towers = new HashSet<Tower>();
        packages = new HashSet<Package>();
        oldPackages = new HashSet<Package>();
    }

    public Set<User> getUsers() {
        return users.keySet();
    }

    public Set<Tower> getTowers() {
        return towers;
    }

    public Set<Package> getPackages() {
        return packages;
    }

    public Set<Package> getOldPackages() {
        return oldPackages;
    }



    public void createNewUser() {
        //users.add(new User());
    }
}
