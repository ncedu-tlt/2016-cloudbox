/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.filter;

import java.util.ArrayList;

/**
 *
 * @author Andrey
 */
public class WebAppFilter {

    private String roleName;
    private ArrayList<String> pages;
    private Integer roleId;
    private String basePage;

    public WebAppFilter() {
        this.roleName = "nologin";
        this.pages = null;
        this.roleId = 0;
        this.basePage = FilterParam.LOGIN_JSP;
    }

    public WebAppFilter(String roleName,  Integer roleId, String basePage,ArrayList<String> pages) {
        if (roleName != null) {
            this.roleName = roleName;
        } else {
            this.roleName = "nologin";
        }
        if (pages != null) {
            this.pages = pages;
        } else {
            this.pages = null;
        }
        if (roleId != null) {
            this.roleId = roleId;
        } else {
            this.roleId = 0;
        }
        if (basePage != null) {
            this.basePage = basePage;
        } else {
            this.basePage = FilterParam.LOGIN_JSP;
        }
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public ArrayList<String> getPages() {
        return pages;
    }

    public void setPages(ArrayList<String> pages) {
        this.pages = pages;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getBasePage() {
        return basePage;
    }

    public void setBasePage(String basePage) {
        this.basePage = basePage;
    }

}
