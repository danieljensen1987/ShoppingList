package com.herokuapp.shoppinglist.interfaces;

import com.herokuapp.shoppinglist.models.ShoppingList;

public interface CreateListCallback {
    void done(ShoppingList returned);
}
