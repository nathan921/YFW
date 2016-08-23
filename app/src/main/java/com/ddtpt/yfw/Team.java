package com.ddtpt.yfw;

/**
 * Created by e228596 on 8/23/2016.
 */
public class Team {
    private int team_id;
    private String team_name, team_key, image_url, nickname, total_points, projected_points, owner_GUID;

    public Team(int id, String name, String key, String image, String user, String tpoints, String projpoints, String guid) {
        owner_GUID = guid;
        team_id = id;
        team_name = name;
        team_key = key;
        image_url = image;
        nickname = user;
        total_points = tpoints;
        projected_points = projpoints;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public void setTeam_key(String team_key) {
        this.team_key = team_key;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTotal_points(String total_points) {
        this.total_points = total_points;
    }

    public void setProjected_points(String projected_points) {
        this.projected_points = projected_points;
    }

    public String getOwner_GUID() {
        return owner_GUID;
    }

    public int getTeam_id() {
        return team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public String getTeam_key() {
        return team_key;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTotal_points() {
        return total_points;
    }

    public String getProjected_points() {
        return projected_points;
    }
}
