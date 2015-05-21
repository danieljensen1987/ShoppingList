package com.herokuapp.shoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.adapters.ShoppingListDetailAdapter;
import com.herokuapp.shoppinglist.interfaces.UpdateListCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.requests.ListRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ListDetailFragment extends ListFragment{
    ShoppingListDetailAdapter adapter;
    ShoppingList sl;

    public ListDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_detail, container, false);

        Button update = (Button)v.findViewById(R.id.btn_updateList);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(v);
            }
        });

        sl = (ShoppingList)getArguments().getSerializable("ListItemObj");
        Log.e("testing", "" + sl.items.size());
        adapter = new ShoppingListDetailAdapter(getActivity(), sl.items);
        setListAdapter(adapter);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(sl.listName);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArrayList dataSet = new ArrayList();
        dataSet.addAll(sl.items.entrySet());
        HashMap.Entry<String, Boolean> item = (HashMap.Entry)dataSet.get(position);
        sl.checked(item.getKey());
        adapter = new ShoppingListDetailAdapter(getActivity(), sl.items);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.e("testing", item.getKey() + item.getValue());


//        Toast.makeText(getActivity(), ""+l.getItemAtPosition(position), Toast.LENGTH_LONG).show();
    }



    private void updateList(View v){
        ListRequests req = new ListRequests(v.getContext());
        req.updateList(sl, new UpdateListCallback() {
            @Override
            public void done(ShoppingList list) {
                Toast.makeText(getActivity(), "DONE", Toast.LENGTH_LONG);
            }
        });

    }

}
