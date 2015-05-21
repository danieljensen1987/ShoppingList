package com.herokuapp.shoppinglist.interfaces;

import com.herokuapp.shoppinglist.models.ShoppingList;

public interface UpdateListCallback {
    void done(ShoppingList list);
}
