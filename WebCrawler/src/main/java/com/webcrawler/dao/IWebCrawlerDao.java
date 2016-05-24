package com.webcrawler.dao;

import java.sql.SQLException;

/**
 * Created by ramandeep on 23/5/16.
 */
public interface IWebCrawlerDao {
    boolean checkURL(String url) throws SQLException;

    void addURL(String url) throws SQLException;

    void updateURL(String url, boolean parsed, String comments) throws SQLException;

    boolean execute(String sql) throws SQLException;

}
