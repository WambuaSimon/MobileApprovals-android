package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.adapters.AdminDocsAdapter;
import com.wizag.mobileapprovals.models.AdminDocsModel;
import com.wizag.mobileapprovals.utils.MySingleton;
import com.wizag.mobileapprovals.utils.SessionManager;

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
    SessionManager sessionManager;
    String workflow = "";
    LinearLayout parent_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_docs);
        setTitle("Admin Documents");

        parent_layout = findViewById(R.id.parent_layout);
        add_doc = findViewById(R.id.add_doc);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        docsModelList = new ArrayList<>();
        loadDocuments();
        //initializing adapter
        adminDocsAdapter = new AdminDocsAdapter(docsModelList, this, new AdminDocsAdapter.AdminDocsAdapterListener() {
            @Override
            public void fabOnClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Activity_Approval.class);
                AdminDocsModel adminDocsModel = docsModelList.get(position);
                String docType = adminDocsModel.getDocType();
                String appStatus = adminDocsModel.getAppStatus();

                intent.putExtra("DocType",docType);
                intent.putExtra("AppStatus",appStatus);

                startActivity(intent);


            }

        });


        recyclerView.setAdapter(adminDocsAdapter);
        adminDocsAdapter.notifyDataSetChanged();

        /*ADD DOC APPROVAL W/F*/

        sessionManager = new SessionManager(getApplicationContext());


    }

    private void loadDocuments() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching Documents...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://approvals.wizag.biz/api/v1/documents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        String message = jsonObject.getString("message");
                        JSONArray docs = jsonObject.getJSONArray("documents");
                        docsModelList.clear();
                        for (int k = 0; k < docs.length(); k++) {
//                            docsModelList.clear();
                            AdminDocsModel model_docs = new AdminDocsModel();
                            JSONObject docsObject = docs.getJSONObject(k);


//                            String doc_id = docsObject.getString("id");
                            String DocType = docsObject.getString("DocType");
                            String DocName = docsObject.getString("DocName");
                            String AccountName = docsObject.getString("AccountName");
                            String DocDate = docsObject.getString("DocDate");
                            String ExclAmt = docsObject.getString("ExclAmt");
                            String VATAmt = docsObject.getString("VATAmt");
                            String InclAmt = docsObject.getString("InclAmt");
                            String AppStatus = docsObject.getString("AppStatus");


                            if (DocType.equalsIgnoreCase("1")) {
                                DocName = "Invoice";
                            } else if (DocType.equalsIgnoreCase("2")) {
                                DocName = "PO";
                            } else if (DocType.equalsIgnoreCase("3")) {
                                DocName = "Credit Note";
                            } else if (DocType.equalsIgnoreCase("4")) {
                                DocName = "PO";
                            } else if (DocType.equalsIgnoreCase("5")) {
                                DocName = "Receipt";
                            } else if (DocType.equalsIgnoreCase("6")) {
                                DocName = "Cash Memo";
                            } else if (DocType.equalsIgnoreCase("7")) {
                                DocName = "Supplier Note";
                            }

//                            /*DOC STATUS*/
//                            if (AppStatus.equalsIgnoreCase("1")) {
//                                AppStatus = "Not Approved";
//                            } else if (AppStatus.equalsIgnoreCase("2")) {
//                                AppStatus = "Partially Approved";
//                            } else if (AppStatus.equalsIgnoreCase("3")) {
//                                AppStatus = "Approved";
//                            } else if (AppStatus.equalsIgnoreCase("4")) {
//                                AppStatus = "Rejected";
//                            }


                            model_docs.setDocType(DocType);
                            model_docs.setDocName(DocName);
                            model_docs.setAccountName(AccountName);
                            model_docs.setDocDate(DocDate);
                            model_docs.setExclAmt("Excl:\t" + ExclAmt);
                            model_docs.setVATAmt("Vat:\t" + VATAmt);
                            model_docs.setInclAmt("Incl:\t" + InclAmt);
                            model_docs.setAppStatus(AppStatus);
                            docsModelList.add(model_docs);


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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


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
