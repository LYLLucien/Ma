package com.lucien.team.unit;

import java.util.Arrays;

/**
 * Created by lucien.li on 2015/10/29.
 */
public class User {

    private String _id;
    private String name;
    private String avatar;
    private String avatar_thumbnail_url;
    private int late_count;
    private String[] lates;
    private String[] teams;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_thumbnail_url() {
        return avatar_thumbnail_url;
    }

    public void setAvatar_thumbnail_url(String avatar_thumbnail_url) {
        this.avatar_thumbnail_url = avatar_thumbnail_url;
    }

    public int getLate_count() {
        return late_count;
    }

    public void setLate_count(int late_count) {
        this.late_count = late_count;
    }

    public String[] getLates() {
        return lates;
    }

    public void setLates(String[] lates) {
        this.lates = lates;
    }

    public String[] getTeams() {
        return teams;
    }

    public void setTeams(String[] teams) {
        this.teams = teams;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatar_thumbnail_url='" + avatar_thumbnail_url + '\'' +
                ", late_count=" + late_count +
                ", lates=" + Arrays.toString(lates) +
                ", teams=" + Arrays.toString(teams) +
                '}';
    }
}
