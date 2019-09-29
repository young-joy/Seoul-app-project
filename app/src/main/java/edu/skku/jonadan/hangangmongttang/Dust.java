package edu.skku.jonadan.hangangmongttang;

import com.google.gson.annotations.SerializedName;

public class Dust {

    @SerializedName(value = "CAISTEP")
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
