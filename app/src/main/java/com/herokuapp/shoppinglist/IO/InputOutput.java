package com.herokuapp.shoppinglist.IO;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.herokuapp.shoppinglist.models.ShoppingList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InputOutput {
    private Context context;
    private File dir;
    private File file;
    String fileName = "MyShoppingLists";

    public InputOutput(Context context){
        this.context = context;
        dir = context.getDir("dataDir", context.MODE_PRIVATE);
        file = new File(dir, fileName);
    }


    public void saveLists(List<ShoppingList> lists) {
        dir = context.getDir("dataDir", context.MODE_PRIVATE);
        file = new File(dir, fileName);
//        File myDir = context.getFilesDir();
        Log.d("testing", file.getAbsolutePath());
        String json = new Gson().toJson(lists);
        try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(json.getBytes());
                fos.flush();
                fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ShoppingList> readLists() {
        StringBuilder total = new StringBuilder();
        dir = context.getDir("dataDir", context.MODE_PRIVATE);
        file = new File(dir, fileName);
        Log.d("testing", file.getAbsolutePath());
        List<ShoppingList> lists = new ArrayList<>();
        try {
            InputStream secondInputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));

            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            secondInputStream.close();
            Type type = new TypeToken<List<ShoppingList>>(){}.getType();
            lists = new Gson().fromJson(""+total, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

}
