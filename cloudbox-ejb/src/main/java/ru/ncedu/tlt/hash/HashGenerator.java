/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author Andrew
 */
@Stateless
@LocalBean
public class HashGenerator {

    //Используем метод 1
    public boolean checkHash(String dataToCheck, String hash)   {
        return hash.equals(generateHashMetod1(dataToCheck));
    }
    
    public String getHash(String toHash){
        return generateHashMetod1(toHash);
    }

    private String generateHashMetod1(String toHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(toHash.getBytes());
            
            byte byteData[] = md.digest();
            
            //convert the byte to hex format method 1
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            System.out.println("Hex format : " + sb.toString());
            
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private String generateHashMetod2(String toHash)  {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(toHash.getBytes());
            
            byte byteData[] = md.digest();
            
            //convert the byte to hex format method 2
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            System.out.println("Hex format : " + hexString.toString());
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
