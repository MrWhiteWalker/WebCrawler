package com.webcrawler.service.impl;

import com.webcrawler.dao.IWebCrawlerDao;
import com.webcrawler.dao.impl.WebCrawlerDaoImpl;
import com.webcrawler.service.ICrawlerService;
import com.webcrawler.utilities.PropertyUtility;
import com.webcrawler.utilities.Utilities;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

//import org.apache.log4j.Logger;

/**
 * Created by ramandeep on 23/5/16.
 */
public class CrawlerServiceImpl implements ICrawlerService {
    // private static final Logger logger = Logger.getLogger(CrawlerServiceImpl.class);

    private IWebCrawlerDao webCrawlerDao;
    private int level = 0;
    private int depthLimit = 5;


    public CrawlerServiceImpl(int depthLimit) throws SQLException, ClassNotFoundException {
        if (depthLimit >= 0)
            this.depthLimit = depthLimit;
        //Loading db parameters
        PropertyUtility propertyUtility = new PropertyUtility();
        propertyUtility.loadProperties();
        webCrawlerDao = new WebCrawlerDaoImpl(propertyUtility.getDbUrl(), propertyUtility.getUsername(),
                propertyUtility.getPassword());
        webCrawlerDao.execute("TRUNCATE `Crawler`.`Record`");

    }

    /**
     * Processes a url
     *
     * @param url
     * @throws SQLException
     * @throws IOException
     */
    public void processPage(String url) throws SQLException, IOException {

        url = Utilities.simplifyUrl(url);
        if (url.length() == 0)
            return;

        //check if the given url is already in database
        if (webCrawlerDao.checkURL(url))
            return;
        else
            //store the url to database to avoid parsing again
            webCrawlerDao.addURL(url);

        // System.out.println("level before: "+level);
        //If the current level is at depth limit, return with corresponding message
        if ((level + 1) == depthLimit) {
            webCrawlerDao.updateURL(url, true, "Not parsed cause depth limit reached");

        } else {
            level++;
            // System.out.println("level after: " + level);


            //get all links and recursively call the processPage method
            Elements elements = getUrlsFromPage(url);
            if (elements != null) {

                int count = 0;
                for (Element link : elements) {
                    count++;
                    System.out.println("Level: " + level + " Left: " + (elements.size() - count));
                    processPage(link.attr("abs:href"));
                }
                //Update URL as parsed when all sub elements have been parsed
                webCrawlerDao.updateURL(url, true, "");
            }
            level--;
        }
    }

    /**
     * Parses a page and returns the list of url elements.
     *
     * @param url
     * @return List of url element or null if error occurs while parsing
     */
    private Elements getUrlsFromPage(String url) {
        Document document;
        Elements questions;
        try {
            document = Jsoup.connect(url).get();
            questions = document.select("a[href]");

        } catch (Exception e) {
            System.out.println("Cannot connect: " + url + "\n" + e.getMessage());
            e.printStackTrace();
            try {
                //Updates url with correct message. These urls  can be referred to later for further processing
                webCrawlerDao.updateURL(url, false, "Failed to parse :" + e.getMessage());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        return questions;
    }

}
