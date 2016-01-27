package com.developer.pouyakarimi.liveshoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.developer.pouyakarimi.liveshoppinglist.R;
import com.developer.pouyakarimi.liveshoppinglist.adapters.UserArrayAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pouyakarimi on 1/14/16.
 */
public class SearchFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    UserArrayAdapter adapter;
    ArrayList<ParseUser> listOfFilteredUsers = new ArrayList<>();

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    public SearchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);


        searchView = (SearchView) rootView.findViewById(R.id.searchViewInSearchFragment);
        listView = (ListView) rootView.findViewById(R.id.listViewInSearchFragment);
        //adapter = new UserArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listOfFilteredUsers );
        //listView.setAdapter(adapter);



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();





        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                if (text.trim().isEmpty())
                    return false;
                final ParseQuery query = ParseUser.getQuery();
                query.whereContains("username", text);
                query.findInBackground(new FindCallback<ParseUser>() {

                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException throwable) {
                        if (parseUsers != null && !(parseUsers.isEmpty())) {
                            adapter = new UserArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listOfFilteredUsers);
                            listView.setAdapter(adapter);
                            adapter.clear();
                            adapter.addAll(parseUsers);
                            adapter.add(ParseUser.getCurrentUser());
                            // adapter = new UserArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listOfFilteredUsers );
                            //listView.setAdapter(adapter);
                            //adapter.clear();
                            //adapter.add(ParseUser.getCurrentUser());
                            //listOfFilteredUsers.clear();
                            //listOfFilteredUsers.addAll(parseUsers);
                            //listOfFilteredUsers.add(ParseUser.getCurrentUser());
                            Toast.makeText(getContext(), "I am here",
                                    Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();
                        } else {
                            listOfFilteredUsers.clear();
                            Toast.makeText(getContext(), "No user found",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                });
                adapter.getFilter().filter(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
