package com.wizag.mobileapprovals.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import com.wizag.mobileapprovals.utils.SwipeAndDragHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        setTitle("Set Approvals");


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


        loadDocuments();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Dialog to confirm submission to db*/
                JSONArray array = new JSONArray();
                for (int i = 0; i < approval_model.size(); i++) {
                    JSONObject obj = new JSONObject();
                    final ApprovalModel approvalModel = approval_model.get(i);

                    String data;
                    myList = new ArrayList<String>();
                    myList.add(approvalModel.getGroupID());

                    try {
                        obj.put("id", approvalModel.getGroupID());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(obj);
                    Log.d("approvals_details", array.toString());


//                    myList.add(approvalModel.getGroupName());
//                    JSONArray myListArray = new JSONArray(myList);


                }


            }
        });
    }

    private void loadDocuments() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching Groups...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.myjson.com/bins/n3fow", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        String message = jsonObject.getString("message");
                        docs = jsonObject.getJSONArray("groups");

                        for (int k = 0; k < docs.length(); k++) {
                            approvals = new ApprovalModel();
                            JSONObject docsObject = docs.getJSONObject(k);


                            grpID = docsObject.getString("GroupID");
                            grpName = docsObject.getString("GroupName");


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


                            if (approval_model.equals(grpID)) {


                            } else {
                                approval_model.add(approvals);


                            }

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




