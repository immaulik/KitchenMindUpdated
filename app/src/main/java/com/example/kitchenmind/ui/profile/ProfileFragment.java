package com.example.kitchenmind.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.Activities.editProfileActivity;
import com.example.kitchenmind.Adapters.LikedAdapter;
import com.example.kitchenmind.Adapters.ViewPagerAdapter;
import com.example.kitchenmind.R;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    View root;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    TextView editProfileTextView, usertext;
    SharedPreferences prefs;
    private String username, userid;
    RecyclerView likeRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView noItemTextView;

    String imagePath = "http://theerecipies.com/KitchenMind/Blog/android/";

    CircleImageView profileImage;

    String imageName, website;

    ArrayList<String> recipeID, recipeImage, recipeTitle, recipeContent, video, TotalLikes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("username", "nothing");
        userid = prefs.getString("userid", "nothing");

        likeRecyclerView = root.findViewById(R.id.likeRecyclerView);
        swipeRefreshLayout = root.findViewById(R.id.profileSwipeRefreshLayout);

        noItemTextView = root.findViewById(R.id.noItemTextView);

        profileImage = root.findViewById(R.id.profileImageHome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        likeRecyclerView.setLayoutManager(linearLayoutManager);

        likeRecyclerView.setFocusable(false);
        likeRecyclerView.setNestedScrollingEnabled(false);

        Log.d("useridprofile", username);

        recipeID = new ArrayList<>();
        recipeImage = new ArrayList<>();
        recipeTitle = new ArrayList<>();
        recipeContent = new ArrayList<>();
        video = new ArrayList<>();
        TotalLikes = new ArrayList<>();

        usertext = root.findViewById(R.id.txt_username);

        if (!username.equals("nothing")) {
            usertext.setText(username);
        }

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

        final ProgressDialog dialog = new ProgressDialog(getContext());
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
                                TotalLikes.add(jsonObject.getString("TotalLikes"));
                            }

                            if (recipeID.isEmpty() && recipeImage.isEmpty()
                                    && recipeTitle.isEmpty() && recipeContent.isEmpty() && video.isEmpty()
                                    && TotalLikes.isEmpty()) {
                                likeRecyclerView.setVisibility(View.GONE);

                                noItemTextView.setVisibility(View.VISIBLE);
                            } else {

                                Log.d("recipeTitles", String.valueOf(recipeTitle));

                                LikedAdapter latestAdapter1 = new LikedAdapter(getContext(), recipeID, recipeImage, recipeTitle, recipeContent, video, TotalLikes);

                                likeRecyclerView.setAdapter(latestAdapter1);

                            }

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

        RequestQueue queueProfile = Volley.newRequestQueue(getContext());
        String urlProfile = "http://theerecipies.com/KitchenMind/Blog/android/getUserDetails.php?userid=" + userid;

        StringRequest stringRequestProfile = new StringRequest(Request.Method.GET, urlProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                imageName = jsonObject.getString("imagename");

                                Picasso.get().load(imagePath + imageName).into(profileImage);
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

        queueProfile.add(stringRequestProfile);

        editProfileTextView = root.findViewById(R.id.profileEditProfile);

        editProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), editProfileActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    public void shuffle() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        swipeRefreshLayout.setRefreshing(false);
    }
}