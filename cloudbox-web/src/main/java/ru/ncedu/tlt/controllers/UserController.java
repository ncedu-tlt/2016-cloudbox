/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.controllers;

import java.util.HashMap;
import ru.ncedu.tlt.entity.User;

/**
 *
 * @author Andrew
 */
public class UserController {
    
    private static volatile UserController instance;

    //Временные поля
    HashMap<String, User> userList;
    private int num = 0;
    
    private UserController() {
        
        userList = new HashMap<>();
        userList.put("admin", new User(num, "admin", "admin", "admin@mail.ru"));
        userList.put("moderator", new User(++num, "moderator", "moderator", "moderator@mail.ru"));
        userList.put("user", new User(++num, "user", "user", "user@mail.ru"));
        
    }
    
    public static UserController getInstance() {
        UserController localInstance = instance;
        if (localInstance == null) {
            synchronized (UserController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserController();
                }
            }
        }
        return localInstance;
    }
    
    public User getUserByName(String name, String pass) {
        User user = userList.get(name);
        if (user == null) {
            return null;
        }
        
        if (!pass.equals(user.getPass())) {
            return null;
        }
        
        return userList.get(name);
    }
    
    public boolean isUserExist(String name) {
        
        return !(userList.get(name) == null);
        
    }
    
    public User addUser(User user) {
        user.setId(++num);
        userList.put(user.getName(), user);
        return user;
    }
    
}
