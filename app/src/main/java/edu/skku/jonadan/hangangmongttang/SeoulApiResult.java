package edu.skku.jonadan.hangangmongttang;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class SeoulApiResult {

    @Element(name="row")
    private List<Location> row;

    public List<Location> getRow() {
        return row;
    }

    public void setRow(List<Location> row) {
        this.row = row;
    }
}
