/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.entity;

import java.util.Date;

/**
 *
 * @author Andrew
 */
public class File {
    
    private Integer id;
    private String name;
    private String ext;
    private String hash;
    private Date date;
    private Integer owner;
    

    public File() {
    }

     public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "File{" + "id=" + id + ", name=" + name + ", ext=" + ext + ", hash=" + hash + ", date=" + date + ", owner=" + owner + '}';
    }
    
    public String getJSON() {              // TODO перевести все на рельсы какой- нибудь библиотеки
        return "{\"id\":" + id + "," +"\"name\":" +"\""+ name +"\""+ "," + "\"ext\":" + "\"" + ext +  
                "\"" + "," + "\"date\":" + "\"" + date.toString() + "\""+ '}';
    }
    
    
}
