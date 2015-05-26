package com.herokuapp.shoppinglist.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.activities.MainActivity;

import java.util.Locale;


public class SettingsFragment extends Fragment {
    String[] languages;
    private Locale myLocale;

    public SettingsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        languages = getResources().getStringArray(R.array.languages);
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        Spinner spinner = (Spinner) v.findViewById(R.id.language_spinner);
        spinner.setPrompt(String.valueOf(R.string.spinner_language));


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        loadLocale();

        Button btnSelect = (Button) v.findViewById(R.id.btn_language);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getBaseContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String language = "en";
                switch (position) {
                    case 0:
                        language = "en";
                        break;
                    case 1:
                        language = "da";
                        break;
                    default:
                        language = "en";
                        break;
                }
                changeLang(language);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });

        return v;
    }


    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

    }


    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

//    private void updateTexts()
//    {
//        txt_hello.setText(R.string.hello_world);
//        btn_en.setText(R.string.btn_en);
//        btn_ru.setText(R.string.btn_ru);
//        btn_fr.setText(R.string.btn_fr);
//        btn_de.setText(R.string.btn_de);
//    }


}
