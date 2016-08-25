package com.ddtpt.yfw;

/**
 * Created by e228596 on 8/24/2016.
 */
public class Matchup {
    private Team home;
    private Team away;

    public Matchup(Team h, Team a) {
        home = h;
        away = a;
    }

    public Team getHomeTeam() {
        return home;
    }

    public Team getAwayTeam() {
        return away;
    }
}
