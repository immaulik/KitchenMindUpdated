package com.example.kitchenmind.Activities;

import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kitchenmind.R;

public class displayActivity extends AppCompatActivity {

    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
//            setTheme(R.style.darktheme);
//        }else{
//            setTheme(R.style.AppTheme);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

//        aSwitch = findViewById(R.id.mySwitch);
//
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
//            aSwitch.setChecked(true);
//        }
//
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    restartApp();
//                }else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    restartApp();
//                }
//            }
//        });
    }
//    public void restartApp(){
//        Intent intent = new Intent(getApplicationContext(),displayActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
