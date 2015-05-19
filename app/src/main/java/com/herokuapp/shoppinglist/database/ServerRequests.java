package com.herokuapp.shoppinglist.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import com.google.gson.JsonArray;
//import com.google.gson.JsonParser;
import com.herokuapp.shoppinglist.interfaces.GetUserCallback;
import com.herokuapp.shoppinglist.interfaces.ListCallback;
import com.herokuapp.shoppinglist.models.Credentials;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServerRequests {
    ProgressDialog dialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "https://shoppingapi.herokuapp.com";
    //public static final String SERVER_ADDRESS = "http://192.168.1.143:3000";

    public ServerRequests(Context context){
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle("Processing");
        dialog.setMessage("Please wait...");
    }

    public void createUser(Credentials user, GetUserCallback callback){
        dialog.show();
        new CreateUserAsync(user, callback ).execute();
    }

    public void login(Credentials user, GetUserCallback callback){
        dialog.show();
        new LoginAsync(user, callback ).execute();
    }

    public void findLists(String uid, ListCallback callback){
        dialog.show();
        new FindListsAsync(uid, callback).execute();
    }

    public class CreateUserAsync extends AsyncTask<String, Void, String>{
        Credentials user;
        GetUserCallback userCallback;

        public CreateUserAsync(Credentials user, GetUserCallback callback){
            this.user = user;
            this.userCallback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String uid = "";
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/newuser");

            JSONObject obj = new JSONObject();
            try {
                obj.put("email", user.getEmail());
                obj.put("password", user.getPassword());

                StringEntity se = new StringEntity(obj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                HttpResponse response = client.execute(post);

                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() != 0){
                    uid = jsonObject.getString(("_id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return uid;
        }

        @Override
        protected void onPostExecute(String uid) {
            dialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(uid);
        }
    }

    public class LoginAsync extends AsyncTask<String, Void, String> {
        Credentials creds;
        GetUserCallback userCallback;

        public LoginAsync(Credentials creds, GetUserCallback callback) {
            this.creds = creds;
            this.userCallback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String uid = null;
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/login");

            JSONObject obj = new JSONObject();
            try {
                obj.put("email", creds.getEmail());
                obj.put("password", creds.getPassword());

                StringEntity se = new StringEntity(obj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                HttpResponse response = client.execute(post);

                if(response.getStatusLine().getStatusCode() == 200){
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    JSONObject jsonObject = new JSONObject(result);
                    uid = jsonObject.getString(("_id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uid;
        }

        @Override
        protected void onPostExecute(String uid) {
            dialog.dismiss();
            userCallback.done(uid);
            super.onPostExecute(uid);
        }
    }

    public class FindListsAsync extends AsyncTask<String, Void, List<ShoppingList>>{
        String uid;
        ListCallback callback;

        public FindListsAsync(String uid, ListCallback callback) {
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

//                JsonParser parser = new JsonParser();
//                JsonArray jasonArray = parser.parse(str).getAsJsonArray();
                JSONArray arr = new JSONArray(str);

                for (int i = 0; i < arr.length(); i++){

                    JSONObject obj = (JSONObject) arr.get(i);
                    String author = obj.getString("author");
                    String listName = obj.getString("listName");
                    ShoppingList l = new ShoppingList(author, listName);

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
}