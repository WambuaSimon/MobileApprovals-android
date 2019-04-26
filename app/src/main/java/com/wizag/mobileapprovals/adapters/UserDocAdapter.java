package com.wizag.mobileapprovals.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.models.UserDocsModel;
import com.wizag.mobileapprovals.ui.Activity_Approval;

import java.util.List;

public class UserDocAdapter extends RecyclerView.Adapter<UserDocAdapter.MyViewHolder> {

    private List<UserDocsModel> docsData;
    Context context;
    public UsersAdapterListener onClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView doc_type;
        TextView doc_date;
        TextView doc_name;
        TextView account_name;
        TextView exl_amt;
        TextView incl_amt;
        TextView vat;

        Button reject;
        Button approve;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.doc_type = itemView.findViewById(R.id.doc_type);
            this.doc_date = itemView.findViewById(R.id.doc_date);
            this.doc_name = itemView.findViewById(R.id.doc_name);
            this.account_name = itemView.findViewById(R.id.account_name);
            this.exl_amt = itemView.findViewById(R.id.exl_amt);
            this.incl_amt = itemView.findViewById(R.id.incl_amt);
            this.vat = itemView.findViewById(R.id.vat);

            this.reject = itemView.findViewById(R.id.reject);
            this.approve = itemView.findViewById(R.id.approve);
        }
    }

    public UserDocAdapter(List<UserDocsModel> data, Context context, UsersAdapterListener listener) {
        this.docsData = data;
        this.context = context;
        this.onClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_docs_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView doc_type = holder.doc_type;
        TextView doc_date = holder.doc_date;
        TextView doc_name = holder.doc_name;
        TextView account_name = holder.account_name;
        TextView exl_amt = holder.exl_amt;
        TextView incl_amt = holder.incl_amt;
        TextView vat = holder.vat;

        Button reject = holder.reject;
        Button approve = holder.approve;


        doc_type.setText(docsData.get(listPosition).getDocType());
        doc_date.setText(docsData.get(listPosition).getDocDate());
        doc_name.setText(docsData.get(listPosition).getDocName());
        account_name.setText(docsData.get(listPosition).getAccountName());

        exl_amt.setText(docsData.get(listPosition).getExclAmt());
        incl_amt.setText(docsData.get(listPosition).getInclAmt());
        vat.setText(docsData.get(listPosition).getVATAmt());

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.approveOnClick(v, listPosition);

//
//                final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
//                        context);
//
//
//                alertDialog2.setTitle("Confirm Approval");
//
//
//                alertDialog2.setMessage("Are you sure you want to approve this document?");
//
//
//                alertDialog2.setPositiveButton("YES",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Write your code here to execute after dialog
//                                docsData.remove(listPosition);
//                                notifyDataSetChanged();
//
//                            }
//                        });
//
//                alertDialog2.setNegativeButton("NO",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Write your code here to execute after dialog
//
//                                dialog.cancel();
//                            }
//                        });
//
//
//                alertDialog2.show();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.rejectOnClick(v, listPosition);

//                final AlertDialog.Builder alert = new AlertDialog.Builder(
//                        context);
//
//                final EditText edittext = new EditText(context);
//                edittext.setPadding(7,7,7,7);
//
////                alert.setMessage("Enter Your Message");
//                alert.setTitle("Reason for Rejecting");
//
//                alert.setView(edittext);
//
//                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //What ever you want to do with the value
//                        Editable YouEditTextValue = edittext.getText();
//                        //OR
//
//                    }
//                });
//
//                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // what ever you want to do with No option.
//                    }
//                });
//
//                alert.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return docsData.size();
    }


    public interface UsersAdapterListener {

        void approveOnClick(View v, int position);

        void rejectOnClick(View v, int position);

    }
}
