/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.FilesServlets;

//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Andrey
 */
public class Service {
//    final static String salt = "the best salt";
//
//    public static String getHash(String str) throws NoSuchAlgorithmException {
//        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//        messageDigest.update(str.concat(salt).getBytes());
//        String encryptedString = new String(messageDigest.digest());
//        return encryptedString;
//    }
    
    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static Timestamp getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static ArrayList<Integer> getIntListFromJSONlist(String str) {
        String[] filesId = str.split(",");
        ArrayList<Integer> intList = new ArrayList();
        for (String s : filesId) {
            intList.add(Integer.parseInt(s.replaceAll("[^0-9]", "")));
        }
        return intList;
    }
}
