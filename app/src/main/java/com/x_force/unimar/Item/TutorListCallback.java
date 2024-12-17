package com.x_force.unimar.Item;

import java.util.List;

public interface TutorListCallback {
    void onTutorListLoaded(List<Item> items);
    void onTutorListError(Exception e);
}
