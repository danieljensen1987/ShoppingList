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
import com.herokuapp.shoppinglist.models.ShoppingListItem;

import java.util.ArrayList;

public class ShoppingListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ShoppingListItem> shoppingListItems;

    public ShoppingListAdapter(Context context, ArrayList<ShoppingListItem> shoppingListItems){
        this.context = context;
        this.shoppingListItems = shoppingListItems;
    }

    @Override
    public int getCount() {
        return shoppingListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingListItems.get(position);
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
        ImageButton imView = (ImageButton)convertView.findViewById(R.id.ibView);
        ImageButton imDelete = (ImageButton)convertView.findViewById(R.id.ibDelete);
        imView.setFocusable(false);
        imDelete.setFocusable(false);

        tvTitle.setText(shoppingListItems.get(position).getmTitle());
        imView.setImageResource(shoppingListItems.get(position).getmViewIcon());
        imDelete.setImageResource(shoppingListItems.get(position).getmDeleteIcon());

        imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "what "+position, Toast.LENGTH_LONG).show();
            }
        });


        return convertView;
    }
}
