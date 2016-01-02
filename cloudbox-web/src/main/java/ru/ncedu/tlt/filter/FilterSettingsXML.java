/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.ncedu.tlt.entity.User;
import ru.ncedu.tlt.entity.UserRole;
import ru.ncedu.tlt.filter.AuthFilter.RequestWrapper;

/**
 *
 * @author Andrew
 */
@Stateless
@LocalBean
public class FilterSettingsXML {

    ArrayList<String> nologinPages = new ArrayList<>();
    ArrayList<String> userPages = new ArrayList<>();
    ArrayList<String> moderatorPages = new ArrayList<>();
    ArrayList<String> adminPages = new ArrayList<>();
    HashMap pagesMap;

    public boolean checkUserAccess(RequestWrapper wrappedRequest, User user) {
        boolean result = false;

        if (pagesMap == null) {
            readPageSettings(wrappedRequest.getServletContext());
        }

        if (user.getUserRoles() == null || user.getUserRoles().size() == 0) {
            UserRole role = new UserRole();
            role.setId(0);
            user.addRole(role);
        }

        String url = wrappedRequest.getRequestURI();
        ArrayList<String> pagesToCheck = new ArrayList<>();

        for (UserRole userRole : user.getUserRoles()) {
            pagesToCheck = (ArrayList<String>) pagesMap.get(userRole.getId());
            for (String page : pagesToCheck) {
                if (url.contains(page)) {
                    result = true;
                } 
            }
        }

        return result;
    }

    ;
    

    public void readPageSettings(ServletContext ctx) {
        ArrayList<String> currentArray = new ArrayList<>();

        pagesMap = new HashMap();
        pagesMap.put(0, nologinPages);
        pagesMap.put(1, userPages);
        pagesMap.put(2, moderatorPages);
        pagesMap.put(3, adminPages);

        try {
            String path = ctx.getRealPath("/WEB-INF/site_map_settings.xml");
            File fXmlFile = new File(path);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

//            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("group");

//            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

//                System.out.println("\nCurrent Element :" + ((Element) nNode).getElementsByTagName("name").item(0).getTextContent());
                switch (((Element) nNode).getElementsByTagName("name").item(0).getTextContent()) {
                    case "nologin":
                        currentArray = nologinPages;
                        break;
                    case "user":
                        currentArray = userPages;
                        break;
                    case "moderator":
                        currentArray = moderatorPages;
                        break;
                    case "admin":
                        currentArray = adminPages;
                        break;
                    default:
//                        currentArray = nologinPages;
                        break;

                }
                
//                System.out.println("node: " + ((Element) nNode).getElementsByTagName("name").item(0).getTextContent());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    for (int i = 0; i < eElement.getElementsByTagName("page").getLength(); i++) {
//                        System.out.println("page " + i + ": " + eElement.getElementsByTagName("page").item(i).getTextContent());
                        currentArray.add(eElement.getElementsByTagName("page").item(i).getTextContent());
//                        System.out.println(eElement.getElementsByTagName("page").item(i).getTextContent());
                    }

                }
            }

            currentArray = null;
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            System.out.println("ooooooop!!!!!!!!!!!!!!");
            System.out.println(e);
        }

    }

}
