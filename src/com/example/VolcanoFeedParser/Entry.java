package com.example.VolcanoFeedParser;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created with IntelliJ IDEA.
 * User: wietse
 * Date: 16/03/13
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
public class Entry {
    public final String title;
    public final String description; //contains HTML
    public final String link;
    public final String guid;
    public final AlertLevel alertLevel;
    public final ColorCode colorCode;
    public final LatLng geoRss;


    public Entry(String title, String description, String link, String guid, AlertLevel alertLevel, ColorCode colorCode, LatLng geoRss) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.guid = guid;
        this.alertLevel = alertLevel;
        this.colorCode = colorCode;
        this.geoRss = geoRss;
    }
}
