package com.webcrawler;

import com.webcrawler.service.ICrawlerService;
import com.webcrawler.service.impl.CrawlerServiceImpl;

import java.io.IOException;
import java.sql.SQLException;


public class Main {
    // private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("starting");
        int depthLimit = -1;
        ICrawlerService crawlerService;

        try {
            if (args.length == 0) {
                System.out.println("Missing arguments: <URL> [Depth Limit (Default: 5 )]");
                System.exit(0);
            } else if (args.length == 2)
                depthLimit = Integer.parseInt(args[1]);

            //Initialize the service with required parameters
            crawlerService = new CrawlerServiceImpl(depthLimit);

            //Start service to process initial page
            crawlerService.processPage(args[0]);

        } catch (ClassNotFoundException e) {
            System.out.println("Error:" + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument value [Depth Limit]:  Only numeric values accepted");
        }
    }

}