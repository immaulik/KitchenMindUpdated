package com.example.kitchenmind.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.Activities.categoryActivity;
import com.example.kitchenmind.Adapters.categoryAdapter;
import com.example.kitchenmind.Adapters.latestAdapter;
import com.example.kitchenmind.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    RecyclerView reccepieRecycler, catList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    ArrayList<String> recipeID, recipeImage, recipeTitle, recipeContent, video,
            Total_likes, views;

    ArrayList<String> catID, catName, is_like, recipeID2;
    private String userid;

    TextView noCatTextView, noRecipesTextView;

    SwipeRefreshLayout swipeRefreshLayout;

    public static final String shared_prefs = "SHARED_PREFS_CATEGORY";

    public boolean firstVisit;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        reccepieRecycler = root.findViewById(R.id.latestRecycler);

        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userid = prefs.getString("userid", "nothing");

        catList = root.findViewById(R.id.catList);

        noCatTextView = root.findViewById(R.id.noCatTextView);
        noRecipesTextView = root.findViewById(R.id.noRecipesTextView);

        swipeRefreshLayout = root.findViewById(R.id.homeSwipeRefreshLayout);

        is_like = new ArrayList<>();
        recipeID2 = new ArrayList<>();
        recipeID = new ArrayList<>();
        recipeImage = new ArrayList<>();
        recipeTitle = new ArrayList<>();
        Total_likes = new ArrayList<>();
        views = new ArrayList<>();
        video = new ArrayList<>();
        recipeContent = new ArrayList<>();

        catID = new ArrayList<>();
        catName = new ArrayList<>();

        firstVisit = true;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        reccepieRecycler.setLayoutManager(linearLayoutManager);

        reccepieRecycler.setFocusable(false);
        reccepieRecycler.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManagerCategory = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        catList.setLayoutManager(linearLayoutManagerCategory);

        catList.setFocusable(false);


//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        String url = "http://theerecipies.com/KitchenMind/Blog/android/getLikedposybyUser.php";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("response123", response);
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                is_like.add(jsonObject.getString("is_like"));
//                                recipeID2.add(jsonObject.getString("recipeID"));
//                                Log.d("islikearray", String.valueOf(is_like));
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("userid", userid);
//                return map;
//            }
//        };
//        queue.add(stringRequest);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shuffle();
                    }
                }, 2500);

            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (firstVisit) {

            Log.d("islikearray", String.valueOf(is_like));

//            Call<List<Recipes>> listCall = RetrofitClient
//                    .getInstance()
//                    .getApi()
//                    .getRecipesPosts();
//
//            listCall.enqueue(new Callback<List<Recipes>>() {
//                @Override
//                public void onResponse(Call<List<Recipes>> call, retrofit2.Response<List<Recipes>> response) {
//                    if (!response.isSuccessful()) {
//                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//
//                    List<Recipes> recipes = response.body();
//
//                    for (Recipes recipes1 : recipes) {
//
//                        recipeID = recipes1.getRecipeID();
//                        recipeImage = recipes1.getRecipeImage();
//                        recipeTitle = recipes1.getRecipeTitle();
//                        video = recipes1.getVideo();
//                        Total_likes = recipes1.getTotalLikes();
//                        views = recipes1.getViews();
//                    }
//
//                    Log.d("recipeTitles",recipeTitle);
//
//
//                }
//
//                @Override
//                public void onFailure(Call<List<Recipes>> call, Throwable t) {
//
//                }Likes
//            });

            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.create();
            }
            RequestQueue queue1 = Volley.newRequestQueue(getContext());
            String url1 = "http://nacxo.com/KitchenMind/allRecipes.php";
            Log.d("urlRecipes", url1);
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();
                            Log.d("responseHomeFragment", response);

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

                                if (    recipeID.isEmpty() &&
                                        recipeTitle.isEmpty() &&
                                        recipeContent.isEmpty()) {

                                    reccepieRecycler.setVisibility(View.GONE);

                                    noRecipesTextView.setVisibility(View.VISIBLE);

                                } else {

                                    Collections.reverse(recipeID);
                                    Collections.reverse(recipeImage);
                                    Collections.reverse(recipeTitle);
                                    Collections.reverse(recipeContent);
                                    Collections.reverse(Total_likes);
                                    Collections.reverse(video);
                                    Collections.reverse(views);

                                    Log.d("recipeTitles", String.valueOf(recipeTitle));
                                    Log.d("viewsUpdated", String.valueOf(views));

                                    latestAdapter latestAdapter1 = new latestAdapter(getContext(), recipeID, recipeImage, recipeTitle, recipeContent, video, is_like, Total_likes, views, userid);

                                    reccepieRecycler.setAdapter(latestAdapter1);

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

            RequestQueue queue2 = Volley.newRequestQueue(getContext());
            String url2 = "http://theerecipies.com/KitchenMind/Blog/android/catList.php";
            Log.d("urlRecipes", url2);
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            Log.d("response1234", response);

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    catID.add(jsonObject.getString("cat_id"));
                                    catName.add(jsonObject.getString("cat_name"));
                                }

                                if (catID.isEmpty() && catName.isEmpty()) {
                                    catList.setVisibility(View.GONE);

                                    noCatTextView.setVisibility(View.VISIBLE);
                                } else {

                                    categoryAdapter cat = new categoryAdapter(catID, catName);

                                    catList.setAdapter(cat);

                                    cat.setOnItemClickListener(new categoryAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            String name = catName.get(position);

                                            Intent intent = new Intent(getContext(), categoryActivity.class);
                                            intent.putExtra("categoryNAME", name);

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
            queue2.add(stringRequest2);
            firstVisit = false;

        } else {
//            RequestQueue queue1 = Volley.newRequestQueue(getContext());
//            String url1 = "http://theerecipies.com/KitchenMind/Blog/android/allRecipes.php";
//            Log.d("urlRecipes", url1);
//            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//
//                            Log.d("response1234", response);
//
//                            try {
//                                JSONArray jsonArray = new JSONArray(response);
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                    recipeID.add(jsonObject.getString("recipeID"));
//                                    recipeImage.add(jsonObject.getString("recipeImage"));
//                                    recipeTitle.add(jsonObject.getString("recipeTitle"));
//                                    recipeContent.add(jsonObject.getString("recipeContent"));
//                                    Total_likes.add(jsonObject.getString("TotalLikes"));
//                                    video.add(jsonObject.getString("video"));
//                                    views.add(jsonObject.getString("views"));
//                                }
//
//                                Collections.reverse(recipeID);
//                                Collections.reverse(recipeImage);
//                                Collections.reverse(recipeTitle);
//                                Collections.reverse(recipeContent);
//                                Collections.reverse(Total_likes);
//                                Collections.reverse(video);
//                                Collections.reverse(views);
//
//                                Log.d("recipeTitles", String.valueOf(recipeTitle));
//                                Log.d("viewsUpdated", String.valueOf(views));
//
//                                latestAdapter latestAdapter1 = new latestAdapter(getContext(), recipeID, recipeImage, recipeTitle, recipeContent, video, is_like, Total_likes, views);
//
//                                reccepieRecycler.setAdapter(latestAdapter1);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });
//            queue1.add(stringRequest1);
        }
    }

    public void shuffle() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        swipeRefreshLayout.setRefreshing(false);
    }

}