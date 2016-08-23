package com.ddtpt.yfw;

/**
 * Created by e228596 on 8/23/2016.
 */
public class DataStore {
    private int myTeamId, myMatchupIndex, matchupCount;
    private Matchup[] matchups;

    public DataStore() {
        matchupCount = 0;
        myMatchupIndex = 0;
        myTeamId = -1;
    }

    public void addMatchup(Matchup match) {
        matchups[matchupCount] = match;
        matchupCount++;
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
        return matchups[myMatchupIndex];
    }

    public Matchup[] getAllMatchup() {
        return matchups;
    }


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

}
