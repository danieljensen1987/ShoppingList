package com.herokuapp.shoppinglist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.IO.InputOutput;
import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.adapters.ShoppingListAdapter;
import com.herokuapp.shoppinglist.database.ServerRequests;
import com.herokuapp.shoppinglist.interfaces.ListCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.models.ShoppingListItem;
import com.herokuapp.shoppinglist.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ListsFragment extends ListFragment{
    Preferences sharedPreferences;
    ArrayList<ShoppingListItem> shoppingListItems = new ArrayList<>();
    List<ShoppingList> shoppingLists = new ArrayList<>();
    ShoppingListAdapter adapter;
    InputOutput io;

    public ListsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lists, container, false);
        sharedPreferences = new Preferences(v.getContext());
        findShoppingLists(v);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ListDetailFragment ldf = new ListDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("ListItemObj", shoppingListItems.get(position).getShoppingList());
        ldf.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.flContent, ldf, "ListDetailFragment").commit();
    }

    private void findShoppingLists(View v) {
        io = new InputOutput(getActivity());
        shoppingLists = io.readLists();
        if(shoppingLists.size() != 0){
            Log.d("testing", "From file");
            init();
        }
        else {
            Log.d("testing", "From server");
            ServerRequests req = new ServerRequests(v.getContext());
            req.findLists(sharedPreferences.getUID(), new ListCallback() {
                @Override
                public void done(List<ShoppingList> lists) {
                    io.saveLists(lists);
                    shoppingLists = io.readLists();
                    init();
                }
            });
        }
    }

    private void init() {
        for(ShoppingList sl : shoppingLists){
            shoppingListItems.add(new ShoppingListItem(sl, R.drawable.view, R.drawable.delete));
        }
        adapter = new ShoppingListAdapter(getActivity(), shoppingListItems);
        setListAdapter(adapter);
    }
}
