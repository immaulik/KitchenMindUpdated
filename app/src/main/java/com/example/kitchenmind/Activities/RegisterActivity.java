package com.example.kitchenmind.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout Ename, Eemail, Epassword;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{9,}" +               //at least 4 characters
                    "$");
    SharedPreferences prefs;
    String theme;
    final String MY_PREFS_NAME = "MyPrefsFile";

    TextView loginHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Ename = findViewById(R.id.ruserName);
        Eemail = findViewById(R.id.ruserEmail);
        Epassword = findViewById(R.id.ruserPassword);

        loginHere = findViewById(R.id.alreadyLogin);

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Go_To_Register(View view) {

        if (!isemailValidate() | !isUsernameValidate() | !isPasswordValidate()){
            return;
        }

        final String email, name, password;
        email = Eemail.getEditText().getText().toString();
        name = Ename.getEditText().getText().toString();
        password = Epassword.getEditText().getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://nacxo.com/KitchenMind/RegisterFile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("Not")) {

                            Toast.makeText(RegisterActivity.this, "You have Registered Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Please Login with different Username,email,Password!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                return map;

            }
        };

        queue.add(stringRequest);
    }

    public boolean isUsernameValidate() {
        String usernameValid = Ename.getEditText().getText().toString().trim();

        if (usernameValid.isEmpty()) {
            Ename.setError("Field can't be empty");
            return false;
        } else {
            Ename.setError(null);
            return true;
        }
    }

    public boolean isemailValidate() {
        String emailString = Eemail.getEditText().getText().toString().trim();

        if (emailString.isEmpty()) {
            Eemail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            Eemail.setError("Please enter a valid email address");
            return false;
        } else {
            Eemail.setError(null);
            return true;
        }

    }

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

    public boolean isPasswordValidate() {
        String passwordString = Epassword.getEditText().getText().toString().trim();

        if (passwordString.isEmpty()) {
            Epassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordString).matches()) {
            Epassword.setError("Password too weak");
            return false;
        }else if (passwordString.length() > 9) {
            Epassword.setError("Password length should be 9 characters only");
            return false;
        } else {
            Epassword.setError(null);
            return true;
        }

    }

}
