package com.example.kitchenmind.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.BuildConfig;
import com.example.kitchenmind.PojoClasses.InitApplication;
import com.example.kitchenmind.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class settingsActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView displayTextView, shareTextView, rateTextView, feedbackTextVIew, chefNameSetting;
    Switch aSwitch;

    String userid;

    SharedPreferences prefs;

    String imgName;

    String imagePath = "http://theerecipies.com/KitchenMind/Blog/android/";

    LinearLayout profileLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    CircleImageView settingImageView;

    String theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences=getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        editor=preferences.edit();
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
            editor.putString("theme","dark");
            editor.apply();
        }
        else
        {
            setTheme(R.style.LightTheme);
            editor.putString("theme","white");
            editor.apply();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chefNameSetting = findViewById(R.id.chefNameSetting);

        aSwitch=findViewById(R.id.myswitch);
        editor=preferences.edit();

//        toolbar = findViewById(R.id.settingToolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(R.string.toolbar_setting_name);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            aSwitch.setChecked(true);
        }


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putString("theme","dark");
                    editor.apply();
                    restartApp();
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putString("theme","white");
                    editor.apply();
                    restartApp();
                }
            }
        });

        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userid = prefs.getString("userid", "nothing");


        profileLayout = findViewById(R.id.profileLayout);

        shareTextView = findViewById(R.id.shareTextView);
        rateTextView = findViewById(R.id.rateTextView);
        feedbackTextVIew = findViewById(R.id.feedbackTextView);

        settingImageView = findViewById(R.id.settingImageView);

        volleyRequest();

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(settingsActivity.this, editProfileActivity.class);
                startActivity(intent);

            }
        });

        shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        rateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
            }
        });

        feedbackTextVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"darshansoni@email.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                /* Send it off to the Activity-Chooser */
                getApplicationContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
    }

    private void restartApp()
    {
        Intent i=new Intent(getApplicationContext(),settingsActivity.class);
        startActivity(i);
        finish();
    }

    public void LogOut(View view) {
        editor.putString("userid","nothing");
        editor.apply();
        Intent intent=new Intent(settingsActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void volleyRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://theerecipies.com/KitchenMind/Blog/android/profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            chefNameSetting.setText(jsonObject.getString("username"));
                            imgName = jsonObject.getString("imagename");

                            Picasso.get().load(imagePath + imgName).into(settingImageView);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(settingsActivity.this
                        , error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("userid", userid);
                return map;
            }
        };
        queue.add(stringRequest);
    }
}
