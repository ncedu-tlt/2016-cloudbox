/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.entity;

import java.util.List;

/**
 *
 * @author Andrew
 */
public class User {
    
    private Integer id;
    private String name;
    private String pass;
    private String hash;
    private String email;
    private String note;
    private String picPath;
    
    private List<File> userFiles;
    private List<UserRole> userRoles;

    public User() {
    }

    public User(Integer id, String name, String pass, String email) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.email = email;
    }

    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public List<File> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(List<File> userFiles) {
        this.userFiles = userFiles;
    }
    
    public void addFile(File file){
        userFiles.add(file);
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    
    public void addRole(UserRole role){
        userRoles.add(role);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", pass=" + pass + ", email=" + email + ", note=" + note + '}';
    }
    
    
    
    
}
