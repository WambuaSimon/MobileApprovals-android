package com.wizag.mobileapprovals.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.models.UserDocsModel;

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
        TextView quantity;
        TextView price;
        TextView total;
        CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.doc_type = itemView.findViewById(R.id.doc_type);
            this.doc_date = itemView.findViewById(R.id.doc_date);
            this.doc_name = itemView.findViewById(R.id.doc_name);
            this.account_name = itemView.findViewById(R.id.account_name);
            this.quantity = itemView.findViewById(R.id.quantity);
            this.price = itemView.findViewById(R.id.price);
            this.total = itemView.findViewById(R.id.total);
            this.card = itemView.findViewById(R.id.card);


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
        TextView quantity = holder.quantity;
        TextView price = holder.price;
        TextView total = holder.total;
        CardView card = holder.card;

        doc_type.setText(docsData.get(listPosition).getDocType());
        doc_date.setText(docsData.get(listPosition).getDocDate());
        doc_name.setText(docsData.get(listPosition).getDocName());
        account_name.setText(docsData.get(listPosition).getAccountName());

        quantity.setText("Quantity: " +docsData.get(listPosition).getQuantity());
        price.setText("Price: " +docsData.get(listPosition).getPrice());

        Double qty = Double.valueOf(docsData.get(listPosition).getQuantity());
        Double prc = Double.valueOf(docsData.get(listPosition).getPrice());


        Double totalPrice = qty * prc;

        total.setText("Total: " +totalPrice);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.itemClick(v, listPosition);
            }
        });



    }

    @Override
    public int getItemCount() {
        return docsData.size();
    }


    public interface UsersAdapterListener {

        void itemClick(View v, int position);

    }
}
