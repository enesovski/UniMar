package com.x_force.unimar.Item;

import java.util.List;

public interface ProductListCallback {
    void onProductListLoaded(List<Item> items);
    void onProductListError(Exception e);

}
