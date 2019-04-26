package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_Login extends AppCompatActivity {
    Button signin;
    String Login = "http://approvals.wizag.biz/api/v1/login";
    EditText username, password;
    String username_txt, password_txt;
    ConstraintLayout parent_layout;
    SessionManager sessionManager;
    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        setTitle("Login");

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        parent_layout = findViewById(R.id.parent_layout);
        sessionManager = new SessionManager(getApplicationContext());

//        HashMap<String, String> user = sessionManager.getUserDetails();
//        String isAdmin = user.get("IsAdmin");
//
//        Toast.makeText(this, isAdmin, Toast.LENGTH_SHORT).show();
//
//        if (sessionManager.isLoggedIn() && isAdmin.equalsIgnoreCase("1")) {
//            startActivity(new Intent(getApplicationContext(), Activity_Admin_Docs.class));
//            finish();
//        }
//
//        else if (sessionManager.isLoggedIn() && isAdmin.equalsIgnoreCase("0")) {
//            startActivity(new Intent(getApplicationContext(), Activity_UserDocuments.class));
//            finish();
//        }

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp != null) {
            String IsAdmin = sp.getString("IsAdmin", null);
            Toast.makeText(this, IsAdmin, Toast.LENGTH_SHORT).show();


            if (sessionManager.isLoggedIn() && IsAdmin.equalsIgnoreCase("1")) {
                startActivity(new Intent(getApplicationContext(), Activity_Admin_Docs.class));
                finish();
            } else if (sessionManager.isLoggedIn() && IsAdmin.equalsIgnoreCase("0")) {
                startActivity(new Intent(getApplicationContext(), Activity_UserDocuments.class));
                finish();
            }

        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username_txt = username.getText().toString();
                password_txt = password.getText().toString();

                if (username_txt.isEmpty()) {
                    username.setError("Enter Username to proceed");
                } else if (password_txt.isEmpty()) {
                    password.setError("Enter Password to proceed");
                } else if (!isNetworkConnectionAvailable()) {
//                    snackbar with callback
                    Snackbar.make(parent_layout, R.string.connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loginUser();
                                }
                            });


                } else {
                    loginUser();
                }


            }
        });
    }


    private void loginUser() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Login,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //check status if success login user
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String token = data.getString("token");

                            String Agentname = jsonObject.getString("AgentName");
                            String GroupID = jsonObject.getString("GroupID");
                            String IsActive = jsonObject.getString("IsActive");
                            String IsAdmin = jsonObject.getString("IsAdmin");

                            if (IsAdmin.equalsIgnoreCase("0")) {
                                sessionManager.createLoginSession(Agentname,
                                        GroupID,
                                        IsActive,
                                        token);

                                SharedPreferences login = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = login.edit();


                                editor.putString("IsAdmin", "0");
                                editor.apply();

                                startActivity(new Intent(getApplicationContext(), Activity_UserDocuments.class));
                                finish();
                            } else if (IsAdmin.equalsIgnoreCase("1")) {
                                sessionManager.createLoginSession(Agentname,
                                        GroupID,
                                        IsActive,
                                        token);

                                SharedPreferences login = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = login.edit();


                                editor.putString("IsAdmin", "1");
                                editor.apply();


                                startActivity(new Intent(getApplicationContext(), Activity_Admin_Docs.class));
                                finish();
                            }


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Snackbar.make(parent_layout, R.string.error_occured, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginUser();
                            }
                        });

                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AgentName", username_txt);
                params.put("password", password_txt);

                return params;
            }


        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {

            Log.d("Network", "Not Connected");
            return false;
        }
    }

}
