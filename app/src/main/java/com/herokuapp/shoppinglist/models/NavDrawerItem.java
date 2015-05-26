package com.herokuapp.shoppinglist.models;

public class NavDrawerItem {
    private int mTitle;
    private int mIcon;

    public NavDrawerItem(){}

    public NavDrawerItem(int title, int icon){
        this.mTitle = title;
        this.mIcon = icon;
    }

    public int getTitle(){
        return this.mTitle;
    }

    public int getIcon(){
        return this.mIcon;
    }

    public void setTitle(int title){
        this.mTitle = title;
    }

    public void setIcon(int icon){
        this.mIcon = icon;
    }
}
