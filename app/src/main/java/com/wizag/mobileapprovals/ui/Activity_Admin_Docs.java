package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.adapters.AdminDocsAdapter;
import com.wizag.mobileapprovals.models.AdminDocsModel;
import com.wizag.mobileapprovals.utils.MySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Admin_Docs extends AppCompatActivity {
    RecyclerView recyclerView;
    AdminDocsAdapter adminDocsAdapter;
    List<AdminDocsModel> docsModelList;

    FloatingActionButton add_doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_docs);

        add_doc = findViewById(R.id.add_doc);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        docsModelList = new ArrayList<>();

        //initializing adapter
        adminDocsAdapter = new AdminDocsAdapter(docsModelList, this);
        recyclerView.setAdapter(adminDocsAdapter);
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
                            AdminDocsModel model_docs = new AdminDocsModel();
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


                            if(DocType.equalsIgnoreCase("1") ){
                                DocType = "Invoice";
                            }else if(DocType.equalsIgnoreCase("2")){
                                DocType = "PO";
                            }
                            else if(DocType.equalsIgnoreCase("3")){
                                DocType = "Credit Note";
                            }

                            else if(DocType.equalsIgnoreCase("4")){
                                DocType = "PO";
                            }

                            else if(DocType.equalsIgnoreCase("5")){
                                DocType = "Receipt";
                            }

                            else if(DocType.equalsIgnoreCase("6")){
                                DocType = "Cash Memo";
                            }

                            else if(DocType.equalsIgnoreCase("7")){
                                DocType = "Supplier Note";
                            }

                            /*DOC STATUS*/
                            if(AppStatus.equalsIgnoreCase("1")){
                                AppStatus = "Not Approved";
                            }
                            else if(AppStatus.equalsIgnoreCase("2")){
                                AppStatus = "Partially Approved";
                            }

                           else if(AppStatus.equalsIgnoreCase("3")){
                                AppStatus = "Approved";
                            }

                            else if(AppStatus.equalsIgnoreCase("4")){
                                AppStatus = "Rejected";
                            }



                            model_docs.setDocType(DocType);
                            model_docs.setDocName(DocName);
                            model_docs.setAccountName(AccountName);
                            model_docs.setDocDate(DocDate);
                            model_docs.setExclAmt("Excl:\t"+ExclAmt);
                            model_docs.setVATAmt("Vat:\t"+VATAmt);
                            model_docs.setInclAmt("Incl:\t"+InclAmt);
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
                adminDocsAdapter.notifyDataSetChanged();
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
}
