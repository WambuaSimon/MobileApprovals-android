package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.wizag.mobileapprovals.adapters.ApprovalAdapter;
import com.wizag.mobileapprovals.models.ApprovalModel;
import com.wizag.mobileapprovals.utils.MySingleton;
import com.wizag.mobileapprovals.utils.SessionManager;
import com.wizag.mobileapprovals.utils.SwipeAndDragHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Activity_Approval extends AppCompatActivity {
    RecyclerView recyclerView;
    ApprovalAdapter approval_adapter;
    List<ApprovalModel> approval_model;
    Button proceed;
    JSONArray docs;
    String grpID;
    String grpName;
    ApprovalModel approvals;
    List<String> myList;
    JSONArray approve;
    String workflow = "http://approvals.wizag.biz/api/v1/workflow";
    LinearLayout parent_layout;
    String docType, status;
    JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        setTitle("Set Approvals");


        parent_layout = findViewById(R.id.parent_layout);
        recyclerView = findViewById(R.id.approval_recyclerview);
        proceed = findViewById(R.id.proceed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        approval_model = new ArrayList<>();


        //initializing adapter
        approval_adapter = new ApprovalAdapter(approval_model, this);
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(approval_adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        approval_adapter.setTouchHelper(touchHelper);
        recyclerView.setAdapter(approval_adapter);

        touchHelper.attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        docType = intent.getStringExtra("DocType");
        status = intent.getStringExtra("AppStatus");

        Log.d("DocType", docType);
        Log.d("AppStatus", status);


        loadGroups();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Dialog to confirm submission to db*/
                array = new JSONArray();
                for (int i = 0; i < approval_model.size(); i++) {
                    JSONObject obj = new JSONObject();
                    final ApprovalModel approvalModel = approval_model.get(i);


                    myList = new ArrayList<String>();
                    myList.add(approvalModel.getGroupID());

                    try {
                        obj.put("id", approvalModel.getGroupID());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(obj);

                }


                postWorkflow();

            }
        });
    }

    public boolean isExist(String strNama) {
        for (int i = 0; i < approval_model.size(); i++) {
            if (approval_model.get(i).equals(strNama)) {
                return true;
            }
        }

        return false;
    }

    private void loadGroups() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching Groups...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://approvals.wizag.biz/api/v1/groups", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        String message = jsonObject.getString("message");
                        Toasty.success(getApplicationContext(), "User Groups Loaded Successfully", Toasty.LENGTH_SHORT).show();
                        docs = jsonObject.getJSONArray("groups");
                        approval_model.clear();
                        for (int k = 0; k < docs.length(); k++) {

                            approvals = new ApprovalModel();
                            JSONObject docsObject = docs.getJSONObject(k);


                            grpID = docsObject.getString("GroupID");
                            grpName = docsObject.getString("GroupName");


                            boolean isExist = isExist(docsObject.getString("GroupName"));

                            if (grpID.equalsIgnoreCase("1")) {
                                grpName = "Accounting and Finance";
                            } else if (grpID.equalsIgnoreCase("2")) {
                                grpName = "ICT Dept.";
                            } else if (grpID.equalsIgnoreCase("3")) {
                                grpName = "HR";
                            } else if (grpID.equalsIgnoreCase("4")) {
                                grpName = "Procurement";
                            } else if (grpID.equalsIgnoreCase("5")) {
                                grpName = "Marketing";
                            } else if (grpID.equalsIgnoreCase("6")) {
                                grpName = "Manager";
                            }


                            approvals.setGroupName(grpName);
                            approvals.setGroupID(grpID);
                            approval_model.add(approvals);


                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                approval_adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
                pDialog.dismiss();
                Toasty.error(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();

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


    private void postWorkflow() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String grp_id = user.get("GroupID");
        final String agent_id = user.get("AgentID");

        /*get first element of array*/


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, workflow,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //check status if success login user
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String success = jsonObject.getString("Success");
                            String message = jsonObject.getString("message");

                            if (success == "true") {
                                Toasty.success(Activity_Approval.this, message, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), Activity_Admin_Docs.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                finish();

                            } else {
                                Toasty.error(Activity_Approval.this, "Error in creating workflow", Toast.LENGTH_SHORT).show();

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
                Toasty.error(Activity_Approval.this, "An Error Occurred", Toast.LENGTH_SHORT).show();


                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("DocType", docType);
                params.put("SequenceID", array.toString());
                params.put("GroupID", grp_id);
                params.put("LastGroup", "0");
                params.put("LastAgent", "0");
                params.put("NextGroup", "");

                Log.d("SequenceIDString", array.toString());
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




