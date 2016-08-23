package com.ddtpt.yfw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by e228596 on 8/23/2016.
 */
public class JsonParser {
    private static final String FANTASY_CONTENT = "fantasy_content";
    private static final String SCOREBOARD = "scoreboard";
    private static final String LEAGUE = "league";
    private static final String MATCHUPS = "matchups";
    private static final String MATCHUP = "matchup";
    private static final String TEAMS = "teams";
    private static final String TEAM = "team";
    private static final String TEAM_KEY = "team_key";
    private static final String TEAM_ID = "team_id";
    private static final String NAME = "name";
    private static final String TEAM_LOGOS = "team_logos";
    private static final String TEAM_LOGO = "team_logo";
    private static final String URL = "url";
    private static final String MANAGERS = "managers";
    private static final String MANAGER = "manager";
    private static final String NICKNAME = "nickname";
    private static final String GUID = "guid";
    private static final String TEAM_POINTS = "team_points";
    private static final String TOTAL = "total";
    private static final String TEAM_PROJECTED_POINTS = "team_projected_points";
    private static final String COUNT = "count";
    private static final String ZERO = "0";



    private String JsonString;

    public JsonParser() {
        JsonString = "";
    }

    public void parseMatchups(JsonElement response) {
        //Gson gson = new GsonBuilder().create();
        int t_id;
        String t_name, t_key, t_image, t_user, t_tpoints, t_projpoints, t_guid;

        t_id = -1;
        t_name = "";
        t_key = "";
        t_guid = "";
        t_image = "";
        t_user = "";
        t_tpoints = "";
        t_projpoints = "";

        JsonArray league = pareDownToLeague(response);
        JsonObject matchups = league
                .get(1).getAsJsonObject()
                .get(SCOREBOARD).getAsJsonObject()
                .get(ZERO).getAsJsonObject()
                .get(MATCHUPS).getAsJsonObject();

        int matches = matchups.get(COUNT).getAsInt();
        for (int i = 0; i < matches; i++) {
            String match_index = String.valueOf(i);
            JsonObject match = matchups.get(match_index).getAsJsonObject().get(MATCHUP).getAsJsonObject();
            JsonObject teams = match.get(ZERO).getAsJsonObject().get(TEAMS).getAsJsonObject();

            Team home, away;
            int team_count =  teams.get(COUNT).getAsInt();
            for (int j = 0; j < team_count; j++) {
                String team_index = String.valueOf(j);
                JsonArray team = teams.get(team_index).getAsJsonObject()
                        .get(TEAM).getAsJsonArray();



                if (j == 0) {
                    home = new Team(t_id, t_name, t_key, t_image, t_user, t_tpoints, t_projpoints, t_guid);
                } else {
                    away = new Team(t_id, t_name, t_key, t_image, t_user, t_tpoints, t_projpoints, t_guid);
                }
            }
        }

    }

    private JsonArray pareDownToLeague(JsonElement json) {
        JsonArray result = json.getAsJsonObject().get(FANTASY_CONTENT)
                .getAsJsonObject().get(LEAGUE).getAsJsonArray();
        return result;
    }
}
