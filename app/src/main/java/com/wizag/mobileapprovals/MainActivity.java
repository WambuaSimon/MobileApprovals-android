package com.wizag.mobileapprovals;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.wizag.mobileapprovals.adapters.PeopleAdapter;
import com.wizag.mobileapprovals.models.AdminDocsModel;
import com.wizag.mobileapprovals.models.People;
import com.wizag.mobileapprovals.utils.MySingleton;
import com.wizag.mobileapprovals.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView txtSearch;
    List<People> mList;
    PeopleAdapter adapter;
    private People selectedPerson;
    List<People> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = retrievePeople();
        txtSearch = (AutoCompleteTextView) findViewById(R.id.auto_name);
        adapter = new PeopleAdapter(this, R.layout.activity_main, R.id.lbl_name, mList);
        txtSearch.setAdapter(adapter);
        txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //this is the way to find selected object/item
                selectedPerson = (People) adapterView.getItemAtPosition(pos);
                String name = selectedPerson.getName();
                String name_id = String.valueOf(selectedPerson.getId());


                Toast.makeText(getApplicationContext(), name + "\n" + name_id, Toast.LENGTH_SHORT).show();

            }
        });
    }
//
//    private List<People> retrievePeople() {
//
//        List<People> list = new ArrayList<People>();
//        list.add(new People("James", 1));
//        list.add(new People("Jason", 2));
//        list.add(new People("Ethan", 3));
//        list.add(new People("Sherlock", 4));
//        list.add(new People("David", 5));
//        list.add(new People("Bryan", 6));
//        list.add(new People("Arjen", 7));
//        list.add(new People("Van", 8));
//        list.add(new People("Zinedine", 9));
//        list.add(new People("Luis", 10));
//        list.add(new People("John", 11));
//        return list;
//    }


    private List<People> retrievePeople() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://approvals.wizag.biz/api/v1/documents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int m = 0; m < jsonArray.length(); m++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(m);
                        String name = jsonObject.getString("itemNo");
                        String name_id = jsonObject.getString("description");
                        list = new ArrayList<People>();
//                        list.add(new People(name, name_id));
                        list.add(new People("James", "1"));
                        list.add(new People("Jason", "2"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//
                Toasty.error(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        });


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

        return list;
    }


}