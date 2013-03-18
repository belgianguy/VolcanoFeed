package com.example.VolcanoFeedParser;

/**
 * Created with IntelliJ IDEA.
 * User: wietse
 * Date: 16/03/13
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
public class Entry {
    public final String title;
    public final String link;
    public final String summary;


    public final String theChange;

    public Entry(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
        this.theChange = "Now";
    }
}
