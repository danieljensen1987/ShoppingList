package com.herokuapp.shoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.database.ServerRequests;
import com.herokuapp.shoppinglist.interfaces.ListCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.preferences.UserLocalStore;

import java.util.ArrayList;
import java.util.List;

public class ListsFragment extends ListFragment {
    UserLocalStore uls;
    List<ShoppingList> shoppingLists = new ArrayList<>();
    ArrayList<String> shoppingTitles = new ArrayList<>();
    ArrayAdapter<String> adapter;

    public ListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lists, container, false);
        uls = new UserLocalStore(v.getContext());
        findShoppingLists(v);
        // Inflate the layout for this fragment
        return v;
    }

    private void findShoppingLists(View v) {
        ServerRequests req = new ServerRequests(v.getContext());
        req.findLists(uls.getUID(), new ListCallback() {
            @Override
            public void done(List<ShoppingList> lists) {
                shoppingLists = lists;
                Log.e("TEST()", "" + shoppingLists.size());
                init();
            }
        });
//        init(v);
    }

    private void init() {

        for (int i = 0; i < shoppingLists.size(); i++) {
            shoppingTitles.add(shoppingLists.get(i).listName);
        }
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, shoppingTitles);
        setListAdapter(adapter);


    }


}
