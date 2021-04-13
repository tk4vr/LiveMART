package com.example.oop_project.Customer;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CustomerShopList extends Fragment{
    double cutDist=1000;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String catname,pname;
    RecyclerView recview1;
    Shopadapter adapter;
    Button filter;
    EditText cutoffDist;
    public CustomerShopList() {

    }

    public CustomerShopList(String catname, String pname) {

        this.catname = catname;
        this.pname = pname;
    }

    public static CustomerShopList newInstance(String param1, String param2) {
        CustomerShopList fragment = new CustomerShopList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1).trim();
            mParam2 = getArguments().getString(ARG_PARAM2).trim();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shop_filter, container, false);

        filter = view.findViewById(R.id.button3);

        recview1 = (RecyclerView) view.findViewById(R.id.rcycler);
        recview1.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i("Mine dis", "pname:" + pname);
        Log.i("Mine dis", "catname:" + catname);

        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        Query query = rootref.child("Quantity").child(catname).child(pname).child("Retailer");
        //pname = Fruits

        FirebaseRecyclerOptions<model_shop> options =
                new FirebaseRecyclerOptions.Builder<model_shop>()
                        .setQuery(query, model_shop.class)
                        .build();

        adapter = new Shopadapter(options,getContext(),  catname , pname,cutDist);
        recview1.setAdapter(adapter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cutoffDist = view.findViewById(R.id.editTextNumberDecimal6);
                cutDist = Double.parseDouble(String.valueOf(cutoffDist.getText()));
                Log.i("DistCheck:", String.valueOf(cutDist));
                //recview1.invalidate();
                adapter = new Shopadapter(options,getContext(),  catname , pname,cutDist);
                recview1.setAdapter(adapter);

            }
        });

        return view;
    }

    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new CustomerSubCategories()).addToBackStack(null).commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}