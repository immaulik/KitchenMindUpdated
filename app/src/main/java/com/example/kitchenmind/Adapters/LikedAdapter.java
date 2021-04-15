package com.example.kitchenmind.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.kitchenmind.Activities.fullRecipeActivity;
import com.example.kitchenmind.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikedAdapter extends RecyclerView.Adapter<LikedAdapter.ViewHoldered> {

    Context context;
    ArrayList<String> recipeID;
    ArrayList<String> recipeImage;
    ArrayList<String> recipeTitle;
    ArrayList<String> recipeContent;
    ArrayList<String> video;
    ArrayList<String> totalLikes;

    String imagePath = "http://theerecipies.com/KitchenMind/Blog/admin/uploaded/";

    public LikedAdapter(Context context, ArrayList<String> recipeID, ArrayList<String> recipeImage, ArrayList<String> recipeTitle, ArrayList<String> recipeContent, ArrayList<String> video, ArrayList<String> totalLikes) {

        this.context=context;
        this.recipeID = recipeID;
        this.recipeImage = recipeImage;
        this.recipeTitle = recipeTitle;
        this.recipeContent = recipeContent;
        this.video = video;
        this.totalLikes = totalLikes;

    }


    @NonNull
    @Override
    public ViewHoldered onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dislikepost_itemfile, parent, false);

        ViewHoldered v = new ViewHoldered(view);

        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoldered holder, final int position) {

        holder.likeTextView.setText(totalLikes.get(position));

        holder.likereceipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://theerecipies.com/KitchenMind/Blog/android/DislikePost.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("RESPONSE", response);
                                if (response.equals("delete")) {
                                    Toast.makeText(context, "Disliked!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Not", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("receipeID", recipeID.get(position));
                        return map;
                    }
                };
                queue.add(stringRequest);
            }
        });

        holder.recipeName.setText(recipeTitle.get(position));
        Picasso.get().load(imagePath + recipeImage.get(position)).into(holder.recipeImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, fullRecipeActivity.class);
                intent.putExtra("recipeID", recipeID.get(position));
                intent.putExtra("recipeTitle", recipeTitle.get(position));
                intent.putExtra("recipeContent", recipeContent.get(position));
                intent.putExtra("recipeImage", recipeImage.get(position));
                intent.putExtra("recipeVideo", video.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeID.size();
    }

    class ViewHoldered extends RecyclerView.ViewHolder {
        TextView recipeName,likeTextView;
        ImageView recipeImageView;
        ImageView likereceipe;

        public ViewHoldered(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.likedRecipeNameTextView);
            recipeImageView = itemView.findViewById(R.id.likedRecipeImageView);
            likereceipe = itemView.findViewById(R.id.likeedreceipe);
            likeTextView = itemView.findViewById(R.id.totallike);

        }
    }
}
