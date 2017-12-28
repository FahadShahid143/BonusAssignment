package com.example.fahadshahid.bonusassignment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fahad Shahid on 12/24/2017.
 */

public class PhoneNumber {


    @SerializedName("number")
    @Expose
    private String number;


    public PhoneNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
