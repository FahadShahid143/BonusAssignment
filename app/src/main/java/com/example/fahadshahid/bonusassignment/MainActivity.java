package com.example.fahadshahid.bonusassignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fahadshahid.bonusassignment.Model.PhoneNumber;
import com.google.gson.Gson;

import org.apache.commons.collections4.CollectionUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<PhoneNumber> PhoneNumberDetailList1 = new ArrayList<>();
    Gson gson;

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void test(PhoneNumberEvent customEvent) {

        PhoneNumberDetailList1 = customEvent.getMessage();
        gson = new Gson();
        String str = gson.toJson(customEvent.getMessage());
        String replace = str.replace("{number:","");
       // ArrayList<PhoneNumber> str = customEvent.getMessage();

        *//*ArrayList<Character> charList = new ArrayList<Character>();
        for(int i = 0; i<str.length();i++){
            charList.add(str.charAt(i));
        }*//*
        String str2 = customEvent.getMessage().toString();


        Log.i(TAG, "test: " + replace);
        Log.i(TAG, "test: " + str2);


    }*/

    private static final String TAG = "MTAG";
    ListView listView;
    ArrayList StoreContacts;
    ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    String name, phonenumber;
    public static final int RequestPermissionCode = 1;
    Button button;
    Button serverContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //EventBus.getDefault().register(this);

        listView = (ListView) findViewById(R.id.listview1);

        button = (Button) findViewById(R.id.button1);
        serverContacts = (Button) findViewById(R.id.btnServerContacts);

        StoreContacts = new ArrayList<PhoneNumber>();

        EnableRuntimePermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetContactsIntoArrayList();

                arrayAdapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, StoreContacts
                );

                listView.setAdapter(arrayAdapter);


            }
        });


        serverContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                /*Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.43.42:8000/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PhoneNumberDetail service = retrofit.create(PhoneNumberDetail.class);

                Call<ArrayList<PhoneNumber>> PhoneNumberList = service.getPhoneNumberList();

                PhoneNumberList.enqueue(new Callback<ArrayList<PhoneNumber>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PhoneNumber>> call, Response<ArrayList<PhoneNumber>> response) {
                        Log.i("response_check", "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                        ArrayList<PhoneNumber> LostDetailList = response.body();
                        PhoneNumberEvent phoneNumberEvent = new PhoneNumberEvent(LostDetailList);
                        EventBus.getDefault().post(phoneNumberEvent);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<PhoneNumber>> call, Throwable t) {
                        Log.i(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");

                    }
                });*/
                Intent intent = new Intent(MainActivity.this, ServerList.class);
                startActivity(intent);

            }
        });

    }

    public void GetContactsIntoArrayList() {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            // name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add(phonenumber);
            Log.i(TAG, "GetContactsIntoArrayList: " + phonenumber);




            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.42:8000/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PhoneNumberDetail service1 = retrofit.create(PhoneNumberDetail.class);

            Call<PhoneNumber> LostList = service1.savelost(phonenumber);
            LostList.enqueue(new Callback<PhoneNumber>() {
                @Override


                public void onResponse(Call<PhoneNumber> call, Response<PhoneNumber> response) {
                    Log.i("Post", "onResponse() called with: call = [" + call + "], response = [" + response + "]");

                    //Intent intent = new Intent(MainActivityActivity.this, MainActivity.class);
                    //startActivity(intent);

                }

                @Override
                public void onFailure(Call<PhoneNumber> call, Throwable t) {
                    Log.i("Post", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                    //Toast.makeText(PostActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                }
            });



        }






        //ArrayList Listname = ListUtils.retainAll(list1,list2);

        ArrayList<PhoneNumber> List = (ArrayList<PhoneNumber>) CollectionUtils.retainAll(StoreContacts,PhoneNumberDetailList1);
        ArrayList<PhoneNumber> List2 = (ArrayList<PhoneNumber>) CollectionUtils.subtract(StoreContacts,PhoneNumberDetailList1);
        //ArrayList commonList = CollectionUtils.retainAll(PhoneNumberDetailList1, StoreContacts);
        Log.i("Comparison", "GetContactsIntoArrayList: " + List);


       // SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPref = getSharedPreferences("com.example.fahadshahid.bonusassignment_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        String str = gson.toJson(StoreContacts);
        //put your value
        editor.putString("list2", str);

        //commits your edits
        editor.apply();
        editor.commit();



        cursor.close();

    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(MainActivity.this, "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
