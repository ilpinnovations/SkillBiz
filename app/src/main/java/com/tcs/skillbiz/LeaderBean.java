package com.tcs.skillbiz;

/**
 * Created by 966893 on 7/2/16.
 */
public class LeaderBean {
    String empId, name, score;

    public LeaderBean(String empId, String name, String score) {
        this.empId = empId;
        this.name = name;
        this.score = score;
    }

    public LeaderBean() {
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
