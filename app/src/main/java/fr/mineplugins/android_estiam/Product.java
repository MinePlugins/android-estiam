package fr.mineplugins.android_estiam;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    public int id;
    public String titre;
    public String url;
    public boolean liked;

    public Product(){

    }

    public void readFromJson(JSONObject object){
        if (object == null) {
            return;
        }

        try {

            // Store "idImdb" in id
            id = object.getInt("id");
            titre = object.getString("title");
            url = object.getString("filename");

        } catch (JSONException e) {
            Log.e("Test", "readFromJson: " + e);
        }
    }
}
