package com.example.kitchenmind.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.kitchenmind.PojoClasses.Recipes;
import com.example.kitchenmind.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolds> {
    Context context;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ArrayList<Recipes> arrayList;
    String userid;
    int postCount;
    String imagePath = "http://theerecipies.com/KitchenMind/Blog/android/";
    SharedPreferences prefs;
    ArrayList<Recipes> arrayList2;
    public SearchAdapter(Context context, ArrayList<Recipes> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        arrayList2=new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.searchitemfile, parent, false);
        viewHolds v = new viewHolds(view);
        return v;
    }

    public void updateList(ArrayList<Recipes> newlist)
    {
        arrayList=new ArrayList<>();
        arrayList.addAll(newlist);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolds holder, final int position) {
        userid = prefs.getString("userid", "nothing");
//        holder.totalLikes.setText(arrayList.get(position).getTotalLikes());

        holder.recipeName.setText(arrayList.get(position).getRecipeTitle());
        Picasso.get().load(imagePath + arrayList.get(position).getRecipeImage()).into(holder.recipeImageView);

//        holder.likereceipe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RequestQueue queue = Volley.newRequestQueue(context);
//                String url = "http://theerecipies.com/KitchenMind/Blog/android/LikePost.php";
//
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("response123", response);
//                                if (response.equals("inserted")) {
//                                    Toast.makeText(context, "Liked!!", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        HashMap<String, String> map = new HashMap<>();
//                        map.put("receipeID", arrayList.get(position).getRecipeID());
//                        map.put("recipeImage", arrayList.get(position).getRecipeImage());
//                        map.put("recipeTitle", arrayList.get(position).getRecipeTitle());
//                        map.put("recipeContent", (String) arrayList.get(position).getRecipeContent());
//                        map.put("video",arrayList.get(position).getVideo());
//                        map.put("userid", userid);
//                        return map;
//                    }
//                };
//                queue.add(stringRequest);
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String count = arrayList.get(position).getViews();

                Log.d("countActual",count);

                postCount = Integer.parseInt(count)+1;

                Log.d("parsedCountedValue", String.valueOf(postCount));

                String id = arrayList.get(position).getRecipeID();

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
                intent.putExtra("recipeID", arrayList.get(position).getRecipeID());
                intent.putExtra("recipeTitle", arrayList.get(position).getRecipeTitle());
                intent.putExtra("recipeContent", (String) arrayList.get(position).getRecipeContent());
                intent.putExtra("recipeImage", arrayList.get(position).getRecipeImage());
                intent.putExtra("recipeVideo", arrayList.get(position).getVideo());
                context.startActivity(intent);
            }
        });

    }


    class viewHolds extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageView recipeImageView;
//        LinearLayout likereceipe;
//        ImageView view;
//        TextView totalLikes;
        public viewHolds(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.searchtitle);
            recipeImageView = itemView.findViewById(R.id.searchimageview);
//            likereceipe = itemView.findViewById(R.id.likelayout);
//            totalLikes = itemView.findViewById(R.id.totallike);
//            view=itemView.findViewById(R.id.likedreceipe);

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
