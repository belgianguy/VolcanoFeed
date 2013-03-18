package com.example.VolcanoFeedParser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public UserRecordAdapter(Context context, int textViewResourceId, ArrayList<UserRecord> users)
    {
        super(context, textViewResourceId, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem, null);
        }

        UserRecord user = users.get(position);

        if (user != null)
        {
            TextView username = (TextView) v.findViewById(R.id.username);
            TextView email = (TextView) v.findViewById(R.id.email);

            if (username != null)
            {
                username.setText(user.username);
            }

            if(email != null)
            {
                email.setText("Email: " + user.email );
            }
        }
        return v;
    }
}