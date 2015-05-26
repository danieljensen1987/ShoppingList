package com.herokuapp.shoppinglist.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.adapters.ShoppingListDetailAdapter;
import com.herokuapp.shoppinglist.fragments.AddItemDialogFragment.AddItemDialogListener;
import com.herokuapp.shoppinglist.interfaces.DeleteListCallback;
import com.herokuapp.shoppinglist.interfaces.UpdateListCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.requests.ListRequests;

import java.util.ArrayList;
import java.util.HashMap;


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
                updateList();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList dataSet = new ArrayList();
                dataSet.addAll(sl.items.entrySet());
                final HashMap.Entry<String, Boolean> item = (HashMap.Entry) dataSet.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.list_detail_dialog_message);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.list_detail_dialog_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sl.items.remove(item.getKey());
                                updateList();
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton(R.string.list_detail_dialog_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    private void updateList(){
        ListRequests req = new ListRequests(getActivity());
        req.updateList(sl, new UpdateListCallback() {
            @Override
            public void done(ShoppingList list) {
                Toast.makeText(getActivity(), R.string.toast_message_done, Toast.LENGTH_LONG).show();
                adapter = null;
                adapter = new ShoppingListDetailAdapter(getActivity(), sl.items);
                setListAdapter(adapter);
            }
        });
    }

    private void addItem(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddItemDialogFragment addItemFragment = new AddItemDialogFragment();
        addItemFragment.setTargetFragment(this, 0);
        addItemFragment.show(fm, "Add new");
    }

    @Override
    public void onFinishAddItemDialog(String input) {
        sl.items.put(input, false);
        updateList();
    }
}
