package com.herokuapp.shoppinglist.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.IO.InputOutput;
import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.adapters.ShoppingListAdapter;
import com.herokuapp.shoppinglist.interfaces.GetAllMyListsCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.preferences.Preferences;
import com.herokuapp.shoppinglist.requests.ListRequests;

import java.util.ArrayList;
import java.util.List;

public class ListsFragment extends ListFragment {
    static final int PICK_CONTACT_REQUEST = 1;
    Preferences sharedPreferences;
    List<ShoppingList> shoppingLists = new ArrayList<>();
    ShoppingListAdapter adapter;
    InputOutput io;
    int selectedIndex = 0;

    public ListsFragment() {
    }

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
        if (shoppingLists.size() != 0) {
            Log.d("testing", "From file");
            init();
        } else {
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
                pickContact();
                return true;
            }
        });
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phoneNumber = "";
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }
            }
        }
        cur.close();
        shoppingLists.get(selectedIndex).addSubscriber(phoneNumber);
        Log.d("testing", shoppingLists.get(selectedIndex).subscribers.toString());
    }

}
