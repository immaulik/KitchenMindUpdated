
package com.example.kitchenmind.PojoClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipes {

    @SerializedName("recipeID")
    @Expose
    private String recipeID;
    @SerializedName("recipeTitle")
    @Expose
    private String recipeTitle;
    @SerializedName("recipeContent")
    @Expose
    private Object recipeContent;
    @SerializedName("recipeImage")
    @Expose
    private String recipeImage;
    @SerializedName("img1")
    @Expose
    private Object img1;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("TotalLikes")
    @Expose
    private String totalLikes;
    @SerializedName("post_category")
    @Expose
    private String postCategory;
    @SerializedName("views")
    @Expose
    private String views;

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public Object getRecipeContent() {
        return recipeContent;
    }

    public void setRecipeContent(Object recipeContent) {
        this.recipeContent = recipeContent;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public Object getImg1() {
        return img1;
    }

    public void setImg1(Object img1) {
        this.img1 = img1;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

}
