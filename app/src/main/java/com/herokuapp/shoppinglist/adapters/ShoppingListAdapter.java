package com.herokuapp.shoppinglist.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.models.ShoppingList;

import java.util.List;

public class ShoppingListAdapter extends BaseAdapter{
    private Context context;
    private List<ShoppingList> shoppingLists;
    private int mViewIcon;
    private int mDeleteIcon;

    public ShoppingListAdapter(Context context, List<ShoppingList> shoppingLists, int mViewIcon, int mDeleteIcon){
        this.context = context;
        this.shoppingLists = shoppingLists;
        this.mViewIcon = mViewIcon;
        this.mDeleteIcon = mDeleteIcon;
    }

    @Override
    public int getCount() {
        return shoppingLists.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.shopping_list_item, null);
        }
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvListTitle);
        tvTitle.setText(shoppingLists.get(position).getListName());
        return convertView;
    }
}
