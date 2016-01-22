/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;



/**
 *
 * @author Andrey
 */
@Stateless
@LocalBean
public class DiskUtils {
    
    private String STORAGE_FOLDER_PATH;   // TODO проблема с путями, здесь в конце необходим пробел ??
    private String STORAGE_FOLDER_PATH_FOR_SIZE_FUNC; 
    private int STORAGE_SPACE_BYTES;
    private int FILE_OPERATING_BUFFER_SIZE;
        
    public DiskUtils() throws IOException{
        Properties property = new Properties(); 
        try {
            try (InputStream in = new FileInputStream("C:/tretyakovpe/CloudBox/cloudbox-ejb/src/main/java/ru/ncedu/tlt/utils/StorageProperties.properties")) {
                property.load(in);
            }
            STORAGE_FOLDER_PATH = property.getProperty("STORAGE_FOLDER_PATH");
            STORAGE_FOLDER_PATH_FOR_SIZE_FUNC = property.getProperty("STORAGE_FOLDER_PATH_FOR_SIZE_FUNC");
            STORAGE_SPACE_BYTES = Integer.decode(property.getProperty("STORAGE_SPACE_BYTES"));
            FILE_OPERATING_BUFFER_SIZE = Integer.decode(property.getProperty("FILE_OPERATING_BUFFER_SIZE"));            
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!" + e.getMessage());
            throw new IOException();
        }  
    }
        
    private int  getFreeStorageSpace(){
        File[] filesInStorage = new File(STORAGE_FOLDER_PATH_FOR_SIZE_FUNC).listFiles();
        int occupideSpace = 0;
        for(File file: filesInStorage){
            File f = file.getAbsoluteFile();
            occupideSpace += file.length();
            System.out.println("file name: "+file.getName());            
            System.out.println("file length: "+f.length());
        }
        int freeSpace = STORAGE_SPACE_BYTES - occupideSpace;
        System.out.println("freeSpace: "+freeSpace);        
        return freeSpace;
    }
    
    public  void  deleteFile (String uuidName) throws BackingStoreException{
        String newFileName = STORAGE_FOLDER_PATH+uuidName;
        boolean fileDeleted = false;
        try{
            fileDeleted = new File(newFileName).delete();
            if(!fileDeleted) throw new BackingStoreException("Problem while deleting file");
        }catch(SecurityException e){
            throw new BackingStoreException("No such file");
        }        
    };
    
    private BufferedInputStream  getFileStream (String uuidName) throws FileNotFoundException{
        String newFileName = STORAGE_FOLDER_PATH + uuidName;
        FileInputStream fos;
        fos = new FileInputStream(newFileName);
        return new BufferedInputStream(fos);
    };
    
    public void writeFileToOutStream(OutputStream outStream, String fileName) throws BackingStoreException{        
        BufferedInputStream fileStream;        
        try {
            fileStream = getFileStream(fileName);
            byte[] buffer = new byte[1024];
            while(true){                
                int numBuffBytes = fileStream.read(buffer);                
                if(numBuffBytes == -1) break;
                outStream.write(buffer, 0, numBuffBytes);
            };
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new BackingStoreException(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new BackingStoreException(e.getMessage());
        }
    }
    
    
    public String storeFile (InputStream byteStream,String uuidName) throws BackingStoreException,IOException {
        
        int freeSpace = getFreeStorageSpace();        
        String shortFileName = null;
        String newFileName = new File(STORAGE_FOLDER_PATH).getAbsolutePath()+ uuidName;
        System.out.println("Сохраняем: "+newFileName);
        try(FileOutputStream fos=new FileOutputStream(newFileName);
            BufferedOutputStream serverFileBufStream = new BufferedOutputStream(fos); 
            BufferedInputStream inputFileBufStream = new BufferedInputStream(byteStream);)
        {
            byte[] buffer = new byte[FILE_OPERATING_BUFFER_SIZE];
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

//--------Проверяет наличие файла на диске    
    public boolean checkFileOnDisk(String uuidName)
    {
        String newFileName = STORAGE_FOLDER_PATH+uuidName;
        File file = new File(newFileName);
        return file.exists();
    }
};