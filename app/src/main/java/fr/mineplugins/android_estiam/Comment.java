package fr.mineplugins.android_estiam;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public int id;
    public String productId;
    public String email;
    public String content;

    public Comment(){

    }

    public void readFromJson(JSONObject object){
        if (object == null) {
            return;
        }

        try {

            // Store "idImdb" in id
            id = object.getInt("id");
            productId = object.getString("productId");
            email = object.getString("email");
            content = object.getString("content");

        } catch (JSONException e) {
            Log.e("Test", "readFromJson: " + e);
        }
    }
}
