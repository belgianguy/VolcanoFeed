package com.example.VolcanoFeedParser;

import android.util.Log;
import android.util.Xml;
import com.google.android.gms.maps.model.LatLng;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wietse
 * Date: 16/03/13
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class MyXMLParser {


    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            Log.d("XMLPARSER", parser.getName()); //RSS

            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.nextTag(); //channel
        Log.d("XMLPARSER", parser.getName());

        //parser.require(XmlPullParser.START_TAG, ns, "feed");
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                entries.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String link = null;
        String guid = null;
        LatLng geoRss = null;
        AlertLevel alertLevel = null;
        ColorCode colorCode = null;


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("guid")) {
                guid = readGuid(parser);
            } else if (name.equals("georss:point")) {
                geoRss = readGeoRssPoint(parser);
            } else if (name.equals("volcano:alertlevel")) {
                alertLevel = readAlertLevel(parser);
            } else if (name.equals("volcano:colorcode")) {
                colorCode = readColorCode(parser);
            } else {
                skip(parser);
            }
        }

        return new Entry(title, description, link, guid, alertLevel, colorCode, geoRss);
    }

    private ColorCode readColorCode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "volcano:colorcode");
        String colorCode = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "volcano:colorcode");

        if (ColorCode.UNASSIGNED.name().equals(colorCode))
            return ColorCode.UNASSIGNED;
        else if (ColorCode.GREEN.name().equals(colorCode))
            return ColorCode.GREEN;
        else if (ColorCode.YELLOW.name().equals(colorCode))
            return ColorCode.YELLOW;
        else if (ColorCode.ORANGE.name().equals(colorCode))
            return ColorCode.ORANGE;
        else if (ColorCode.RED.name().equals(colorCode))
            return ColorCode.RED;
        else
            return ColorCode.UNASSIGNED;
    }

    private AlertLevel readAlertLevel(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "volcano:alertlevel");
        String alertLevel = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "volcano:alertlevel");

        if (AlertLevel.UNASSIGNED.name().equals(alertLevel))
            return AlertLevel.UNASSIGNED;
        else if (AlertLevel.NORMAL.name().equals(alertLevel))
            return AlertLevel.NORMAL;
        else if (AlertLevel.ADVISORY.name().equals(alertLevel))
            return AlertLevel.ADVISORY;
        else if (AlertLevel.WATCH.name().equals(alertLevel))
            return AlertLevel.WATCH;
        else if (AlertLevel.WARNING.name().equals(alertLevel))
            return AlertLevel.WARNING;
        else
            return AlertLevel.UNASSIGNED;
    }

    private LatLng readGeoRssPoint(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "georss:point");
        LatLng returnPoint = null;
        String gPoint = readText(parser).trim();
        if (gPoint.indexOf(" ") != -1) {
            String latString = gPoint.substring(0, gPoint.indexOf(" "));
            String lonString = gPoint.substring(gPoint.indexOf(" "), gPoint.length());
            double latd = Double.parseDouble(latString);
            double lond = Double.parseDouble(lonString);

            returnPoint = new LatLng(latd, lond);
        }
        parser.require(XmlPullParser.END_TAG, ns, "georss:point");
        return returnPoint;

    }

    private String readGuid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "guid");
        String guid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "guid");
        return guid;
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = "";

        while (parser.next() != XmlPullParser.END_TAG) {


            if (parser.getEventType() == XmlPullParser.TEXT) {
                Log.d("XMLPARSER", "TEXT TYPE DETECTED");
                description += parser.getText();
                continue;
            }


            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("b")) {
                description += readBold(parser);
            } else {
                skip(parser);
            }
        }


        //description += readText(parser);

        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readBold(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "b");
        String boldText = "<br/><br/><b>" + readText(parser) + "</b>";
        parser.require(XmlPullParser.END_TAG, ns, "b");
        return boldText;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}