package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import es.dmoral.toasty.Toasty;

public class Activity_UserDocuments extends AppCompatActivity implements removeRecyclerItem {
    RecyclerView recyclerView;
    UserDocAdapter userDocsAdapter;
    List<UserDocsModel> docsModelList;
    SessionManager sessionManager;
    Context context;
    String DocType;
    String reason;
    TextView empty_view;
    String groupID;
    String id_to_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_documents);
        setTitle("Documents");
        sessionManager = new SessionManager(getApplicationContext());
        context = this;

        empty_view = findViewById(R.id.empty_view);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        if (!user.isEmpty()) {
            groupID = user.get("GroupID");
        }
        recyclerView = findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        docsModelList = new ArrayList<>();

        loadDocuments();


        //initializing adapter
        userDocsAdapter = new UserDocAdapter(docsModelList, this, new UserDocAdapter.UsersAdapterListener() {
            @Override
            public void approveOnClick(View v, final int position) {
                id_to_update = docsModelList.get(position).DocType;


                final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        context);


                alertDialog2.setTitle("Confirm Approval");


                alertDialog2.setMessage("Are you sure you want to approve this document?");


                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog


                                approve();
                                docsModelList.remove(position);
                                userDocsAdapter.notifyDataSetChanged();
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
            public void rejectOnClick(View v, final int position) {
                id_to_update = docsModelList.get(position).DocType;


                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        context);

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
                            docsModelList.remove(position);
                            userDocsAdapter.notifyDataSetChanged();
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


        recyclerView.setAdapter(userDocsAdapter);

    }

    private void loadDocuments() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching Documents...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://approvals.wizag.biz/api/v1/groupDocs", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {

                        String success = jsonObject.getString("success");
                        String message = jsonObject.getString("message");
                        Toasty.success(context, message, Toast.LENGTH_SHORT).show();

                        JSONArray documents = jsonObject.getJSONArray("documents");

                        if (success.equalsIgnoreCase("true")) {
                            docsModelList.clear();
                            for (int k = 0; k < documents.length(); k++) {

                                UserDocsModel model_docs = new UserDocsModel();
                                JSONObject docsObject = documents.getJSONObject(k);

                                String isApproved = docsObject.getString("IsApproved");
                                JSONArray sequenceID = docsObject.getJSONArray("SequenceID");
                                for (int p = 0; p < sequenceID.length(); p++) {
                                    JSONObject sequenceObject = sequenceID.getJSONObject(p);
                                    String seq_id = sequenceObject.getString("id");

                                    if (seq_id.contains(groupID) && !isApproved.equalsIgnoreCase("1")) {


                                        JSONObject singleDoc = docsObject.getJSONObject("document");
                                        String AppStatus = singleDoc.getString("AppStatus");

                                        if (singleDoc != null && AppStatus.equalsIgnoreCase("0")) {

                                            /*Load Documents*/

                                            DocType = singleDoc.getString("DocType");
                                            String DocName = singleDoc.getString("DocName");
                                            String AccountName = singleDoc.getString("AccountName");
                                            String DocDate = singleDoc.getString("DocDate");
                                            String ExclAmt = singleDoc.getString("ExclAmt");
                                            String VATAmt = singleDoc.getString("VATAmt");
                                            String InclAmt = singleDoc.getString("InclAmt");



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
                                                DocType = "Cash Memo";
                                            } else if (DocType.equalsIgnoreCase("7")) {
                                                DocName = "Supplier Note";
                                            } else {
                                                DocName = "Cheque";
                                            }

                                            model_docs.setDocType(DocType);
                                            model_docs.setDocName(DocName);
                                            model_docs.setAccountName(AccountName);
                                            model_docs.setDocDate(DocDate);
                                            model_docs.setExclAmt("Excl:\t" + ExclAmt);
                                            model_docs.setVATAmt("Vat:\t" + VATAmt);
                                            model_docs.setInclAmt("Incl:\t" + InclAmt);
                                            model_docs.setAppStatus(AppStatus);

                                            Log.d("DocType", DocType);

                                            docsModelList.add(model_docs);


                                        }

                                    }


                                }


                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userDocsAdapter.notifyDataSetChanged();

                toggleEmptyNotes();
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
        } else if (id == R.id.refresh) {
            loadDocuments();

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


    private void approve() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "http://approvals.wizag.biz/api/v1/documents/" + id_to_update,
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
                                Toasty.success(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            } else {
                                Toasty.error(getApplicationContext(), "Error in updating document", Toast.LENGTH_LONG).show();

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
                Toasty.error(getApplicationContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();


                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AppStatus", "1");
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
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "http://approvals.wizag.biz/api/v1/documents/" + id_to_update,
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
                                Toasty.success(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            } else {
                                Toasty.error(getApplicationContext(), "Error in updating document", Toast.LENGTH_LONG).show();

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
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AppStatus", "0");
                params.put("RejectionReason", reason);
                return params;


            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

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

    private void toggleEmptyNotes() {
        if (docsModelList.size() > 0) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
    }

}
