package com.example.fahadshahid.bonusassignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

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

public class ServerList extends AppCompatActivity {

    private static final String TAG = "MTAG";

    private RecyclerView recyclerView;
    private LostAdapter mAdapter;
    Button btn;
    Button logout;
    Gson gson;

    ArrayList<PhoneNumber> lostDetailList1 = new ArrayList();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list2);
        EventBus.getDefault().register(ServerList.this);

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mAdapter = new LostAdapter(ServerList.this, lostDetailList1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);




        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        String list = sharedPref.getString("list","");
        ArrayList<PhoneNumber> str = gson.fromJson(list, ArrayList.class);
        String list2 = sharedPref.getString("list2","");
        ArrayList<PhoneNumber> str2 = gson.fromJson(list2, ArrayList.class);
        Log.i(TAG, "onCreate: " + str);
        Log.i(TAG, "onCreate2: " + str2);

        Collection<PhoneNumber> commonList = CollectionUtils.intersection(str2,str);

        final ArrayList<PhoneNumber> arr = new ArrayList<>(commonList);
        Log.i(TAG, "onCreate3: " + arr);




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

       /* final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMax(800);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setTitle("Please Wait");
        progressDialog.setProgressStyle(0);
        progressDialog.show();
*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.42:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhoneNumberDetail service = retrofit.create(PhoneNumberDetail.class);

        Call<ArrayList<PhoneNumber>> PhoneNumberList = service.getContactNumberList();

        PhoneNumberList.enqueue(new Callback<ArrayList<PhoneNumber>>() {
            @Override
            public void onResponse(Call<ArrayList<PhoneNumber>> call, Response<ArrayList<PhoneNumber>> response) {
               // progressDialog.dismiss();
                Log.i("response_check", "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                ArrayList<PhoneNumber> LostDetailList = response.body();
                PhoneNumberEvent phoneNumberEvent = new PhoneNumberEvent(LostDetailList);
                EventBus.getDefault().post(phoneNumberEvent);
            }

            @Override
            public void onFailure(Call<ArrayList<PhoneNumber>> call, Throwable t) {
                //progressDialog.dismiss();
               mAdapter.changeset(arr);
                Log.i(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");

            }
        });



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(PhoneNumberEvent customEvent) {
        Log.i("check","Hello");
        lostDetailList1 = customEvent.getMessage();
        gson = new Gson();
        String str = gson.toJson(customEvent.getMessage());



        mAdapter.changeset(lostDetailList1);

    }
}
