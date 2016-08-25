package com.ddtpt.yfw;

import java.util.ArrayList;

/**
 * Created by e228596 on 8/23/2016.
 */
public class DataStore {
    private int myTeamId, myMatchupIndex;
    private ArrayList<Matchup> matchups;

    public DataStore() {
        myMatchupIndex = 0;
        myTeamId = -1;
    }

    public void addMatchup(Matchup match) {
        matchups.add(match);
    }

    public int matchupCount() {
        return matchups.size();
    }

    public int getMyTeamIdByGUID(String user_guid) {
        for (Matchup m : matchups) {
            if (m.getHomeTeam().getOwner_GUID().equals(user_guid)) {
                myTeamId = m.getHomeTeam().getTeam_id();
                return myTeamId;
            } else if (m.getAwayTeam().getOwner_GUID().equals(user_guid)) {
                myTeamId = m.getAwayTeam().getTeam_id();
                return myTeamId;
            }
        }
        return -1;
    }

    public Matchup getMatchupByTeamId(int teamId) {
        for (Matchup m : matchups) {
            if (m.getHomeTeam().getTeam_id() == teamId  || m.getAwayTeam().getTeam_id() == teamId) {
                return m;
            }
        }
        return null;
    }

    public Matchup getMyMatchup() {
        return matchups.get(myMatchupIndex);
    }

    public ArrayList<Matchup> getAllMatchup() {
        return matchups;
    }


}
