package com.herokuapp.shoppinglist.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingList implements Serializable{
    public String _id;
    public String author;
    public String listName;
    public List<String> subscribers;
    public HashMap<String,Boolean> items;

    public String get_id() {
        return _id;
    }

    public ShoppingList(String author, String listName){
        this.author = author;
        this.listName = listName;
        this.subscribers = new ArrayList<>();
        this.items = new HashMap<>();
    }

    public ShoppingList(String _id, String author, String listName){
        this._id = _id;
        this.author = author;
        this.listName = listName;
        this.subscribers = new ArrayList<>();
        this.items = new HashMap<>();
    }

    public String getListName() {
        return listName;
    }

    public void changeListName(String listName){
        this.listName = listName;
    }

    public void addSubscriber(String subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(String subscriber){
        int i = subscribers.indexOf(subscriber);
        subscribers.remove(i);
    }

    public void addItem (String itemName, Boolean checked){
        if(checked == null){
            items.put(itemName,false);
        } else {
            items.put(itemName,checked);
        }

    }

    public void removeItem(String item){
        items.remove(item);
    }

    public Boolean isCheked(String item){
        return items.get(item);
    }

    public void checked (String item){
        boolean checked = items.get(item);
        if(checked){
            items.put(item,false);
        } else{
            items.put(item, true);
        }
    }
}
