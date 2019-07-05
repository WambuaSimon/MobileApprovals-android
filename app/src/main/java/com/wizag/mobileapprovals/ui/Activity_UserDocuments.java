package com.wizag.mobileapprovals.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.wizag.mobileapprovals.models.Approvers;
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
    String AppStatus;
    String reason;
    TextView empty_view;
    String groupID, agentID;
    String id_to_update;
    ArrayList<Approvers> approverList;
    String approverGrpId;
    String appStatus;
    String DocId;
    String next, curr;
    String lastGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_documents);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        sessionManager = new SessionManager(getApplicationContext());
        context = this;
        approverList = new ArrayList<>();

        empty_view = findViewById(R.id.empty_view);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        if (!user.isEmpty()) {
            groupID = user.get("GroupID");
            agentID = user.get("AgentID");
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        docsModelList = new ArrayList<>();

        if (isNetworkConnected()) {
            loadDocuments();

        } else {
            Toasty.error(getApplicationContext(), "Ensure you have internet connection", Toasty.LENGTH_LONG).show();

        }


        //initializing adapter
        userDocsAdapter = new UserDocAdapter(docsModelList, this, new UserDocAdapter.UsersAdapterListener() {
            @Override
            public void itemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Activity_UserDocumentDetails.class);

                id_to_update = docsModelList.get(position).DocType;

                String doc_date = docsModelList.get(position).getDocDate();
                String doc_name = docsModelList.get(position).getDocName();
                String account_name = docsModelList.get(position).getAccountName();
                String exl_amt = docsModelList.get(position).getExclAmt();
                String incl_amt = docsModelList.get(position).getInclAmt();
                String vat = docsModelList.get(position).getVATAmt();

                String price = docsModelList.get(position).getPrice();
                String quantity = docsModelList.get(position).getQuantity();

                Double totalPrice = Double.parseDouble(docsModelList.get(position).getPrice()) * Double.parseDouble(docsModelList.get(position).getQuantity());
                String total = String.valueOf(totalPrice);

                intent.putExtra("DocType", id_to_update);
                intent.putExtra("doc_date", doc_date);
                intent.putExtra("doc_name", doc_name);
                intent.putExtra("account_name", account_name);
                intent.putExtra("exl_amt", exl_amt);
                intent.putExtra("incl_amt", incl_amt);
                intent.putExtra("vat", vat);
                intent.putExtra("price", price);
                intent.putExtra("quantity", quantity);
                intent.putExtra("total", total);
                intent.putExtra("AppStatus", appStatus);

                intent.putExtra("AgentID", agentID);
                intent.putExtra("LastGroup", groupID);
                intent.putExtra("LastAgent", agentID);
                intent.putExtra("NextGroup", next);

//                Toast.makeText(context, appStatus, Toast.LENGTH_SHORT).show();

                startActivity(intent);


            }


        });


        recyclerView.setAdapter(userDocsAdapter);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
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


                                String nxtGrpId = docsObject.getString("NextGroup");
                                lastGrp = docsObject.getString("LastGroup");
//                                Toast.makeText(context, nxtGrpId, Toast.LENGTH_SHORT).show();

                                JSONArray sequenceID = docsObject.getJSONArray("SequenceID");
                                for (int p = 0; p < sequenceID.length(); p++) {


                                    String seq_id = sequenceID.get(0).toString();
                                    String finalVal = sequenceID.get(p-1).toString();
                                    Log.d("SequenceIDString", finalVal);
                                    Toast.makeText(context, finalVal, Toast.LENGTH_SHORT).show();

                                    Approvers approvers = new Approvers();
                                    approvers.setSequenceID(seq_id);
                                    approverList.add(approvers);

                                    for (int z = 0; z < approverList.size() - 1; z++) {
                                        String grpToApprove = approverList.get(0).getSequenceID();


                                        int seqIdPosition = approverList.size() - 1;
                                        if (seqIdPosition != Integer.parseInt(groupID)) {
                                            appStatus = "2";
                                        } else if (Integer.parseInt(groupID) == Integer.parseInt(finalVal)) {
                                            appStatus = "1";
                                        }

                                    }

                                }


                                JSONObject singleDoc = null;
                                try {

                                    singleDoc = docsObject.getJSONObject("document");
                                    if (Integer.parseInt(groupID) == Integer.parseInt(nxtGrpId)) {

                                        DocId = singleDoc.getString("DocId");
                                        DocType = singleDoc.getString("DocType");
                                        String DocName = singleDoc.getString("DocName");
                                        String AccountName = singleDoc.getString("AccountName");
                                        String DocDate = singleDoc.getString("DocDate");
                                        String price = singleDoc.getString("Price");
                                        String quantity = singleDoc.getString("Quantity");
                                        String exc = singleDoc.getString("ExclAmt");
                                        String incl = singleDoc.getString("InclAmt");
                                        String vat = singleDoc.getString("VATAmt");


                                        model_docs.setDocType(DocType);
                                        model_docs.setDocName(DocName);
                                        model_docs.setAccountName(AccountName);
                                        model_docs.setDocDate(DocDate);
                                        model_docs.setPrice(price);
                                        model_docs.setQuantity(quantity);
                                        model_docs.setExclAmt(exc);
                                        model_docs.setInclAmt(incl);
                                        model_docs.setVATAmt(vat);


                                        Log.d("DocType", DocType);

                                        if (docsModelList.contains(DocId)) {


                                        } else {

                                            docsModelList.add(model_docs);
                                        }

                                    } else {
                                        Toasty.warning(getApplicationContext(), "No Documents to approve at this time", Toasty.LENGTH_LONG).show();

                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }


                    }


                } catch (
                        JSONException e) {
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
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();

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


        MySingleton.getInstance(this).

                addToRequestQueue(stringRequest);


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
        getMenuInflater().inflate(R.menu.user_menu, menu);
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

        } else if (id == R.id.history) {
            /*open history activity*/

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


    private void toggleEmptyNotes() {
        if (docsModelList.size() > 0) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
    }

}
