package com.lucien.team.unit;

/**
 * Created by lucien.li on 2015/10/29.
 */
public class Team {
    private String name;
    private int teamId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", teamId=" + teamId +
                '}';
    }
}
