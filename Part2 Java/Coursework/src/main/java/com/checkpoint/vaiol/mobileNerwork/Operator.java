package com.checkpoint.vaiol.mobileNerwork;

import java.util.List;

public class Operator {
    private List<User> users;
    private List<Tower> towers;
    private List<Package> packages;
    private List<Package> oldPackages;

    public List<User> getUsers() {
        return users;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public List<Package> getOldPackages() {
        return oldPackages;
    }
}
