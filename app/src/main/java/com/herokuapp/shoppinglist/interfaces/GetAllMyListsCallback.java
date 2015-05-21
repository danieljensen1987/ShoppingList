package com.herokuapp.shoppinglist.interfaces;

import com.herokuapp.shoppinglist.models.ShoppingList;

import java.util.List;

public interface GetAllMyListsCallback {
    void done(List<ShoppingList> lists);
}
