package com.sudhar.netizen.models;



import com.google.gson.annotations.SerializedName;

import java.util.List;


public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String Username;

    @SerializedName("about")
    private String About;

    @SerializedName("template_count")
    private int TemplateCount;

    @SerializedName("meme_count")
    private int MemeCount;


    @SerializedName("profile_pic")
    private String ProfilePicURL;

    @SerializedName("followers_count")
    private int FollowersCount;

    @SerializedName("following_count")
    private int FollowingCount;

    @SerializedName("followers")
    private List<Followers> Followers;

    @SerializedName("following")
    private List<Followers> Following;

    @SerializedName("is_following")
    private boolean is_following;


    public int getId() {
        return id;
    }

    public String getUsername() {
        return Username;
    }

    public String getAbout() {
        return About;
    }

    public int getTemplateCount() {
        return TemplateCount;
    }

    public int getMemeCount() {
        return MemeCount;
    }



    public List<Followers> getFollowers() {
        return Followers;
    }

    public List<Followers> getFollowing() {
        return Following;
    }

    public int getFollowersCount() {
        return FollowersCount;
    }

    public int getFollowingCount() {
        return FollowingCount;
    }

    public boolean is_following() {
        return is_following;
    }

    public String getProfilePicURL() {
        return ProfilePicURL;
    }

    private class Followers {
        @SerializedName("username")
        private String mUsername;

        public String getUsername() {
            return mUsername;
        }
    }

}

