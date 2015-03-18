package com.android.sneha.spend;

import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView smsList;
    List<String> smsAddress;
    List<String> smsBody;
    List<Integer> smsRecDate;
    String[] smsReceivedDate;
    String smsbdy,smsadd,smsRDate;
    String formattedDate;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsList = (ListView) findViewById(R.id.smslistView);
        smsAddress = new ArrayList<String>();
        smsBody = new ArrayList<String>();
        smsRecDate = new ArrayList<>();
        String[][] mKeywords = {{"state bank", "transaction", "ref no"}, {"thanks", "calling", "121"}};

        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    userName =  user.getUsername();
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                   userName =  user.getUsername();
                    //Log.d("UserName",userName);
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });



        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        //Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"),new String[]{"date"},null,null,null);
        for (int i = 0; i < mKeywords.length; i++) {
            if (cursor.moveToFirst()) { // must check the result to prevent exception

                do {
                    int flag = 0;
                    String msgData = (cursor.getString((cursor.getColumnIndexOrThrow("body")))).toLowerCase();
                    for (int k = 0; k < mKeywords[i].length; k++) {
                        if (msgData.contains(mKeywords[i][k])) {
                            flag = k + 1;
                        } else {
                            break;
                        }
                    }
                    if (flag == mKeywords[i].length) {

                        smsAddress.add(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                        smsBody.add(cursor.getString((cursor.getColumnIndexOrThrow("body"))));
                       // smsRecDate.add(cursor.getString((cursor.getColumnIndexOrThrow("body"))));
                       smsRecDate.add(cursor.getColumnIndex("date"));
                    }

                } while (cursor.moveToNext());

            } else {
                // empty box, no SMS
            }
        }
        SMSAdapter adapter = new SMSAdapter(MainActivity.this, smsAddress, smsBody);
        smsList.setAdapter(adapter);
        String[]  parseSMSAddress = new String[smsAddress.size()];
        parseSMSAddress =  smsAddress.toArray(parseSMSAddress);

        String[] parseSMSbody = new String[smsBody.size()];
        parseSMSbody = smsBody.toArray(parseSMSbody);

        String[] smsReceivedDate = new String[smsRecDate.size()];
        smsReceivedDate = smsRecDate.toArray(smsReceivedDate);

        for(int i=0;i< parseSMSAddress.length;i++) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
//                Date mDate = sdf.parse(smsReceivedDate[i]);
//                FormattedDate = sdf.format(mDate);
//            } catch (java.text.ParseException e) {
//                e.printStackTrace();
//            }

            smsadd = parseSMSAddress[i];
            smsbdy = parseSMSbody[i];
            smsRDate = smsReceivedDate[i];

            parseStore(smsadd,smsbdy,smsRDate);
        }
    }

    private void parseStore(String pSMSAddress, String pSMSbody,String smsRDate) {
        ParseObject message = new ParseObject("message");

        message.put("Address", pSMSAddress);
        message.put("smsbody", pSMSbody);
        message.put("sms_Received_Date", smsRDate);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //success
                    Toast.makeText(MainActivity.this, "Message sent!", Toast.LENGTH_LONG).show();
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("there was an error sending your message.Please try again")
                            .setTitle("We're sorry")
                            .setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
    }
}
