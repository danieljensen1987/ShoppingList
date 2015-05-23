package com.herokuapp.shoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.adapters.ShoppingListDetailAdapter;
import com.herokuapp.shoppinglist.interfaces.UpdateListCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.requests.ListRequests;
import com.herokuapp.shoppinglist.fragments.AddItemDialogFragment.AddItemDialogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListDetailFragment extends ListFragment implements AddItemDialogListener{
    ShoppingListDetailAdapter adapter;
    ShoppingList sl;

    public ListDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_detail, container, false);

        Button updateButton = (Button)v.findViewById(R.id.btn_updateList);
        Button addItemButton = (Button)v.findViewById(R.id.btn_addItem);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(v);
            }
        });
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        sl = (ShoppingList)getArguments().getSerializable("ListItemObj");
        adapter = new ShoppingListDetailAdapter(getActivity(), sl.items);
        setListAdapter(adapter);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ArrayList dataSet = new ArrayList();
        dataSet.addAll(sl.items.entrySet());
        HashMap.Entry<String, Boolean> item = (HashMap.Entry)dataSet.get(position);
        sl.checked(item.getKey());
        adapter.notifyDataSetChanged();
    }

    private void updateList(View v){
        ListRequests req = new ListRequests(v.getContext());
        req.updateList(sl, new UpdateListCallback() {
            @Override
            public void done(ShoppingList list) {
                Toast.makeText(getActivity(), "DONE", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void addItem(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddItemDialogFragment addItemFragment = new AddItemDialogFragment();
        addItemFragment.setTargetFragment(this,0);
        addItemFragment.show(fm, "Add new");
    }

    @Override
    public void onFinishAddItemDialog(String input) {
        sl.items.put(input, false);
        Toast.makeText(getActivity(),"Item " + input + " added", Toast.LENGTH_LONG).show();
        // expensive operation for "updating adapter"
        // adapter.notifyDataSetChanged() does
        adapter = null;
        adapter = new ShoppingListDetailAdapter(getActivity(), sl.items);
        setListAdapter(adapter);
    }
}
