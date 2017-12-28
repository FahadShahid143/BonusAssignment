package com.example.fahadshahid.bonusassignment;

import com.example.fahadshahid.bonusassignment.Model.PhoneNumber;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Fahad Shahid on 12/24/2017.
 */

interface PhoneNumberDetail {

    @GET("phonenumber")
    Call<ArrayList<PhoneNumber>> getPhoneNumberList();

    @GET("contactnumber")
    Call<ArrayList<PhoneNumber>> getContactNumberList();

    @POST("savenumber")
    @FormUrlEncoded
    Call<PhoneNumber> savelost(@Field("contact_number") String number
    );

    @POST("savenumber")
    Call<PhoneNumber> savelost(@Body PhoneNumber phoneNumber);
}
