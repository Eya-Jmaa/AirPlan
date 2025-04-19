package com.example.hotels.tests;

import com.example.hotels.models.Role;
import com.example.hotels.models.User;
import com.example.hotels.services.ServiceUser;

public class Main {
    public static void main(String[] args) {
        ServiceUser serviceUser = new ServiceUser();
        serviceUser.add(new User("malak","malak.taboubi@gmail.com", "sonic", "passenger", "98765432", "centre_ville"));
        //System.out.println("User added!");
        //serviceUser.update(new User("malak","malak.taboubi@gmail.com", "sonic", "passenger", "98765432", "soukra", 1));
        //System.out.println("User updated!");
        //serviceUser.delete(1);
        //System.out.println("User deleted!");
        System.out.println(serviceUser.getAllUsers());
    }
}
