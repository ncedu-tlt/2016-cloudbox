/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.controllers;

import java.util.ArrayList;
import ru.ncedu.tlt.entity.File;
import ru.ncedu.tlt.entity.User;

/**
 *
 * @author pavel.tretyakov
 */
public class FileController {
    
    private static volatile FileController instance;
    
    public static FileController getInstance() {
        FileController localInstance = instance;
        if (localInstance == null) {
            synchronized (UserController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FileController();
                }
            }
        }
        return localInstance;
    }
//---    
    public File getFileById(int fileId)
    {
        File file = new File();
        String query = "select * "
                + "from cb_file "
                + "where fileid = "
                + fileId
                +";";
        
    return file;
    }

//---    
    public ArrayList getFileListByExtension(String ext)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "select * "
                + "from cb_file "
                + "where fileext = '"
                +ext
                +"';";
        
        return fileList;
    }

//---    
    public ArrayList getFileListByExtension(String ext, User user)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "select * "
                + "from cb_userfile uf "
                + "inner join cb_file f on uf.uf_fileid = f.fileid "
                + "where f.fileext = '"
                + ext
                + "' and uf.uf_userid = "
                + user.getId()
                + ";";
        
        return fileList;
    }

//---    
    public ArrayList getFileListByDate(String date)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "";
        
        return fileList;
    }
    
//---    
    public ArrayList getFileListByOwner(User user)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "select * "
                + "from cb_file "
                + "where fileuserid = "
                + user.getId()
                + ";";
        
        return fileList;
    }

//---    
    public ArrayList getFileListByUser(User user)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "SELECT * "
                + "FROM CB_FILE "
                + "JOIN CB_USERFILE "
                + "ON CB_FILE.FILEID = CB_USERFILE.UF_FILEID "
                + "WHERE (CB_USERFILE.UF_USERID = "
                + user.getId()
                + ") AND (CB_USERFILE.UF_DEL IS NULL) "
                + "ORDER BY CB_FILE.FILENAME;";
        
        return fileList;
    }


//---    
    public void addFile(File file)
    {
        String query = "";
        
    }

//---    
    public void markAsDeleted(File file)
    {
        String query = "UPDATE CB_USERFILE " +
                "SET UF_DEL = SYSDATE " +
                "WHERE (UF_FILEID = "
                + file.getId()
                + ");";

    }

//---    
    public void markAsDeleted(File file, User user)
    {
        String query = "UPDATE CB_USERFILE " +
                "SET UF_DEL = SYSDATE " +
                "WHERE (UF_FILEID = "
                + file.getId()
                + ") AND (UF_USERID = "
                + user.getId()
                + ");";
        
    }
}
