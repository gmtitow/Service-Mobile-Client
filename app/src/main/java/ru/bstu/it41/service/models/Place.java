package ru.bstu.it41.service.models;

import java.io.Serializable;

/**
 * Created by Герман on 23.04.2018.
 */

public class Place implements Serializable {
    String mDescription;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
