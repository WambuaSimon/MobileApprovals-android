package com.wizag.mobileapprovals.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.models.AdminDocsModel;
import com.wizag.mobileapprovals.models.AdminDocuments;
import com.wizag.mobileapprovals.ui.Activity_Approval;

import java.util.List;

public class AdminDocsAdapter extends RecyclerView.Adapter<AdminDocsAdapter.MyViewHolder> {

    private List<AdminDocuments> docsData;
    Context context;
    public AdminDocsAdapterListener onClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView doc_type;
        TextView doc_date;
        TextView doc_name;
        TextView account_name;
        TextView exl_amt;
        TextView incl_amt;
        TextView vat;
        TextView status;
        FloatingActionButton fab;
        TextView statusDesc;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.doc_type = itemView.findViewById(R.id.doc_type);
            this.doc_date = itemView.findViewById(R.id.doc_date);
            this.doc_name = itemView.findViewById(R.id.doc_name);
            this.account_name = itemView.findViewById(R.id.account_name);
            this.exl_amt = itemView.findViewById(R.id.exl_amt);
            this.incl_amt = itemView.findViewById(R.id.incl_amt);
            this.vat = itemView.findViewById(R.id.vat);
            this.status = itemView.findViewById(R.id.status);
            this.fab = itemView.findViewById(R.id.add_doc);

        }
    }

    public AdminDocsAdapter(List<AdminDocuments> data, Context context, AdminDocsAdapterListener listener) {
        this.docsData = data;
        this.context = context;
        this.onClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_docs_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final TextView doc_type = holder.doc_type;
        TextView doc_date = holder.doc_date;
        TextView doc_name = holder.doc_name;
        TextView account_name = holder.account_name;
        TextView exl_amt = holder.exl_amt;
        TextView incl_amt = holder.incl_amt;
        TextView vat = holder.vat;
        final TextView status = holder.status;
        FloatingActionButton fab = holder.fab;


        doc_type.setText(docsData.get(listPosition).getDocType());
        doc_date.setText(docsData.get(listPosition).getDocDate());
        doc_name.setText(docsData.get(listPosition).getDocName());
        account_name.setText(docsData.get(listPosition).getAccountName());

        exl_amt.setText(docsData.get(listPosition).getExclAmt());
        incl_amt.setText(docsData.get(listPosition).getInclAmt());
        vat.setText(docsData.get(listPosition).getVATAmt());
        status.setText(docsData.get(listPosition).getAppStatus());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.fabOnClick(v, listPosition);

                docsData.remove(listPosition);
                notifyDataSetChanged();


            }
        });


    }

    @Override
    public int getItemCount() {
        return docsData.size();
    }

    public interface AdminDocsAdapterListener {

        void fabOnClick(View v, int position);


    }

}
