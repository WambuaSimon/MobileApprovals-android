package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import es.dmoral.toasty.Toasty;

public class Activity_UserDocumentDetails extends AppCompatActivity {
    Button reject, approve;
    TextView doc_name, doc_date, account_name,
            quantity, price, total,
            exl_amt, incl_amt, vat;
    String reason;
    String DocType, AppStatus;
    String AgentID, LastGroup, LastAgent, NextGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_document_details);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        doc_name = findViewById(R.id.doc_name);
        doc_date = findViewById(R.id.doc_date);
        account_name = findViewById(R.id.account_name);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        total = findViewById(R.id.total);
        exl_amt = findViewById(R.id.exl_amt);
        incl_amt = findViewById(R.id.incl_amt);
        vat = findViewById(R.id.vat);

        /*receive data from UserDocs*/
        Intent intent = getIntent();
        String doc_date_txt = intent.getStringExtra("doc_date");
        String doc_name_txt = intent.getStringExtra("doc_name");
        String account_name_txt = intent.getStringExtra("account_name");
        String exl_amt_txt = intent.getStringExtra("exl_amt");
        String incl_amt_txt = intent.getStringExtra("incl_amt");
        String vat_txt = intent.getStringExtra("vat");
        String price_txt = intent.getStringExtra("price");
        String quantity_txt = intent.getStringExtra("quantity");
        String total_txt = intent.getStringExtra("total");
        DocType = intent.getStringExtra("DocType");
        AppStatus = intent.getStringExtra("AppStatus");

        AgentID = intent.getStringExtra("AgentID");
        LastGroup = intent.getStringExtra("LastGroup");
        LastAgent = intent.getStringExtra("LastAgent");
        NextGroup = intent.getStringExtra("NextGroup");


        doc_name.setText(doc_name_txt);
        doc_date.setText(doc_date_txt);
        account_name.setText(account_name_txt);
        quantity.setText("Quantity: " + quantity_txt);
        price.setText("Price: " + price_txt);
        total.setText("Total: " + total_txt);
        exl_amt.setText("Excl: " + exl_amt_txt);
        incl_amt.setText("Incl: " + incl_amt_txt);
        vat.setText("Vat: " + vat_txt);


        reject = findViewById(R.id.reject);
        approve = findViewById(R.id.approve);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(Activity_UserDocumentDetails.this);
                final EditText edittext = new EditText(getApplicationContext());
                edittext.setPadding(7, 7, 7, 7);
                edittext.setTextColor(getResources().getColor(R.color.black));

//                alert.setMessage("Enter Your Message");
                alert.setTitle("Reason for Rejecting");

                alert.setView(edittext);

                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        reason = edittext.getText().toString();
                        if (reason.isEmpty()) {
                            edittext.setError("Enter Reason to proceed");
                        } else {

                            reject();
//                            docsModelList.remove(position);
//                            userDocsAdapter.notifyDataSetChanged();
                        }
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

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        Activity_UserDocumentDetails.this);

                alertDialog2.setTitle("Confirm Approval");

                alertDialog2.setMessage("Are you sure you want to approve this document?");


                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog


                                approve();
                                updateWorkflow();
                                approve.setVisibility(View.GONE);

//                                docsModelList.remove(position);
//                                userDocsAdapter.notifyDataSetChanged();
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
        });


    }


    private void updateWorkflow() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "http://approvals.wizag.biz/api/v1/workflow/" + DocType,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //check status if success login user
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");


                            if (success == "true") {
                                Toasty.success(getApplicationContext(), message, Toasty.LENGTH_SHORT).show();

                            } else {
                                Toasty.error(getApplicationContext(), "Error in updating document", Toasty.LENGTH_SHORT).show();

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
                error.getMessage();
//                Toasty.error(getApplicationContext(), "An Error Occurred", Toasty.LENGTH_SHORT).show();


                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("SequenceID", );
                params.put("AgentID", AgentID);
                params.put("LastGroup", LastGroup);
                params.put("LastAgent", LastAgent);
                params.put("NextGroup", "");
                params.put("ApprovalStatus", "");
                return params;


            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("token");
                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);

                headers.putAll(headersSys);
                return headers;
            }


        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void approve() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "http://approvals.wizag.biz/api/v1/documents/" + DocType,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //check status if success login user
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");


                            if (success == "true") {
                                Toasty.success(getApplicationContext(), message, Toasty.LENGTH_SHORT).show();

                            } else {
                                Toasty.error(getApplicationContext(), "Error in updating document", Toasty.LENGTH_SHORT).show();

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
                Toasty.error(getApplicationContext(), "An Error Occurred", Toasty.LENGTH_SHORT).show();


                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AppStatus",AppStatus);
                params.put("RejectionReason", "");
                return params;


            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("token");
                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);

                headers.putAll(headersSys);
                return headers;
            }


        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void reject() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "http://approvals.wizag.biz/api/v1/documents/" + DocType,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //check status if success login user
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");


                            if (success == "true") {
                                Toasty.success(getApplicationContext(), message, Toasty.LENGTH_LONG).show();

                            } else {
                                Toasty.error(getApplicationContext(), "Error in updating document", Toasty.LENGTH_LONG).show();

                            }


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                Toasty.error(getApplicationContext(), error.getMessage(), Toasty.LENGTH_SHORT).show();


                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AppStatus", "4");
                params.put("RejectionReason", reason);
                return params;


            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("token");
                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);

                headers.putAll(headersSys);
                return headers;
            }


        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
