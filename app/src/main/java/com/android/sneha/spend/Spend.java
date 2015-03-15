package com.android.sneha.spend;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by sneha on 14/3/15.
 */
public class Spend extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "IHXeunohWxEoi31ktX9pQlV9zw1l7D8cdPwGAEFH", "RleSriS0NJRfUBdCFwpBtsJXEnayXfLBCujmjW1r");


    }
}
