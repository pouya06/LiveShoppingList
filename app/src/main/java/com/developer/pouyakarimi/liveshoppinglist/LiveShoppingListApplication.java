package com.developer.pouyakarimi.liveshoppinglist;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by pouyakarimi on 12/21/15.
 */
public class LiveShoppingListApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

    }
}
