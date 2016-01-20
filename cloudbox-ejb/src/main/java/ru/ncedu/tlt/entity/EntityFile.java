/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author victori
 */
public class EntityFile implements Serializable {   
    private Integer id;
    private String name;
    private String ext;
    private String hash;
    private Timestamp date;
    private Integer owner;
    
    public EntityFile(){
        //this.id = generetion.getId("file");   //нужен генератор или sequence
        //this.id = 21;                         //раскоментить для тестов
    }
    
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public String getHash() {
        return hash;
    }

    public Timestamp getDate() {
        return date;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }
    
    public String getJSON() {              // TODO перевести все на рельсы какой- нибудь библиотеки
        return "{\"id\":" + id + "," +"\"name\":" +"\""+ name +"\""+ "," + "\"ext\":" + "\"" + ext +  
                "\"" + "," + "\"date\":" + "\"" + (date!=null ? date.toString() :"NULL") + "\""+ '}';
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntityFile)) {
            return false;
        }
        EntityFile other = (EntityFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "File{" + "id=" + id + ", name=" + name + ", ext=" + ext + ", hash=" + hash + ", date=" + date + ", owner=" + owner + '}';
    }
}
