package com.oneshoppoint.yates.yates;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by stephineosoro on 05/09/16.
 */
public class MyIntro extends BaseIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*// Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(first_fragment);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);*/

        // Instead of fragments, you can also use our default slide #66B4F2
        // Just set a title, description, background and image. AppIntro will do the rest.
//        addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        addSlide(AppIntroFragment.newInstance("Welcome", "Click 'Get prescription' if you are a patient to get your prescription; If you are a qualified medic, you can register and afterwards login after you have been verified.", R.drawable.home, Color.parseColor("#27ae60")));
        addSlide(AppIntroFragment.newInstance("Get Prescription","Paste your prescription code and enter your location. We'll give you the cheapest chemists around so us to purchase your prescription!", R.drawable.getprescription, Color.parseColor("#34495e")));
        addSlide(AppIntroFragment.newInstance("Medics", "After registering as a medic and being verified, You can add a patient by clicking the + sign and thereafter prescribe a patient.We'll store all the patients you have prescribed for, for easier patient management!", R.drawable.patients, Color.parseColor("#27ae60")));
        addSlide(AppIntroFragment.newInstance("Prescription",  "Prescription has been made much easier!. Choose the relevant fields for the prescription and add a prescription to a patient on the fly!", R.drawable.prescribe, Color.parseColor("#34495e")));



        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        /*// Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(false);
        showDoneButton(true);*/

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
       /* setVibrate(true);
        setVibrateIntensity(30);*/
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        /*Intent intent = new Intent(getApplicationContext(),HomeChoose.class);
        startActivity(intent);*/
//        loadMainActivity();
        finish();
        Toast.makeText(getApplicationContext(), "Skipping", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        /*Intent intent = new Intent(getApplicationContext(),HomeChoose.class);
        startActivity(intent);*/
//        loadMainActivity();
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    public void getStarted(View v) {
        loadMainActivity();
    }
}