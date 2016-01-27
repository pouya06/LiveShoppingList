package com.developer.pouyakarimi.liveshoppinglist.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by pouyakarimi on 1/13/16.
 */
public class UserGroupsArrayAdapter extends ArrayAdapter<ParseObject> {

    private int resource;

    public UserGroupsArrayAdapter(Context context, int resource, List<ParseObject> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflatedView = super.getView(position, convertView, parent);

        TextView tv = (TextView) inflatedView.findViewById(android.R.id.text1);

        ParseObject userGroup = getItem(position);
        if (tv != null) {
            tv.setText(userGroup.get("title").toString());
        }

        return inflatedView;

    }
}
