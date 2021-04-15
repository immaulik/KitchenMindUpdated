package com.example.kitchenmind.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgotPasssword extends AppCompatActivity {

    TextInputLayout passwordEditText;
    String username, password;

    SharedPreferences prefs;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    String theme;

    ArrayList<String> emailList;

    @Override
    protected void onStart() {
        super.onStart();

        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        theme = prefs.getString("theme","white");
        Log.d("theme",theme);
//        Toast.makeText(this, theme, Toast.LENGTH_SHORT).show();
        if(theme.contains("dark"))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if(theme.contains("white"))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passsword);

        passwordEditText = findViewById(R.id.forgotPasswordEditText);

        emailList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://theerecipies.com/KitchenMind/Blog/android/fetchEmail.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                emailList.add(jsonObject.getString("email"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

    public void forgotPassword(View view) {

        if (!isemailValidate()) {
            return;
        }

        final String emailId = passwordEditText.getEditText().getText().toString();

        if (emailList.contains(emailId)) {

            Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://theerecipies.com/KitchenMind/Blog/android/emailDetails.php?email=" + emailId;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    username = jsonObject.getString("username");
                                    password = jsonObject.getString("password");
                                }

                                final String fromEmail = "contacteatsin@gmail.com";
                                final String fromPassword = "88777888444US";
                                String toEmails = emailId;

                                final String emailSubject = "Password For Kitchen Mind";
                                final String emailBody = "Username = "+ username+ "\n" + "Password = "+password;

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            MailSender sender = new MailSender(fromEmail,
                                                    fromPassword);
                                            sender.sendMail(emailSubject, emailBody,
                                                    fromEmail, emailId);
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }

                                }).start();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(ForgotPasssword.this, "Please enter valid email id", Toast.LENGTH_SHORT).show();

                }
            });


            queue.add(stringRequest);




        } else {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isemailValidate() {
        String emailString = passwordEditText.getEditText().getText().toString().trim();

        if (emailString.isEmpty()) {
            passwordEditText.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            passwordEditText.setError("Please enter a valid email address");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }

    }

}
