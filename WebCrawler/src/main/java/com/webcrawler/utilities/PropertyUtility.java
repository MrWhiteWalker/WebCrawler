package com.webcrawler.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ramandeep on 23/5/16.
 */
public class PropertyUtility {
    private String dbUrl;
    private String username;
    private String password;


    public void loadProperties(){
        Properties prop = new Properties();
        InputStream input = null;

        try {

            //  input = new FileInputStream("db.properties");

            input = new PropertyUtility().getClass().getClassLoader().getResourceAsStream("db.properties");
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            dbUrl = prop.getProperty("CRAWLER.URL");
            username = prop.getProperty("CRAWLER.USERNAME");
            password = prop.getProperty("CRAWLER.PASSWORD");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getDbUrl() {
        return dbUrl;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }
}
