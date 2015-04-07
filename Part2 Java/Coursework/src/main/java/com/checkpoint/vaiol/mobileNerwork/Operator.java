package com.checkpoint.vaiol.mobileNerwork;

import com.checkpoint.vaiol.mobileNerwork.customer.User;
import com.checkpoint.vaiol.mobileNerwork.events.Call;
import com.checkpoint.vaiol.mobileNerwork.packages.Package;
import com.checkpoint.vaiol.mobileNerwork.packages.PackageFactory;

import java.util.*;

public class Operator {
    private String name;
    private Map<User, Package> users;
    private List<Call> callList;
    private Set<Tower> towers;
    private Set<Package> oldPackages;

    public Operator(String name) {
        this.name = name;
        users = new HashMap<User, Package>();
        towers = new HashSet<Tower>();
        oldPackages = new HashSet<Package>();
        callList = new LinkedList<Call>();
    }

    public Set<User> getUsers() {
        return users.keySet();
    }

    public Set<Tower> getTowers() {
        return towers;
    }

    public Set<Package> getOldPackages() {
        return oldPackages;
    }


    public User createNewUser() {
        User tmpUser = new User(PackageFactory.getBasicPackage(), MobileNetwork.generatePhoneNumber());
        users.put(tmpUser, tmpUser.getaPackage());
        return tmpUser;
    }
}
