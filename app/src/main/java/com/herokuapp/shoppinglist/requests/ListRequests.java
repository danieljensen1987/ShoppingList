package com.herokuapp.shoppinglist.requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.herokuapp.shoppinglist.interfaces.GetAllMyListsCallback;
import com.herokuapp.shoppinglist.interfaces.UpdateListCallback;
import com.herokuapp.shoppinglist.models.ShoppingList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListRequests {
    ProgressDialog dialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "https://shoppingapi.herokuapp.com";

    public ListRequests(Context context){
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle("Processing");
        dialog.setMessage("Please wait...");
    }

    public void getAllMyLists(String uid, GetAllMyListsCallback callback){
        dialog.show();
        new GetAllMyListsAsync(uid, callback).execute();
    }

    public void updateList(ShoppingList list, UpdateListCallback callback){
        dialog.show();
        new UpdateListAsync(list, callback).execute();
    }

    public class GetAllMyListsAsync extends AsyncTask<String, Void, List<ShoppingList>>{
        String uid;
        GetAllMyListsCallback callback;

        public GetAllMyListsAsync(String uid, GetAllMyListsCallback callback) {
            this.uid = uid;
            this.callback = callback;
        }

        @Override
        protected List<ShoppingList> doInBackground(String... params) {
            List<ShoppingList> lists = new ArrayList<>();
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpGet get = new HttpGet(SERVER_ADDRESS + "/findLists/" + uid);

            try {
                HttpResponse response = client.execute(get);
                String str = EntityUtils.toString(response.getEntity());

                JSONArray arr = new JSONArray(str);

                for (int i = 0; i < arr.length(); i++){

                    JSONObject obj = (JSONObject) arr.get(i);
                    String _id = obj.getString("_id");
                    String author = obj.getString("author");
                    String listName = obj.getString("listName");
                    ShoppingList l = new ShoppingList(_id, author, listName);

                    String subs = obj.getString("subscribers");
                    JSONArray subArr = new JSONArray(subs);
                    for (int j = 0; j < subArr.length(); j++){
                        l.addSubscriber(subArr.get(j).toString());
                    }

                    String itemString = obj.getString("items");
                    JSONArray itemArr = new JSONArray(itemString);
                    for (int k = 0; k < itemArr.length(); k++){
                        JSONObject ito = itemArr.getJSONObject(k);
                        String itemName = ito.getString("itemName");
                        Boolean checked = ito.getBoolean("checked");
                        l.addItem(itemName, checked);
                    }
                   lists.add(l);
                }
            } catch (Exception e) {
                Log.e("LIST()" , e.getMessage());
                Log.e("LIST()" , e.toString());
                e.printStackTrace();
            }
            return lists;
        }

        @Override
        protected void onPostExecute(List<ShoppingList> lists) {
            dialog.dismiss();
            callback.done(lists);
            super.onPostExecute(lists);
        }
    }

    public class UpdateListAsync extends AsyncTask<String, Void, ShoppingList>{
        ShoppingList list;
        UpdateListCallback callback;

        public UpdateListAsync(ShoppingList list, UpdateListCallback callback){
            this.list = list;
            this.callback = callback;
        }

        @Override
        protected ShoppingList doInBackground(String... params) {
            ShoppingList returnedList = null;

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/updateList");

            JSONObject obj = new JSONObject();
            try {
                obj.put("_id", list._id);
                obj.put("author", list.author);
                obj.put("listName", list.listName);
                JSONArray arr = new JSONArray();
                for(Map.Entry<String,Boolean> entry : list.items.entrySet()){
                    JSONObject items = new JSONObject();
                    items.put("itemName", entry.getKey());
                    items.put("checked", entry.getValue());
                    arr.put(items);
                }
                obj.put("items", arr);
                JSONArray subscribers = new JSONArray(list.subscribers);
                obj.put("subscribers", subscribers);
                Log.d("testing", obj.toString());

                StringEntity se = new StringEntity(obj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                HttpResponse response = client.execute(post);

                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                Log.d("testing return",result);

                if(result.length() != 0){
                    Type type = new TypeToken<ShoppingList>(){}.getType();
                    returnedList = new Gson().fromJson(result, type);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedList;
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            dialog.dismiss();
            callback.done(shoppingList);
            super.onPostExecute(shoppingList);
        }
    }
}