package com.example.VolcanoFeedParser;

/**
 * Created with IntelliJ IDEA.
 * User: wietse
 * Date: 16/03/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class UserRecord {
    public String username;
    public String link;
    public String description;
    public AlertLevel alertLevel;
    public ColorCode colorCode;

    public UserRecord(String username, String link, String description, AlertLevel alertLevel, ColorCode colorCode) {
        this.username = username;
        this.link = link;
        this.description = description;
        this.alertLevel = alertLevel;
        this.colorCode = colorCode;
    }
}
