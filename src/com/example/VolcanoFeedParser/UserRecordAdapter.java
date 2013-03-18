package com.example.VolcanoFeedParser;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: wietse
 * Date: 16/03/13
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public class UserRecordAdapter extends ArrayAdapter<UserRecord> {

    private final Context context;
    private ArrayList<UserRecord> users;

    public UserRecordAdapter(Context context, int textViewResourceId, ArrayList<UserRecord> users) {
        super(context, textViewResourceId, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem, null);
        }

        UserRecord user = users.get(position);

        if (user != null) {
            TextView username = (TextView) v.findViewById(R.id.username);
            TextView link = (TextView) v.findViewById(R.id.link);
            TextView description = (TextView) v.findViewById(R.id.description);
            TextView alertLevel = (TextView) v.findViewById(R.id.alertLevel);
            TextView colorCode = (TextView) v.findViewById(R.id.colorCode);
            ImageView avatar = (ImageView) v.findViewById(R.id.avatar);

            if (username != null) {
                username.setText(user.username);
            }

            if (link != null) {
                link.setText("Link: " + user.link);
            }

            if (description != null) {
                description.setText(Html.fromHtml(user.description));
            }

            if (alertLevel != null) {
                alertLevel.setText(user.alertLevel.name());
            }

            if (colorCode != null) {
                colorCode.setText(user.colorCode.name());
            }

            if (avatar != null) {

                avatar.setImageResource(context.getResources()
                        .getIdentifier(user.alertLevel.name().toLowerCase() + "_" + user.colorCode.name().toLowerCase(), "drawable", context.getPackageName()));
            }
        }
        return v;
    }
}