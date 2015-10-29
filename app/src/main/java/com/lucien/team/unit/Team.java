package com.lucien.team.unit;

/**
 * Created by lucien.li on 2015/10/29.
 */
public class Team {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }
}
