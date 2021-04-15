package com.example.kitchenmind.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.Adapters.latestAdapter;
import com.example.kitchenmind.Adapters.selectedCategoryAdapter;
import com.example.kitchenmind.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class categoryActivity extends AppCompatActivity {

    String name;
    RecyclerView categoryView;
    SwipeRefreshLayout swipeRefreshLayout;
    selectedCategoryAdapter adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    private String userid;

    TextView noCategoryTextView;

    String theme;
    ArrayList<String> recipeID, recipeID2, recipeImage, recipeTitle, recipeContent, video, is_like, Total_likes, views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryView = findViewById(R.id.selectedCategoryRecyclerView);

        swipeRefreshLayout = findViewById(R.id.swipeCategoryAcitivity);

        noCategoryTextView = findViewById(R.id.noCategoryTextView);

        name = getIntent().getStringExtra("categoryNAME");
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userid = prefs.getString("userid", "nothing");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeID = new ArrayList<>();
        recipeImage = new ArrayList<>();
        recipeTitle = new ArrayList<>();
        recipeContent = new ArrayList<>();
        video = new ArrayList<>();
        Total_likes = new ArrayList<>();
        is_like = new ArrayList<>();
        recipeID2 = new ArrayList<>();
        views = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        categoryView.setLayoutManager(linearLayoutManager);

        categoryView.setFocusable(false);

        Log.d("islikearray", String.valueOf(is_like));
        RequestQueue queue1 = Volley.newRequestQueue(this);
        String url1 = "http://theerecipies.com/KitchenMind/Blog/android/getCategory.php?post_category=" + name;
        Log.d("urlRecipes", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response1234", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                recipeID.add(jsonObject.getString("recipeID"));
                                recipeImage.add(jsonObject.getString("recipeImage"));
                                recipeTitle.add(jsonObject.getString("recipeTitle"));
                                recipeContent.add(jsonObject.getString("recipeContent"));
                                Total_likes.add(jsonObject.getString("TotalLikes"));
                                video.add(jsonObject.getString("video"));
                                views.add(jsonObject.getString("views"));

                            }

                            if (recipeID.isEmpty() &&
                                    recipeTitle.isEmpty() &&
                                    recipeContent.isEmpty()) {

                                categoryView.setVisibility(View.GONE);

                                noCategoryTextView.setVisibility(View.VISIBLE);

                            } else {

                                Collections.reverse(recipeID);
                                Collections.reverse(recipeImage);
                                Collections.reverse(recipeTitle);
                                Collections.reverse(recipeContent);
                                Collections.reverse(Total_likes);
                                Collections.reverse(video);
                                Collections.reverse(views);

                                adapter = new selectedCategoryAdapter(categoryActivity.this, recipeID,
                                        recipeImage, recipeTitle, recipeContent, Total_likes, video, is_like, views);

                                categoryView.setAdapter(adapter);


                                adapter.OnCategoryClickListener(new selectedCategoryAdapter.OnCategoryClickListener() {
                                    @Override
                                    public void onCatrgoryClicked(int position) {

                                        String count = views.get(position);

                                        Log.d("countActual", count);

                                        int postCount = Integer.parseInt(count) + 1;

                                        Log.d("parsedCountedValue", String.valueOf(postCount));

                                        String id = recipeID.get(position);

                                        Toast.makeText(categoryActivity.this, String.valueOf(postCount), Toast.LENGTH_SHORT).show();

                                        RequestQueue queue = Volley.newRequestQueue(categoryActivity.this);
                                        String url = "http://theerecipies.com/KitchenMind/Blog/android/updateCount.php?recipeID=" + id + "&views=" + postCount;

                                        Log.d("urlUpdated", url);

                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        Log.d("responseUpdated", response);

                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        });
                                        queue.add(stringRequest);

                                        Intent intent = new Intent(categoryActivity.this, fullRecipeActivity.class);
                                        intent.putExtra("recipeID", recipeID.get(position));
                                        intent.putExtra("recipeTitle", recipeTitle.get(position));
                                        intent.putExtra("recipeContent", recipeContent.get(position));
                                        intent.putExtra("recipeImage", recipeImage.get(position));
                                        intent.putExtra("recipeVideo", video.get(position));
                                        startActivity(intent);
                                    }
                                });

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
        queue1.add(stringRequest1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        updateActivity();

                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, 2500);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        updateActivity();

    }

    @Override
    protected void onStart() {
        super.onStart();
        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userid = prefs.getString("userid", "nothing");
        theme = prefs.getString("theme", "white");
        Log.d("theme", theme);
//        Toast.makeText(this, theme, Toast.LENGTH_SHORT).show();
        if (theme.contains("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if (theme.contains("white")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }


    public void updateActivity() {
        recipeID = new ArrayList<>();
        recipeImage = new ArrayList<>();
        recipeTitle = new ArrayList<>();
        recipeContent = new ArrayList<>();
        Total_likes = new ArrayList<>();
        video = new ArrayList<>();
        views = new ArrayList<>();

        RequestQueue queue1 = Volley.newRequestQueue(this);
        String url1 = "http://theerecipies.com/KitchenMind/Blog/android/getCategory.php?post_category=" + name;
        Log.d("urlRecipes", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response1234", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                recipeID.add(jsonObject.getString("recipeID"));
                                recipeImage.add(jsonObject.getString("recipeImage"));
                                recipeTitle.add(jsonObject.getString("recipeTitle"));
                                recipeContent.add(jsonObject.getString("recipeContent"));
                                Total_likes.add(jsonObject.getString("TotalLikes"));
                                video.add(jsonObject.getString("video"));
                                views.add(jsonObject.getString("views"));

                            }

                            Collections.reverse(recipeID);
                            Collections.reverse(recipeImage);
                            Collections.reverse(recipeTitle);
                            Collections.reverse(recipeContent);
                            Collections.reverse(Total_likes);
                            Collections.reverse(video);
                            Collections.reverse(views);

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue1.add(stringRequest1);
    }
}
