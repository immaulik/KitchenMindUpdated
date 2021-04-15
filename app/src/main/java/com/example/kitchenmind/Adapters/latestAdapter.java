package com.example.kitchenmind.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Darshan Soni on 5/2/20.
 */
public class latestAdapter extends RecyclerView.Adapter<latestAdapter.ViewHolders>{

    Context context;
    ArrayList<String> recipeID;
    ArrayList<String> recipeImage;
    ArrayList<String> recipeTitle;
    ArrayList<String> nrecipeTitle;
    ArrayList<String> recipeContent;
    String imagePath = "http://nacxo.com/KitchenMind/Images";
    ArrayList<String> video;
    ArrayList<String> views;
    String userid;
    ArrayList<String> total_likes;

    int postCount;

    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.latestitem_file, parent, false);
        ViewHolders v = new ViewHolders(view);
        return v;
    }

    public latestAdapter(Context context, ArrayList<String> recipeID, ArrayList<String> recipeImage, ArrayList<String> recipeTitle, ArrayList<String> recipeContent, ArrayList<String> video, ArrayList<String> is_like, ArrayList<String> total_likes, ArrayList<String> views, String userid) {

        this.context = context;
        this.recipeID = recipeID;
        this.recipeImage = recipeImage;
        this.recipeTitle = recipeTitle;
        this.recipeContent = recipeContent;
        this.video = video;
        this.views = views;
        this.total_likes = total_likes;
        this.nrecipeTitle = new ArrayList<>(recipeTitle);
        this.userid=userid;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolders holder, final int position) {

        int likes = Integer.parseInt(total_likes.get(position));
        likes +=1;

        holder.totalLikes.setText(total_likes.get(position));

        holder.recipeName.setText(recipeTitle.get(position));
        Log.d("imagePath",imagePath + recipeImage.get(position));
        Picasso.get().load(imagePath + recipeImage.get(position)).into(holder.recipeImageView);

        holder.likereceipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://theerecipies.com/KitchenMind/Blog/android/LikePost.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response123", response);
                                if (response.equals("inserted")) {
                                    holder.view.setImageResource(R.drawable.liked);
                                    Toast.makeText(context, "Liked!!", Toast.LENGTH_SHORT).show();
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
                        map.put("recipeImage", recipeImage.get(position));
                        map.put("recipeTitle", recipeTitle.get(position));
                        map.put("recipeContent", recipeContent.get(position));
                        map.put("video", video.get(position));
                        map.put("userid", userid);
                        map.put("TotalLikes",total_likes.get(position));
                        return map;
                    }
                };
                queue.add(stringRequest);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String count = views.get(position);

                Log.d("countActual",count);

                postCount = Integer.parseInt(count)+1;

                Log.d("parsedCountedValue", String.valueOf(postCount));

                String id = recipeID.get(position);

                Toast.makeText(context, String.valueOf(postCount), Toast.LENGTH_SHORT).show();

                RequestQueue queue = Volley.newRequestQueue(context);
                String url ="http://theerecipies.com/KitchenMind/Blog/android/updateCount.php?recipeID="+id+"&views="+postCount;

                Log.d("urlUpdated",url);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("responseUpdated",response);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);

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


    static class ViewHolders extends RecyclerView.ViewHolder {
        TextView recipeName;
        CircleImageView recipeImageView;
        LinearLayout likereceipe;
        ImageView view;
        TextView totalLikes;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.latestRecipeNameTextView);
            recipeImageView = itemView.findViewById(R.id.latestRecipeImageView);
            likereceipe = itemView.findViewById(R.id.likelayout);
            totalLikes = itemView.findViewById(R.id.totallike);
            view=itemView.findViewById(R.id.likedreceipe);
        }
    }
}
