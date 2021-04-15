package com.example.kitchenmind.ui.search;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kitchenmind.Adapters.SearchAdapter;
import com.example.kitchenmind.Adapters.latestAdapter;
import com.example.kitchenmind.PojoClasses.Recipes;
import com.example.kitchenmind.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    View root;
    RecyclerView searchRecycler;
    SearchView searchView;
    SearchAdapter searchAdapter;
    ArrayList<String> recipeID, recipeID2, recipeImage, recipeTitle, recipeContent, video, is_like, Total_likes, views;
    ArrayList<Recipes> arrayList;
    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        searchRecycler = root.findViewById(R.id.searchRecycler);
        arrayList=new ArrayList<>();


        searchView = root.findViewById(R.id.search_view);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        searchRecycler.setLayoutManager(linearLayoutManager);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        searchRecycler.setLayoutManager(gridLayoutManager);


        final ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();
        RequestQueue queue1 = Volley.newRequestQueue(getContext());
        String url1 = "http://theerecipies.com/KitchenMind/Blog/android/allRecipes.php";
        Log.d("urlRecipes", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Recipes recipe=new Recipes();
                                recipe.setRecipeID(jsonObject.getString("recipeID"));
                                recipe.setRecipeImage(jsonObject.getString("recipeImage"));
                                recipe.setRecipeTitle(jsonObject.getString("recipeTitle"));
                                recipe.setRecipeContent(jsonObject.getString("recipeContent"));
                                recipe.setVideo(jsonObject.getString("video"));
                                recipe.setViews(jsonObject.getString("views"));
                                arrayList.add(i,recipe);
                            }

//                            Log.d("recipeTitles", String.valueOf(recipeTitle));
//
                            searchAdapter = new SearchAdapter(getContext(),arrayList);

                            searchRecycler.setAdapter(searchAdapter);

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String usetInput=newText.toLowerCase();
                ArrayList<Recipes> newList=new ArrayList<>();
                for(Recipes title : arrayList)
                {
                    if(title.getRecipeTitle().toLowerCase().contains(usetInput))
                    {
                        newList.add(title);
                    }
                }
                searchAdapter.updateList(newList);
                return true;
            }
        });
        return root;
    }

}