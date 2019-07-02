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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.adapters.AdminDocsAdapter;
import com.wizag.mobileapprovals.models.AdminDocsModel;
import com.wizag.mobileapprovals.models.AdminDocuments;
import com.wizag.mobileapprovals.network.APIClient;
import com.wizag.mobileapprovals.network.APiInterface;
import com.wizag.mobileapprovals.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Admin_Docs extends AppCompatActivity {
    RecyclerView recyclerView;
    AdminDocsAdapter adminDocsAdapter;
    List<AdminDocuments> docsModelList;

    FloatingActionButton add_doc;
    SessionManager sessionManager;
    String workflow = "";
    LinearLayout parent_layout;
    String AppStatus;
    TextView empty_view;
    APiInterface aPiInterface;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_docs);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        aPiInterface = APIClient.getClient(this).create(APiInterface.class);
        empty_view = findViewById(R.id.empty_view);
        parent_layout = findViewById(R.id.parent_layout);
//        add_doc = findViewById(R.id.add_doc);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        docsModelList = new ArrayList<>();
        loadDocuments();
        //initializing adapter
        adminDocsAdapter = new AdminDocsAdapter(docsModelList, this, new AdminDocsAdapter.AdminDocsAdapterListener() {
            @Override
            public void fabOnClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Activity_Approval.class);
                AdminDocuments adminDocsModel = docsModelList.get(position);

                String docType = adminDocsModel.getDocType();
                String appStatus = adminDocsModel.getAppStatus();

                intent.putExtra("DocType", docType);
                intent.putExtra("AppStatus", appStatus);

//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        });


        recyclerView.setAdapter(adminDocsAdapter);
        adminDocsAdapter.notifyDataSetChanged();


        /*ADD DOC APPROVAL W/F*/
        sessionManager = new SessionManager(getApplicationContext());

    }

    void loadDocuments() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching Documents...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<AdminDocsModel> adminDocsModelCall = aPiInterface.getAdminDocs();
        adminDocsModelCall.enqueue(new Callback<AdminDocsModel>() {
            @Override
            public void onResponse(Call<AdminDocsModel> call, Response<AdminDocsModel> response) {
                pDialog.dismiss();
                AdminDocsModel adminDocsModel = response.body();
                if (adminDocsModel.isStatus() == true) {
                    docsModelList.clear();

                    String message = adminDocsModel.getMessage();
                    Toasty.success(getApplicationContext(), message, Toasty.LENGTH_SHORT);
                    List<AdminDocuments> adminDocumentsList = adminDocsModel.getAdminDocumentsList();
                    AdminDocuments adminDocumentsModel = null;
                    for (int i = 0; i < adminDocumentsList.size(); i++) {
                        adminDocumentsModel = new AdminDocuments();

                        String docType = adminDocumentsList.get(i).getDocType();
                        String DocName = adminDocumentsList.get(i).getDocName();
                        String AccountName = adminDocumentsList.get(i).getAccountName();
                        String DocDate = adminDocumentsList.get(i).getDocDate();
                        String ExclAmt = adminDocumentsList.get(i).getExclAmt();
                        String VATAmt = adminDocumentsList.get(i).getVATAmt();
                        String InclAmt = adminDocumentsList.get(i).getInclAmt();
                        AppStatus = adminDocumentsList.get(i).getAppStatus();

                        if (AppStatus != null && AppStatus.equalsIgnoreCase("0")) {
//
                            adminDocumentsModel.setDocType(docType);
                            adminDocumentsModel.setDocName(DocName);
                            adminDocumentsModel.setAccountName(AccountName);
                            adminDocumentsModel.setDocDate(DocDate);
                            adminDocumentsModel.setExclAmt("Excl:\t" + ExclAmt);
                            adminDocumentsModel.setVATAmt("Vat:\t" + VATAmt);
                            adminDocumentsModel.setInclAmt("Incl:\t" + InclAmt);
                            adminDocumentsModel.setAppStatus(AppStatus);
                            docsModelList.add(adminDocumentsModel);

                        }

                    }


                } else {
                    Toasty.error(getApplicationContext(), "An Error Occured while loading data", Toasty.LENGTH_LONG).show();

                }
                adminDocsAdapter.notifyDataSetChanged();
                toggleEmptyNotes();
            }

            @Override
            public void onFailure(Call<AdminDocsModel> call, Throwable t) {
                pDialog.dismiss();
                Toasty.error(getApplicationContext(), "Could not load data", Toasty.LENGTH_LONG).show();
            }
        });
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

    private void toggleEmptyNotes() {
        if (docsModelList.size() > 0) {
            empty_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.VISIBLE);
        }
    }

}
