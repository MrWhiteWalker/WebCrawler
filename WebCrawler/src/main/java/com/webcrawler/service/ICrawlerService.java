package com.webcrawler.service;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by ramandeep on 23/5/16.
 */
public interface ICrawlerService {
    void processPage(String url) throws SQLException, IOException;

}
