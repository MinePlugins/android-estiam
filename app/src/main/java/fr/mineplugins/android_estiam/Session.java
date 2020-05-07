package fr.mineplugins.android_estiam;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setEmail(String email) {
        prefs.edit().putString("email", email).commit();
    }

    public void setName(String name) {
        prefs.edit().putString("name", name).commit();
    }

    public void setId(int id) {
        prefs.edit().putInt("id", id).commit();
    }

    public void clear(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
    public String getEmail() {
        String email = prefs.getString("email","");
        return email;
    }
    public String getName() {
        String name = prefs.getString("name","");
        return name;
    }
    public int getId() {
        int id = prefs.getInt("id",0);
        return id;
    }
}