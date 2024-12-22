package com.x_force.unimar.Item;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public interface ItemAdapterParent {

    public View getView(int position, View convertView, ViewGroup parent);
    public void setItems(List<Item> items);
    public void notifyDataSetChanged();

}
