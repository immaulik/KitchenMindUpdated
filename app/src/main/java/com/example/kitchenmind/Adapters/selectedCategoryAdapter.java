package com.example.kitchenmind.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.Activities.categoryActivity;
import com.example.kitchenmind.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class selectedCategoryAdapter extends RecyclerView.Adapter<selectedCategoryAdapter.selectedCategoryViewHolder> {

    categoryActivity categoryActivity;
    ArrayList<String> recipeID;
    ArrayList<String> recipeImage;
    ArrayList<String> recipeTitle;
    ArrayList<String> recipeContent;
    ArrayList<String> total_likes;
    ArrayList<String> video;
    ArrayList<String> is_like, recipeID2;
    ArrayList<String> views;

    String userid;

    SharedPreferences prefs;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    OnCategoryClickListener mListener;

    String imagePath = "http://theerecipies.com/KitchenMind/Blog/admin/uploaded/";

    public selectedCategoryAdapter(categoryActivity categoryActivity, ArrayList<String> recipeID, ArrayList<String> recipeImage, ArrayList<String> recipeTitle, ArrayList<String> recipeContent, ArrayList<String> total_likes, ArrayList<String> video, ArrayList<String> is_like, ArrayList<String> views) {

        this.categoryActivity = categoryActivity;
        this.recipeID = recipeID;
        this.recipeImage = recipeImage;
        this.recipeTitle = recipeTitle;
        this.recipeContent = recipeContent;
        this.video = video;
        this.is_like = is_like;
        this.total_likes = total_likes;
        this.views = views;

        recipeID2 = new ArrayList<>();

        prefs = categoryActivity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }

    public interface OnCategoryClickListener{
        void onCatrgoryClicked(int position);
    }

    public void OnCategoryClickListener(OnCategoryClickListener listener){
        mListener = listener;
    }



    @NonNull
    @Override
    public selectedCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_category_itemfile,parent,false);

        return new selectedCategoryViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final selectedCategoryViewHolder holder, final int position) {

        userid = prefs.getString("userid", "nothing");
        holder.totalLikes.setText(total_likes.get(position));

        holder.recipeName.setText(recipeTitle.get(position));
        Picasso.get().load(imagePath + recipeImage.get(position)).into(holder.recipeImageView);

        holder.likereceipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(categoryActivity);
                String url = "http://theerecipies.com/KitchenMind/Blog/android/LikePost.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response123", response);
                                if (response.equals("inserted")) {
                                    Toast.makeText(categoryActivity, "Liked!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(categoryActivity, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("receipeID", recipeID.get(position));
                        map.put("recipeImage", recipeImage.get(position));
                        map.put("recipeTitle", recipeTitle.get(position));
                        map.put("recipeContent", recipeContent.get(position));
                        map.put("video", video.get(position));
                        map.put("userid", userid);
                        return map;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeID.size();
    }

    public static class selectedCategoryViewHolder extends RecyclerView.ViewHolder{

        TextView recipeName;
        ImageView recipeImageView;
        ImageView likereceipe;
        TextView totalLikes;

        public selectedCategoryViewHolder(@NonNull View itemView, final OnCategoryClickListener listener) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.categoryRecipeNameTextView);
            recipeImageView = itemView.findViewById(R.id.categoryRecipeImageView);
            likereceipe = itemView.findViewById(R.id.categoryLikeReceipe);
            totalLikes = itemView.findViewById(R.id.categoryTotalLike);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position = getAdapterPosition();

                        if (position!=RecyclerView.NO_POSITION){
                            listener.onCatrgoryClicked(position);
                        }

                    }
                }
            });
        }
    }
}
