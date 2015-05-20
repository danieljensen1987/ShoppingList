package com.herokuapp.shoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.models.ShoppingList;


public class ListDetailFragment extends Fragment {

    public ListDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_detail, container, false);
        ShoppingList sl = (ShoppingList)getArguments().getSerializable("ListItemObj");
        TextView tv = (TextView)v.findViewById(R.id.details);
        tv.setText(sl.listName);
        return v;
    }

}
