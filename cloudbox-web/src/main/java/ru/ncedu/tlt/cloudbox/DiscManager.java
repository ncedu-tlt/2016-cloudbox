/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;



/**
 *
 * @author Andrey
 */
public class DiscManager {
    private static final File storage ;  
    private static final String storageFolderPathSizeFunc;
    private static final int maxStorageSpaceBytes;
    private static final int fileBufferSizeBytes;
    static{
        String storageFolderPath = "C:\\JavaSrc\\STORAGE\\ "; //TODO хрень с путями пробел в конце и тп.
        storageFolderPathSizeFunc = "C:\\JavaSrc\\STORAGE";
        // TODO достать путь и допустимый объем
        maxStorageSpaceBytes = 100000;
        storage = new File(storageFolderPath);
        fileBufferSizeBytes = 1024;     
    }
        
    static private int  getFreeStorageSpace(){
        File[] filesInStorage = new File(storageFolderPathSizeFunc).listFiles();
        int occupideSpace = 0;
        for(File file: filesInStorage){
            File f = file.getAbsoluteFile();
            occupideSpace += file.length();
            System.out.println("file name: "+file.getName());            
            System.out.println("file lengt: "+f.length());
        }
        int freeSpace = maxStorageSpaceBytes - occupideSpace;
        System.out.println("freeSpace: "+freeSpace);        
        return freeSpace;
    }
    
    static public  boolean  deleteFile (String uuidName){
        String newFileName = storage.getAbsolutePath()+uuidName;
        if(new File(newFileName).delete()) {
            System.out.println("file deleted");
            return true;    
        }
        return false;
    };
    
    static public BufferedInputStream  getFileStream (String uuidName) throws FileNotFoundException{
        String newFileName = storage.getAbsolutePath()+uuidName;
        FileInputStream fos;
        fos = new FileInputStream(newFileName);
        return new BufferedInputStream(fos);
    };
    
    
    static public String storeFile (InputStream byteStream,String uuidName) throws BackingStoreException,IOException {
        
        int freeSpace = getFreeStorageSpace();        
        String shortFileName = null;
        String newFileName = storage.getAbsolutePath()+ uuidName;
                
        try(FileOutputStream fos=new FileOutputStream(newFileName);
            BufferedOutputStream serverFileBufStream = new BufferedOutputStream(fos); 
            BufferedInputStream inputFileBufStream = new BufferedInputStream(byteStream);)
        {
            byte[] buffer = new byte[fileBufferSizeBytes];
            int summStoredBytes = 0;
            while(true){                
                int numBuffBytes = inputFileBufStream.read(buffer);
                if (freeSpace - summStoredBytes< numBuffBytes){
                    throw new BackingStoreException("storage overflow");
                } 
                // TODO может выйти за границу при параллельном доступе 
                //  нужно проверять размер свободного пространства
                //  в процессе, но размер файла не считается пока к файлу открыт поток
                if(numBuffBytes == -1) break;
                serverFileBufStream.write(buffer, 0, numBuffBytes);
                summStoredBytes += numBuffBytes;
            }
            shortFileName = uuidName;
        }
        catch(BackingStoreException ex){
            if(!(new File(newFileName).delete())) 
                throw new BackingStoreException("FILE WAS NOT DELETED AFTER WRIGHTING PROBLEM");
            throw new BackingStoreException("Storage Exception");
        } 
        catch(IOException ex){
            shortFileName = null;
            throw new IOException(newFileName , ex);
        } 
        finally{
            return shortFileName;
        }
    };
};