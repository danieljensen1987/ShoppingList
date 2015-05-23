package com.herokuapp.shoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.herokuapp.shoppinglist.R;

public class AddItemDialogFragment extends DialogFragment implements OnEditorActionListener{
    View view;

    public interface AddItemDialogListener{
        void onFinishAddItemDialog(String input);
    }

    private EditText itemName;

    public AddItemDialogFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_add_item, container);
        itemName = (EditText)view.findViewById(R.id.et_itemName);
        getDialog().setTitle("Add new item");

        itemName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        itemName.setOnEditorActionListener(this);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId){


            AddItemDialogListener callback = (AddItemDialogListener)getTargetFragment();
            callback.onFinishAddItemDialog(itemName.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
