package com.ddtpt.yfw;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by e228596 on 8/23/2016.
 */
public class YahooJsonParser {
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



    private JsonElement fullJsonElement;

    public YahooJsonParser() {
        fullJsonElement = null;
    }
    public YahooJsonParser(String json) {
        fullJsonElement = new JsonParser().parse(json);
    }

    public JsonElement getFullJsonElement() {
        return fullJsonElement;
    }

    public static ArrayList<Matchup> parseMatchups(JsonElement response) {
        ArrayList<Matchup> parsed_matchups = new ArrayList<>();
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
            home = null;
            away = null;
            int team_count = teams.get(COUNT).getAsInt();
            for (int j = 0; j < team_count; j++) {
                String team_index = String.valueOf(j);
                JsonArray team = teams.get(team_index).getAsJsonObject()
                        .get(TEAM).getAsJsonArray();

                t_id = Integer.valueOf(team.get(0).getAsJsonArray()
                        .get(1).getAsJsonObject()
                        .get(TEAM_ID).getAsString());

                t_name = team.get(0).getAsJsonArray()
                        .get(2).getAsJsonObject()
                        .get(NAME).getAsString();

                t_key = team.get(0).getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get(TEAM_KEY).getAsString();

                t_image = team.get(0).getAsJsonArray()
                        .get(5).getAsJsonObject()
                        .get(TEAM_LOGOS).getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get(TEAM_LOGO).getAsJsonObject()
                        .get(URL).getAsString();

                JsonObject manager_object = team.get(0).getAsJsonArray()
                        .get(14).getAsJsonObject()
                        .get(MANAGERS).getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get(MANAGER).getAsJsonObject();

                t_user = manager_object.get(NICKNAME).getAsString();

                t_guid = manager_object.get(GUID).getAsString();

                t_tpoints = team.get(1).getAsJsonObject()
                        .get(TEAM_POINTS).getAsJsonObject()
                        .get(TOTAL).getAsString();

                t_projpoints = team.get(1).getAsJsonObject()
                        .get(TEAM_PROJECTED_POINTS).getAsJsonObject()
                        .get(TOTAL).getAsString();


                if (j == 0) {
                    home = new Team(t_id, t_name, t_key, t_image, t_user, t_tpoints, t_projpoints, t_guid);
                } else {
                    away = new Team(t_id, t_name, t_key, t_image, t_user, t_tpoints, t_projpoints, t_guid);
                }
            }
            parsed_matchups.add(new Matchup(home, away));
        }
        return parsed_matchups;
    }

    private static JsonArray pareDownToLeague(JsonElement json) {
        JsonArray result = json.getAsJsonObject().get(FANTASY_CONTENT)
                .getAsJsonObject().get(LEAGUE).getAsJsonArray();
        return result;
    }
}
