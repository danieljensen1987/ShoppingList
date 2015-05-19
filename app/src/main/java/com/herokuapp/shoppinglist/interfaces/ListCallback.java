package com.herokuapp.shoppinglist.interfaces;

import com.herokuapp.shoppinglist.models.ShoppingList;

import java.util.List;

public interface ListCallback {
    void done(List<ShoppingList> lists);
}
