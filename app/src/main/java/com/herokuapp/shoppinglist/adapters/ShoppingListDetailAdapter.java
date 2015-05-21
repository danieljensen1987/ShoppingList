package com.herokuapp.shoppinglist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingListDetailAdapter extends BaseAdapter{
    private Context context;
    private ArrayList dataSet;

    public ShoppingListDetailAdapter(Context context, HashMap<String, Boolean> items){
        this.context = context;
        dataSet = new ArrayList();
        dataSet.addAll(items.entrySet());
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public HashMap.Entry<String, Boolean> getItem(int position) {
        return (HashMap.Entry) dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.shopping_list_detail_item, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.tvItemTitle);
        CheckBox checked = (CheckBox)convertView.findViewById(R.id.cbItemChecked);
        HashMap.Entry<String, Boolean> item = getItem(position);
        title.setText(item.getKey());
        checked.setChecked(item.getValue());

        return convertView;
    }
}
