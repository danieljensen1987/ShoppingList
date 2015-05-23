package com.herokuapp.shoppinglist.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.IO.InputOutput;
import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.adapters.ShoppingListAdapter;
import com.herokuapp.shoppinglist.fragments.NewListDialogFragment.NewListDialogListener;
import com.herokuapp.shoppinglist.interfaces.CreateListCallback;
import com.herokuapp.shoppinglist.interfaces.DeleteListCallback;
import com.herokuapp.shoppinglist.interfaces.GetAllMyListsCallback;
import com.herokuapp.shoppinglist.interfaces.UpdateListCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;
import com.herokuapp.shoppinglist.preferences.Preferences;
import com.herokuapp.shoppinglist.requests.ListRequests;

import java.util.ArrayList;
import java.util.List;

public class ListsFragment extends ListFragment implements NewListDialogListener{
    static final int PICK_CONTACT_REQUEST = 1;
    Preferences sharedPreferences;
    List<ShoppingList> shoppingLists = new ArrayList<>();
    ShoppingListAdapter adapter;
    InputOutput io;
    int selectedIndex = 0;
    //View view;

    public ListsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lists, container, false);
        Button newListButton = (Button) v.findViewById(R.id.btn_newList);
        Button refreshButton = (Button) v.findViewById(R.id.btn_refreshList);
        sharedPreferences = new Preferences(v.getContext());
        findShoppingLists(v);

        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addList();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findShoppingLists(v);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                selectedIndex = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("What do you want to do?");
                builder.setCancelable(true);
                builder.setPositiveButton("Share list?",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                pickContact();
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton("Delete list? ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteList(shoppingLists.get(position));
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
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

    private void deleteList(ShoppingList sl){
        ListRequests req = new ListRequests(getActivity());
        req.deleteList(sl, new DeleteListCallback() {
            @Override
            public void done(Boolean isDeleted) {
                if(isDeleted){
                    findShoppingLists(getListView());
                }
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
        switch(requestCode) {
            case (PICK_CONTACT_REQUEST):
                if (resultCode == getActivity().RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getActivity().managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getActivity().getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            phoneNumber = phones.getString(phones.getColumnIndex("data1")); // data1 coloumn is the internal coloumn name in android contact database.
                            Log.d("testing", phoneNumber);
                            updateList(phoneNumber);
                        }
                    }
                }
        }
    }

    private void updateList(String phoneNumber){
        shoppingLists.get(selectedIndex).addSubscriber(phoneNumber);
        ListRequests req = new ListRequests(getActivity());
        req.updateList(shoppingLists.get(selectedIndex), new UpdateListCallback() {
            @Override
            public void done(ShoppingList list) {
                Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addList(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        NewListDialogFragment newListDialogFragment = new NewListDialogFragment();
        newListDialogFragment.setTargetFragment(this, 0);
        newListDialogFragment.show(fm, "Create new list");
    }

    @Override
    public void onFinishNewListDialog(String input) {
        ShoppingList newList = new ShoppingList(sharedPreferences.getUID(), input);
        ListRequests requests = new ListRequests(getActivity());
        requests.createList(newList, new CreateListCallback() {
            @Override
            public void done(ShoppingList returned) {
                findShoppingLists(getListView());
            }
        });

    }
}
