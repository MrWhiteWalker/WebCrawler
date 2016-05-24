package com.webcrawler.utilities;

/**
 * Created by ramandeep on 23/5/16.
 */
public class Utilities {
    public static String simplifyUrl(String url) {
        url = url.trim();
        if (url.endsWith("/#"))
            url = url.substring(0, url.length() - 2);
        else if (url.endsWith("#") || url.endsWith("/"))
            url = url.substring(0, url.length() - 1);
        return url;
    }

}
