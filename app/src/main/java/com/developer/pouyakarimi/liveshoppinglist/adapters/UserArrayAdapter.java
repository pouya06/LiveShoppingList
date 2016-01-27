package com.developer.pouyakarimi.liveshoppinglist.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by pouyakarimi on 1/19/16.
 */
public class UserArrayAdapter  extends ArrayAdapter<ParseUser> {

    private int resource;

    public UserArrayAdapter(Context context, int resource, List<ParseUser> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflatedView = super.getView(position, convertView, parent);

        TextView tv = (TextView) inflatedView.findViewById(android.R.id.text1);

        ParseUser user = getItem(position);
        if (tv != null) {
//            tv.setText(user.get("username").toString());
            tv.setText("hi!");
        }

        return inflatedView;

    }
}
