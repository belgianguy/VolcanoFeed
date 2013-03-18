package com.example.VolcanoFeedParser;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ListActivity {
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String FEED_URL = "http://volcanoes.usgs.gov/rss/vhpcaprss.xml";//"http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;
    public static String sPref = null;

    private ArrayList<UserRecord> users;

    private class PullXmlTask extends AsyncTask<String, Void, List<Entry>> {
        @Override
        protected List<Entry> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                //return getResources().getString(R.string.connection_error);
                return new ArrayList<Entry>();
            } catch (XmlPullParserException e) {
                //return getResources().getString(R.string.xml_error);
                return new ArrayList<Entry>();
            }
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            setContentView(R.layout.main);
            // Displays the HTML string in the UI via a WebView
            //WebView myWebView = (WebView) findViewById(R.id.webview);
            //myWebView.loadData(result, "text/html", null);
            //Log.d("XMLPARSER", result);

            for (Entry entry : entries) {

                users.add(new UserRecord(entry.title, entry.link, entry.description, entry.alertLevel, entry.colorCode));
                //entry.link;
                //entry.summary;
                //entry.title;

            }


        }

        // Uploads XML from stackoverflow.com, parses it, and combines it with
        // HTML markup. Returns HTML string.
        private List<Entry> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            MyXMLParser XmlParser = new MyXMLParser();
            List<Entry> entries = null;
            String title = null;
            String url = null;
            String summary = null;
            Calendar rightNow = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

            // Checks whether the user set the preference to include summary text
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean pref = sharedPrefs.getBoolean("summaryPref", false);


            try {
                stream = downloadUrl(urlString);
                entries = XmlParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
            // Each Entry object represents a single post in the XML feed.
            // This section processes the entries list to combine each entry with HTML markup.
            // Each entry is displayed in the UI as a link that optionally includes
            // a text summary.

            /*
            for (Entry entry : entries)
            {
                htmlString.append("<p><a href='");
                htmlString.append(entry.link);
                htmlString.append("'>" + entry.title + "</a></p>");
                // If the user set the preference to include summary text,
                // adds it to the display.
                if (pref) {
                    htmlString.append(entry.summary);
                }
            }
            return htmlString.toString();
            */
            return entries;
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        users = new ArrayList<UserRecord>();

        loadPage();

        //ArrayList<UserRecord> users = new ArrayList<UserRecord>();
        /*users.add(new UserRecord("wietse", "wietse@test.com"));
        users.add(new UserRecord("kate", "kate@test.com"));
        users.add(new UserRecord("jim", "jim@test.com"));
        users.add(new UserRecord("stan", "stan@test.com"));*/


        UserRecordAdapter adapter = new UserRecordAdapter(getApplicationContext(), R.layout.listitem, users);

        setListAdapter(adapter);


    }

    // Uses AsyncTask to download the XML feed from stackoverflow.com.
    public void loadPage() {
        sPref = ANY;
        wifiConnected = true;


        if ((sPref.equals(ANY)) && (wifiConnected || mobileConnected)) {
            new PullXmlTask().execute(FEED_URL);
        } else if ((sPref.equals(WIFI)) && (wifiConnected)) {
            new PullXmlTask().execute(FEED_URL);
        } else {
            Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_LONG).show();
        }
    }


}
