package com.herokuapp.shoppinglist.fragments;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.herokuapp.shoppinglist.IO.InputOutput;
import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.adapters.ShoppingListAdapter;
import com.herokuapp.shoppinglist.requests.ListRequests;
import com.herokuapp.shoppinglist.interfaces.GetAllMyListsCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ListsFragment extends ListFragment{
    Preferences sharedPreferences;
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
        args.putSerializable("ListItemObj", shoppingLists.get(position));
        ldf.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, ldf, "ListDetailFragment");
        transaction.addToBackStack(null);
        transaction.commit();
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
            ListRequests req = new ListRequests(v.getContext());
            req.getAllMyLists(sharedPreferences.getUID(), new GetAllMyListsCallback() {
                @Override
                public void done(List<ShoppingList> lists) {
                    //io.saveLists(lists);
                    shoppingLists = lists;
                    init();
                }
            });
        }
    }

    private void init() {
        adapter = new ShoppingListAdapter(getActivity(), shoppingLists, R.drawable.view, R.drawable.delete);
        setListAdapter(adapter);
    }
}
