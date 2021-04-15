package com.example.kitchenmind.Api;

import com.example.kitchenmind.PojoClasses.Recipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Darshan Soni on 5/2/20.
 */
public interface Api {

    @GET("allRecipes.php")
    Call<List<Recipes>> getRecipesPosts(

    );
}
