package com.wizag.mobileapprovals.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.wizag.mobileapprovals.ui.Activity_Login;

import java.util.HashMap;


public class SessionManager {
    // Shared Preferences
    SharedPreferences prefs;


    // Editor for Shared prefserences
    Editor editor;

    // Context
    Context _context;

    // Shared prefs mode
    int PRIVATE_MODE = 0;

    // Sharedprefs file name
    private static final String PREF_NAME = "Wizag";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "AgentName";
    public static final String KEY_GROUPID = "GroupID";
    public static final String KEY_ISADMIN = "IsAdmin";
    public static final String KEY_ISACTIVE = "IsActive";
    public static final String TOKEN = "token";
    public static final String AGENT_ID = "AgentID";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        prefs = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String AgentName, String GroupID, String IsActive,String token,String id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in prefs
        editor.putString(KEY_NAME, AgentName);

        // Storing email in prefs
        editor.putString(KEY_GROUPID, GroupID);
        editor.putString(AGENT_ID, id);

        //storing access token
        // editor.putString(TOKEN, token);
        editor.putString(KEY_ISACTIVE, IsActive);

        editor.putString(TOKEN, token);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Activity_Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_GROUPID, prefs.getString(KEY_GROUPID, null));
        user.put(KEY_ISADMIN, prefs.getString(KEY_ISADMIN, null));
        user.put(AGENT_ID, prefs.getString(AGENT_ID, null));
        user.put(KEY_ISACTIVE, prefs.getString(KEY_ISACTIVE, null));
        user.put(TOKEN, prefs.getString(TOKEN, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Activity_Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     **/
    // Get Login Model_Category
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }
}
