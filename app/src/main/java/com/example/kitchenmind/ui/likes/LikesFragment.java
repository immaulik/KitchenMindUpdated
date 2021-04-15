package com.example.kitchenmind.ui.likes;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.Adapters.LikedAdapter;
import com.example.kitchenmind.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikesFragment extends Fragment {

    View view;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    RecyclerView reccepieRecycler;
    ArrayList<String> recipeID, recipeImage, recipeTitle, recipeContent, video, likeNumber;
    SharedPreferences prefs;

    TextView noItemTextView;

    private String userid;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_likes, container, false);
        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userid = prefs.getString("userid", "nothing");
        Log.d("userid", userid);

        noItemTextView = view.findViewById(R.id.noItemTextView);

        swipeRefreshLayout = view.findViewById(R.id.swipeLikeFragment);

        reccepieRecycler = view.findViewById(R.id.likesRecycler);

        recipeID = new ArrayList<>();
        recipeImage = new ArrayList<>();
        recipeTitle = new ArrayList<>();
        recipeContent = new ArrayList<>();
        video = new ArrayList<>();
        likeNumber = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        reccepieRecycler.setLayoutManager(linearLayoutManager);

        reccepieRecycler.setFocusable(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipe();

                    }
                },2500);
            }
        });

        RequestQueue queueLikes = Volley.newRequestQueue(getContext());
        String urlLikes ="http://theerecipies.com/KitchenMind/Blog/android/allRecipes.php";

        StringRequest stringRequestLikes = new StringRequest(Request.Method.GET, urlLikes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                likeNumber.add(jsonObject.getString("TotalLikes"));

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

        queueLikes.add(stringRequestLikes);

        final ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://theerecipies.com/KitchenMind/Blog/android/getLikedposybyUser.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("response123", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                recipeID.add(jsonObject.getString("receipeID"));
                                recipeImage.add(jsonObject.getString("recipeImage"));
                                recipeTitle.add(jsonObject.getString("recipeTitle"));
                                recipeContent.add(jsonObject.getString("recipeContent"));
                                video.add(jsonObject.getString("video"));
                            }

                            Log.d("recipeTitles", String.valueOf(recipeTitle));

//                            LikedAdapter latestAdapter1 = new LikedAdapter(getContext(), recipeID, recipeImage, recipeTitle, recipeContent, video);
//
//                            reccepieRecycler.setAdapter(latestAdapter1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

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
        return view;
    }

    public void swipe(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        swipeRefreshLayout.setRefreshing(false);
    }

}
