package com.webcrawler.dao.impl;

import com.webcrawler.dao.IWebCrawlerDao;

import java.sql.*;

//import org.apache.log4j.Logger;

public class WebCrawlerDaoImpl implements IWebCrawlerDao {
    // private static final Logger logger = Logger.getLogger(WebCrawlerDaoImpl.class);

    private Connection connection = null;

    public WebCrawlerDaoImpl(String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        // String url = "jdbc:mysql://localhost:3306/Crawler";
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("connection built");

    }

    public ResultSet runSql(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public boolean execute(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.execute(sql);
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public boolean checkURL(String url) throws SQLException {
        String sql = "SELECT * FROM `Crawler`.`Record` where url = ?";
        //Statement statement = connection.createStatement();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, url);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
            return true;
        else
            return false;
    }

    @Override
    public void addURL(String url) throws SQLException {
        String sql = "INSERT INTO  `Crawler`.`Record` (`URL`) VALUES (?);";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, url);
        stmt.execute();
    }

    @Override
    public void updateURL(String url, boolean parsed, String comments) throws SQLException {
        String sql = "UPDATE   `Crawler`.`Record` SET `parsed`  = ? , comments = ? where URL =(?);";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setBoolean(1, parsed);
        stmt.setString(2, comments);
        stmt.setString(3, url);
        stmt.execute();
    }
}