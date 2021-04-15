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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private TextInputLayout Eemail, Epassword;
    TextView forgotPassword;
    SharedPreferences prefs;
    String theme;

    TextView registerHere;
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
        setContentView(R.layout.activity_login);
        Eemail = findViewById(R.id.luserEmail);
        Epassword = findViewById(R.id.luserPassword);

        registerHere = findViewById(R.id.textViewRegisterHere);

        forgotPassword = findViewById(R.id.forgotPassword);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void GoLogin(View view) {

        if (!isPasswordValidate() | !isemailValidate()) {
            return;
        }

        final String email = Eemail.getEditText().getText().toString();
        final String password = Epassword.getEditText().getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://nacxo.com/KitchenMind/LoginFile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LoginResponse",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("0");
                            Log.d("JSONOBJECT", String.valueOf(jsonObject1));
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("userid", jsonObject1.getString("userid"));
                            editor.putString("username", jsonObject1.getString("name"));
                            editor.putString("email", jsonObject1.getString("email"));
                            editor.putString("password", jsonObject1.getString("password"));
                            editor.apply();
                            String is_login = jsonObject.getString("is_login");
                            Log.d("is_login",is_login);
                            if(is_login.equals("yes"))
                            {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void GoRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void forgotPass(View view) {

        Intent intent = new Intent(LoginActivity.this, ForgotPasssword.class);
        startActivity(intent);
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

    public boolean isPasswordValidate() {
        String passwordString = Epassword.getEditText().getText().toString().trim();

        if (passwordString.isEmpty()) {
            Epassword.setError("Field can't be empty");
            return false;
        } else if (passwordString.length() > 9) {
            Epassword.setError("Max Length is 9 Characters");
            return false;
        } else {
            Epassword.setError(null);
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


}
