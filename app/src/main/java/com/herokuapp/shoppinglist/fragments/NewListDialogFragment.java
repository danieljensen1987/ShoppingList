package com.herokuapp.shoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.herokuapp.shoppinglist.R;

public class NewListDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{
    View view;

    public interface NewListDialogListener{
        void onFinishNewListDialog(String input);
    }

    private EditText listName;

    public NewListDialogFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_list_dialog, container);
        listName = (EditText) view.findViewById(R.id.et_listName);
        getDialog().setTitle(R.string.new_list_dialog_title);
        listName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        listName.setOnEditorActionListener(this);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId){
            NewListDialogListener callback = (NewListDialogListener)getTargetFragment();
            callback.onFinishNewListDialog(listName.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
