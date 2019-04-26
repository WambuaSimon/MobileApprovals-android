package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.adapters.UserDocAdapter;
import com.wizag.mobileapprovals.models.UserDocsModel;
import com.wizag.mobileapprovals.utils.MySingleton;
import com.wizag.mobileapprovals.utils.SessionManager;
import com.wizag.mobileapprovals.utils.removeRecyclerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_UserDocuments extends AppCompatActivity implements removeRecyclerItem {
    RecyclerView recyclerView;
    UserDocAdapter userDocsAdapter;
    List<UserDocsModel> docsModelList;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_docs);
        setTitle("Documents");

        sessionManager = new SessionManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        docsModelList = new ArrayList<>();

        //initializing adapter
        userDocsAdapter = new UserDocAdapter(docsModelList, this, new UserDocAdapter.UsersAdapterListener() {
            @Override
            public void approveOnClick(View v, int position) {
                final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        Activity_UserDocuments.this);


                alertDialog2.setTitle("Confirm Approval");


                alertDialog2.setMessage("Are you sure you want to approve this document?");


                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
//                                Toast.makeText(Activity_UserDocuments.this, , Toast.LENGTH_SHORT).show();

                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();
                            }
                        });


                alertDialog2.show();

            }

            @Override
            public void rejectOnClick(View v, int position) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        Activity_UserDocuments.this);

                final EditText edittext = new EditText(getApplicationContext());
                edittext.setPadding(7, 7, 7, 7);

//                alert.setMessage("Enter Your Message");
                alert.setTitle("Reason for Rejecting");

                alert.setView(edittext);

                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        Editable YouEditTextValue = edittext.getText();
                        //OR

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });
        recyclerView.setAdapter(userDocsAdapter);
        /*ADD DOC APPROVAL W/F*/


        loadDocuments();


    }

    private void loadDocuments() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching Documents...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.myjson.com/bins/x2uqg", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        String message = jsonObject.getString("message");
                        JSONArray docs = jsonObject.getJSONArray("documents");
                        for (int k = 0; k < docs.length(); k++) {
                            UserDocsModel model_docs = new UserDocsModel();
                            JSONObject docsObject = docs.getJSONObject(k);


                            String doc_id = docsObject.getString("id");
                            String DocType = docsObject.getString("DocType");
                            String DocName = docsObject.getString("DocName");
                            String AccountName = docsObject.getString("AccountName");
                            String DocDate = docsObject.getString("DocDate");
                            String ExclAmt = docsObject.getString("ExclAmt");
                            String VATAmt = docsObject.getString("VATAmt");
                            String InclAmt = docsObject.getString("InclAmt");
                            String AppStatus = docsObject.getString("AppStatus");


                            if (DocType.equalsIgnoreCase("1")) {
                                DocType = "Invoice";
                            } else if (DocType.equalsIgnoreCase("2")) {
                                DocType = "PO";
                            } else if (DocType.equalsIgnoreCase("3")) {
                                DocType = "Credit Note";
                            } else if (DocType.equalsIgnoreCase("4")) {
                                DocType = "PO";
                            } else if (DocType.equalsIgnoreCase("5")) {
                                DocType = "Receipt";
                            } else if (DocType.equalsIgnoreCase("6")) {
                                DocType = "Cash Memo";
                            } else if (DocType.equalsIgnoreCase("7")) {
                                DocType = "Supplier Note";
                            }

                            /*DOC STATUS*/
                            if (AppStatus.equalsIgnoreCase("1")) {
                                AppStatus = "Not Approved";
                            } else if (AppStatus.equalsIgnoreCase("2")) {
                                AppStatus = "Partially Approved";
                            } else if (AppStatus.equalsIgnoreCase("3")) {
                                AppStatus = "Approved";
                            } else if (AppStatus.equalsIgnoreCase("4")) {
                                AppStatus = "Rejected";
                            }


                            model_docs.setDocType(DocType);
                            model_docs.setDocName(DocName);
                            model_docs.setAccountName(AccountName);
                            model_docs.setDocDate(DocDate);
                            model_docs.setExclAmt("Excl:\t" + ExclAmt);
                            model_docs.setVATAmt("Vat:\t" + VATAmt);
                            model_docs.setInclAmt("Incl:\t" + InclAmt);
                            model_docs.setAppStatus(AppStatus);


                            if (docsModelList.contains(doc_id)) {
                                /*do nothing*/
                            } else {
                                docsModelList.add(model_docs);
                            }
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userDocsAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


        };


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onItemRemoved(UserDocsModel userDocsModel) {
        if (docsModelList.contains(userDocsModel)) {
            docsModelList.remove(userDocsModel);
            userDocsAdapter.notifyDataSetChanged();
        }


        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {

            sessionManager.logoutUser();
            finish();

//            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
