package com.developer.pouyakarimi.liveshoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.pouyakarimi.liveshoppinglist.adapters.UserGroupsArrayAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LiveShoppingListActivity {

    private EditText userInput;
    private ListView groupListView;
    private List<ParseObject> userGroups = new ArrayList<>();
    private UserGroupsArrayAdapter groupAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.userGroupToolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddingUserGroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        groupListView = (ListView) findViewById(R.id.userListView);
        groupAdaptor = new UserGroupsArrayAdapter(this, android.R.layout.simple_list_item_1, userGroups);
        groupListView.setAdapter(groupAdaptor);

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "ID: " + userGroups.get(position).getObjectId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserGroup");
        query.whereEqualTo("users", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> userGroups, ParseException error) {
                if(userGroups != null) {
                    groupAdaptor.clear();
                    groupAdaptor.addAll(userGroups);
                }
            }
        });
    }

    private void openDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.group_prompt, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        TextView userInputTitle = (TextView) promptsView.findViewById(R.id.textViewDialogUserInput);
        userInputTitle.setText(getString(R.string.title_group));

        userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (userInput.getText().toString().trim().isEmpty()) {
                                    Toast.makeText(MainActivity.this, getString(R.string.not_saved_entry), Toast.LENGTH_SHORT).show();
                                } else {
                                    new SaveUserGroupAsyncTask().execute(userInput.getText().toString());
                                    //TODO: open an activity and add users search
                                    Intent intent = new Intent(getApplicationContext(), ListsAndUsersActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("users_title", userInput.getText().toString());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialogBuilder.show();
    }

    class SaveUserGroupAsyncTask extends AsyncTask<String, Void, ParseObject> {
        private ParseException parseException;

        @Override
        protected ParseObject doInBackground(String... params) {
            String groupTitle = params[0];
            ParseUser currentUser = ParseUser.getCurrentUser();

            ParseObject userGroup = new ParseObject("UserGroup");
            userGroup.put("title", groupTitle);

            ParseRelation<ParseUser> userGroupUserRelation = userGroup.getRelation("users");
            userGroupUserRelation.add(currentUser);
            //todo: change it to saveEventully()
            try {
                userGroup.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ParseRelation<ParseObject> userUserGroupRelation = currentUser.getRelation("userGroups");
            userUserGroupRelation.add(userGroup);
            //todo: change it to saveEventully()
            try {
                currentUser.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return userGroup;
        }

        @Override
        protected void onPostExecute(ParseObject userGroup) {
            super.onPostExecute(userGroup);

            userGroups.add(userGroup);
            groupAdaptor.notifyDataSetChanged();
        }
    }

//    private void addAndRefreshList() {
//        AsyncTask<Void,Void,List<String>> asyncTask = new AsyncTask<Void, Void, List<String>>() {
//            @Override
//            protected List<String> doInBackground(Void... params){
//
//                return DBHandler.notesArray(1);
//            }
//
//            @Override
//            protected void onPostExecute(List<String> notesList) {
//                notes.clear();
//                notes.addAll(notesList);
//                noteArrayAdapter.notifyDataSetChanged();
//                checkTheListView();
//            }
//        };
//        asyncTask.execute();
//
//    }
}
