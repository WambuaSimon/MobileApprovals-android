package com.wizag.mobileapprovals.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wizag.mobileapprovals.R;
import com.wizag.mobileapprovals.models.ApprovalModel;
import com.wizag.mobileapprovals.utils.OnItemClickListener;
import com.wizag.mobileapprovals.utils.SwipeAndDragHelper;

import java.util.List;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.MyViewHolder> implements
        SwipeAndDragHelper.ActionCompletionContract {

    private List<ApprovalModel> docsData;
    Context context;
    private ItemTouchHelper touchHelper;

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        ApprovalModel targetUser = docsData.get(oldPosition);
        ApprovalModel user = new ApprovalModel(targetUser);
        docsData.remove(oldPosition);
        docsData.add(newPosition, user);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onViewSwiped(int position) {
        docsData.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {

        this.touchHelper = touchHelper;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview_reorder;
        TextView group;
        CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageview_reorder = itemView.findViewById(R.id.imageview_reorder);
            this.group = itemView.findViewById(R.id.group);
            this.card = itemView.findViewById(R.id.card);


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
        int itemViewType = getItemViewType(listPosition);
        final TextView group = holder.group;

        final CardView card = holder.card;
        final ImageView imageview_reorder = holder.imageview_reorder;
        imageview_reorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(holder);
                }
                return false;
            }
        });
        group.setText(docsData.get(listPosition).getGroupName());

        /*implement imageview_reorder click*/


    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return docsData.size();
    }


}
