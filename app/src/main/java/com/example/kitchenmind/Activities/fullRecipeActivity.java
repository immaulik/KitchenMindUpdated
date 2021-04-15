package com.example.kitchenmind.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.Adapters.categoryAdapter;
import com.example.kitchenmind.R;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class fullRecipeActivity extends AppCompatActivity {
    final String MY_PREFS_NAME = "MyPrefsFile";
//    TextView recipeTitleTextView;
//    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    String recipeID, recipeTitle, recipeVideo, recipeContent, recipeImage;
    SharedPreferences prefs;
    String userid;
    ImageView recipeImgView;
    VideoView videoView;
    String VideoUrl = "http://theerecipies.com/KitchenMind/Blog/admin/uploaded/";
    String imgURL = "http://theerecipies.com/KitchenMind/Blog/admin/uploaded/";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
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
        setContentView(R.layout.activity_full_recipe);

        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userid = prefs.getString("userid", "nothing");
        webView = findViewById(R.id.webview_fullRecipe);
//        swipeRefreshLayout = findViewById(R.id.swipeFullrecipe);
        recipeImgView = findViewById(R.id.imageGone);
        videoView = findViewById(R.id.recipeVideoView);

        String videoPath = "android.resource://"+getPackageName() + "/" + R.raw.video;

//        recipeTitleTextView = findViewById(R.id.recipeTitleTextView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setSupportZoom(false);
        webView.setHapticFeedbackEnabled(false);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        recipeID = getIntent().getStringExtra("recipeID");
        Log.d("IIDD", recipeID);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://theerecipies.com/KitchenMind/Blog/android/getParticularPost.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        Log.d("Fullvideo", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            recipeContent = jsonObject.getString("recipeContent");
                            recipeTitle = jsonObject.getString("recipeTitle");
                            recipeImage = jsonObject.getString("recipeImage");
                            recipeVideo = jsonObject.getString("video");
//                            recipeTitleTextView.setText(recipeTitle);
                            fullRecipeActivity.this.setTitle(recipeTitle);

                            webView.loadUrl("http://theerecipies.com/KitchenMind/Blog/viewpost.php?recipeID=" + recipeID);


                            Log.d("recipeImage", recipeImage);

                            if (recipeVideo.isEmpty()) {
                                videoView.setVisibility(View.GONE);

                                recipeImgView.setVisibility(View.VISIBLE);
//
                                Picasso.get().load(imgURL + recipeImage).into(recipeImgView);

                            } else {


                                videoView.setVisibility(View.VISIBLE);

                                Uri uri = Uri.parse(VideoUrl + recipeVideo);
                            Log.d("FinalVideoUrl", String.valueOf(uri));
                            videoView.setVideoURI(uri);

                            }

                            MediaController mediaController = new MediaController(fullRecipeActivity.this);
                            videoView.setMediaController(mediaController);
                            mediaController.setAnchorView(videoView);
                            mediaController.setMediaPlayer(videoView);

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
                Log.d("IIIDDD", recipeID);
                HashMap<String, String> map = new HashMap<>();
                map.put("r_id", recipeID);
                return map;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.likemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.likemenu)
        {
            GoLike();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(fullRecipeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void GoLike() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://theerecipies.com/KitchenMind/Blog/android/LikePost.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response123", response);
                        if (response.equals("inserted")) {
                            Toast.makeText(fullRecipeActivity.this, "Liked!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fullRecipeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("receipeID", recipeID);
                map.put("recipeImage", recipeImage);
                map.put("recipeTitle", recipeTitle);
                map.put("recipeContent", recipeContent);
                map.put("video", recipeVideo);
                map.put("userid", userid);
                return map;
            }
        };
        queue.add(stringRequest);
    }

    public void backEvent(View view) {
        Intent intent=new Intent(fullRecipeActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
