package com.herokuapp.shoppinglist.models;

public class ShoppingListItem {
    private String mTitle;
    private String mQuantity;
    private ShoppingList shoppingList;
    private int mViewIcon;
    private int mDeleteIcon;

    public ShoppingListItem(){};

    public ShoppingListItem(ShoppingList shoppingList, int mViewIcon, int mDeleteIcon){
        this.mTitle = shoppingList.listName;
        this.mQuantity = ""+shoppingList.items.size();
        this.shoppingList = shoppingList;
        this.mViewIcon = mViewIcon;
        this.mDeleteIcon = mDeleteIcon;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    public int getmViewIcon() {
        return mViewIcon;
    }

    public void setmViewIcon(int mViewIcon) {
        this.mViewIcon = mViewIcon;
    }

    public int getmDeleteIcon() {
        return mDeleteIcon;
    }

    public void setmDeleteIcon(int mDeleteIcon) {
        this.mDeleteIcon = mDeleteIcon;
    }
}
