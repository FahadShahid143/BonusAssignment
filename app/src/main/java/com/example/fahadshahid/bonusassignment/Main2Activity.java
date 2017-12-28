package com.example.fahadshahid.bonusassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.fahadshahid.bonusassignment.Model.PhoneNumber;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    ArrayList<PhoneNumber> PhoneNumberDetailList1 = new ArrayList();
    Gson gson;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(PhoneNumberEvent customEvent) {

        PhoneNumberDetailList1 = customEvent.getMessage();
        gson = new Gson();
        String str = gson.toJson(customEvent.getMessage());

        Log.i("check", str);


    }

    private static final String TAG = "MTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EventBus.getDefault().register(this);


        Retrofit retrofit = new Retrofit.Builder()
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
        });
    }
}
