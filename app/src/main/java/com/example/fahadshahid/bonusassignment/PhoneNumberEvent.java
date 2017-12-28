package com.example.fahadshahid.bonusassignment;

import com.example.fahadshahid.bonusassignment.Model.PhoneNumber;

import java.util.ArrayList;

/**
 * Created by Fahad Shahid on 12/24/2017.
 */

class PhoneNumberEvent {
    private ArrayList<PhoneNumber> message;

    public PhoneNumberEvent(ArrayList<PhoneNumber> message) {
        this.message = message;
    }



    public ArrayList<PhoneNumber> getMessage() {
        return message;
    }
}

