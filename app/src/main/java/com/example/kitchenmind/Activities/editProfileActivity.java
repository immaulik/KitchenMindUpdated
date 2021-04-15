package com.example.kitchenmind.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.DatePickerFragment;
import com.example.kitchenmind.R;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class editProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

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
    ImageView iconImageView;

    TextView birthdateTextView;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private final int REQUEST_CODE = 1;
    String gender;

    LinearLayout linearLayout, birthdateLayout;

    String imagePath = "http://theerecipies.com/KitchenMind/Blog/android/";

    Spinner spinner;

    JSONObject jsonObject;
    RequestQueue rQueue;

    TextInputLayout userName, websiteText, bioText, passwordEditText, emailEditText;

    String password = "Darshan";
    SharedPreferences prefs;
    private int mSelectedIndex = 0;
    String pid;
    String imgName;
    String userid;
    private Bitmap bitmap = null;
    String theme;
    private ArrayAdapter mAdapter;
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

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(6);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setTitle(R.string.title_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iconImageView = findViewById(R.id.editProfileIconImage);

        userName = findViewById(R.id.userNameEditText);
        websiteText = findViewById(R.id.websiteEditText);
        bioText = findViewById(R.id.bioEditText);
        passwordEditText = findViewById(R.id.passwordChange);
        linearLayout = findViewById(R.id.passwordTexiInputLayout);
        birthdateLayout = findViewById(R.id.birthdateLayout);
        birthdateTextView = findViewById(R.id.birthdateTextView);
        spinner = findViewById(R.id.spinnerGender);
        emailEditText = findViewById(R.id.emailEditText);

        List<String> genderUser = new ArrayList<>();
        genderUser.add(0, "Gender");
        genderUser.add("Male");
        genderUser.add("Female");
        genderUser.add("Prefer not to say");

        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        pid = prefs.getString("userid", "nothing");
        userid = prefs.getString("userid", "nothing");

        birthdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "date picker");
            }
        });


        mAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,genderUser){
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the spinner collapsed item (non-popup item) as a text view
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.RED);

                // Return the view
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                // Cast the drop down items (popup items) as text view
                TextView tv = (TextView) super.getDropDownView(position,convertView,parent);

                // Set the text color of drop down items
                tv.setTextColor(R.attr.textcolor);

                // If this item is selected item
                if(position == mSelectedIndex){
                    // Set spinner selected popup item's text color
                    tv.setTextColor(R.attr.textcolor);
                }

                // Return the modified view
                return tv;
            }
        };

        spinner.setAdapter(mAdapter);

        spinner.setOnItemSelectedListener(this);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://theerecipies.com/KitchenMind/Blog/android/profile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            userName.getEditText().setText(jsonObject.getString("username"));
                            websiteText.getEditText().setText(jsonObject.getString("website"));
                            bioText.getEditText().setText(jsonObject.getString("bio"));
                            birthdateTextView.setText(jsonObject.getString("b_date"));
                            passwordEditText.getEditText().setText(jsonObject.getString("password"));
                            emailEditText.getEditText().setText(jsonObject.getString("email"));
                            imgName = jsonObject.getString("imagename");
                            String name=jsonObject.getString("gender");
                            Log.d("EDITNAME",name);
                            int position=0;
                            if(name.equals("Prefer not to say"))
                            {
                                position=3;
                            }
                            if(name.equals("Male"))
                            {
                                position=1;
                            }
                            if(name.equals("Female"))
                            {
                                position=2;
                            }
                            spinner.setSelection(position);

                            Picasso.get().load(imagePath + imgName).into(iconImageView);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(editProfileActivity.this
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


        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        birthdateTextView.setText(currentDate);

    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getItemAtPosition(position).equals("Gender")) {



        } else {

            gender = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, gender, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void GoSave(View view) {

        if (!isUsernameValidate() | !isemailValidate() | !isWebsiteValidate() | !isBioValidate() | !isPasswordValidate()){

            return;
        }

        final String username, website, uBio, dateString, password, imagename, email;
        username = userName.getEditText().getText().toString();
        website = websiteText.getEditText().getText().toString();
        uBio = bioText.getEditText().getText().toString();

        final String bio = uBio.replaceAll(" ", "%20");

        dateString = birthdateTextView.getText().toString();

        final String b_date = dateString.replaceAll(" ", "%20");

        password = passwordEditText.getEditText().getText().toString();
        imagename = userid;
        //Log.d("widgetAbcd", susername +" "b_date+ swebsite +" "+ uBio +" "+ sb_date +" "+ spassword +" "+ sgender + " " + simage);

        email = emailEditText.getEditText().getText().toString();

        String img = imageToString(bitmap).replaceAll(" ", "%20");

        Log.d("imageName",img);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        uploadImage();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://theerecipies.com/KitchenMind/Blog/android/updateProfile.php?userid="+userid
                +"&username="+username+"&email="+email+"&website="+website+"&bio="+bio+"&b_date="+b_date
                +"&gender="+gender+"&password="+password;

        Log.d("url12345",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        Toast.makeText(editProfileActivity.this, response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Toast.makeText(editProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            try{

                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
                iconImageView.setImageBitmap(bitmap);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
    }

    public boolean isUsernameValidate() {
        String usernameValid = userName.getEditText().getText().toString().trim();

        if (usernameValid.isEmpty()) {
            userName.setError("Field can't be empty");
            return false;
        } else {
            userName.setError(null);
            return true;
        }
    }

    public boolean isWebsiteValidate() {
        String webValid = websiteText.getEditText().getText().toString().trim();

        if (webValid.isEmpty()) {
            websiteText.setError("Field can't be empty");
            return false;
        } else if (!Patterns.WEB_URL.matcher(webValid).matches()) {
            websiteText.setError("Enter a valid URL type");
            return false;
        } else {
            websiteText.setError(null);
            return true;
        }
    }

    public boolean isBioValidate() {
        String bioString = bioText.getEditText().getText().toString().trim();

        if (bioString.isEmpty()) {
            bioText.setError("Field can't be empty");
            return false;
        } else if (bioString.length() > 50) {
            bioText.setError("Bio is too long!!! It should be in between 0 to 50 characters");
            return false;
        } else {
            bioText.setError(null);
            return true;
        }

    }

    public boolean isemailValidate() {
        String emailString = emailEditText.getEditText().getText().toString().trim();

        if (emailString.isEmpty()) {
            emailEditText.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            emailEditText.setError("Please enter a valid email address");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }

    }

    public boolean isPasswordValidate() {
        String passwordString = passwordEditText.getEditText().getText().toString().trim();

        if (passwordString.isEmpty()) {
            passwordEditText.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordString).matches()) {
            passwordEditText.setError("Password too weak");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }

    }

    public void uploadImage() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://theerecipies.com/KitchenMind/Blog/android/uploadImage.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(editProfileActivity.this, response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(editProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                String image = imageToString(bitmap);

                map.put("userid",userid);
                map.put("imagename","");
                map.put("image",image);
//                map.put("description","Hello");

                return map;
            }
        };

        queue.add(stringRequest);

    }
}

