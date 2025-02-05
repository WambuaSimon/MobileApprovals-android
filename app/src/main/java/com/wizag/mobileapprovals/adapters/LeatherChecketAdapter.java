//package com.wizag.mobileapprovals.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.TextView;
//
//import com.wizag.mobileapprovals.R;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LeatherChecketAdapter extends ArrayAdapter<LeatherChecketModel> {
//
//    Context context;
//    int resource, textViewResourceId;
//    List<LeatherChecketModel> items, tempItems, suggestions;
//
//    public LeatherChecketAdapter(Context context, int resource, int textViewResourceId, List<LeatherChecketModel> items) {
//        super(context, resource, textViewResourceId, items);
//        this.context = context;
//        this.resource = resource;
//        this.textViewResourceId = textViewResourceId;
//        this.items = items;
//        tempItems = new ArrayList<LeatherChecketModel>(items); // this makes the difference.
//        suggestions = new ArrayList<LeatherChecketModel>();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.test_layout, parent, false);
//        }
//        LeatherChecketModel leatherChecketModel = items.get(position);
//        if (leatherChecketModel != null) {
//            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
//            if (lblName != null)
//                lblName.setText(leatherChecketModel.getDescription());
//        }
//        return view;
//    }
//
//    @Override
//    public Filter getFilter() {
//        return nameFilter;
//    }
//
//    /**
//     * Custom Filter implementation for custom suggestions we provide.
//     */
//    Filter nameFilter = new Filter() {
//        @Override
//        public CharSequence convertResultToString(Object resultValue) {
//            String str = ((LeatherChecketModel) resultValue).getDescription();
//            return str;
//        }
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            if (constraint != null) {
//                suggestions.clear();
//                for (LeatherChecketModel leatherChecketModel : tempItems) {
//                    if (leatherChecketModel.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        suggestions.add(leatherChecketModel);
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = suggestions;
//                filterResults.count = suggestions.size();
//                return filterResults;
//            } else {
//                return new FilterResults();
//            }
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            List<LeatherChecketModel> filterList = (ArrayList<LeatherChecketModel>) results.values;
//            if (results != null && results.count > 0) {
//                clear();
//                for (LeatherChecketModel leatherChecketModel : filterList) {
//                    add(leatherChecketModel);
//                    notifyDataSetChanged();
//                }
//            }
//        }
//    };
//}
