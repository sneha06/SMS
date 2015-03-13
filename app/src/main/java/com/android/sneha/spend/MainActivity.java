package com.android.sneha.spend;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView smsList;
    List<String> smsAddress;
    List<String> smsBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsList = (ListView) findViewById(R.id.smslistView);
        smsAddress = new ArrayList<String>();
        smsBody = new ArrayList<String>();
        String[][] mKeywords = {{"state bank", "transaction", "ref no"}, {"thanks", "calling", "121"}};

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        for (int i = 0; i < mKeywords.length; i++) {
            if (cursor.moveToFirst()) { // must check the result to prevent exception

                do {
                    int flag = 0;
                    String msgData = (cursor.getString((cursor.getColumnIndexOrThrow("body")))).toLowerCase();
                    for (int k = 0; k < mKeywords[i].length; k++) {
                        if (msgData.contains(mKeywords[i][k])) {
                            flag = k + 1;
                        }
                        else{
                            break;
                        }
                    }
                    if (flag == mKeywords[i].length) {
                        smsAddress.add(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                        smsBody.add(cursor.getString((cursor.getColumnIndexOrThrow("body"))));
                    }

                } while (cursor.moveToNext());

            } else {
                // empty box, no SMS
            }
        }
        SMSAdapter adapter = new SMSAdapter(MainActivity.this, smsAddress, smsBody);
        smsList.setAdapter(adapter);
    }
}
