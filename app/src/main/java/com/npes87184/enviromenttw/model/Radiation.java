package com.npes87184.enviromenttw.model;

/**
 * Created by npes87184 on 2015/4/26.
 */
public class Radiation {

    private String location;
    private String value;

    public Radiation(String location, String value) {
        this.location = location;
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public  String getValue() {
        return value;
    }
}
