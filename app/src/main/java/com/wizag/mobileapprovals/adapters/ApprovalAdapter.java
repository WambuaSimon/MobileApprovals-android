package com.wizag.mobileapprovals.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.models.ApprovalModel;

import java.util.List;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.MyViewHolder> {

    private List<ApprovalModel> docsData;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox checkbox;
        TextView group;
        CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.checkbox = itemView.findViewById(R.id.checkbox);
            this.group = itemView.findViewById(R.id.group);
            this.card = itemView.findViewById(R.id.card);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
//            int adapterPosition = getAdapterPosition();
//            if ()

        }
    }

    public ApprovalAdapter(List<ApprovalModel> data, Context context) {
        this.docsData = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.approval_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final TextView group = holder.group;
        final CheckBox checkbox = holder.checkbox;
        final CardView card = holder.card;
        holder.checkbox.setOnCheckedChangeListener(null);
        final ApprovalModel approvalModel = docsData.get(listPosition);
        if (approvalModel.isSelected()) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                approvalModel.setSelected(isChecked);
//                Toast.makeText(ApprovalAdapter.this.context, "Data\t" + group.getText().toString(), Toast.LENGTH_SHORT).show();


                /*hide checkbox and add it to arraylist*/
//                card.setVisibility(View.GONE);

            }
        });


        group.setText(docsData.get(listPosition).getGroupName());

        /*implement checkbox click*/


    }

    @Override
    public int getItemCount() {
        return docsData.size();
    }


}
