package com.example.intern12.easymeasurements;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.intern12.easymeasurements.data.AdapterMasurari;
import com.example.intern12.easymeasurements.data.ClickListener;
import com.example.intern12.easymeasurements.data.Masurari;
import com.example.intern12.easymeasurements.data.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class ListaMasurariFragment extends Fragment implements DetailActivity.EditMeasureList {
    RecyclerView recyclerView;
    AdapterMasurari mAdapter;
    ArrayList<Masurari> myMeasureList = new ArrayList<Masurari>();
    DatabaseReference mDatabase;
    Intent intent;
    CheckBox checkBox;
    TextView textView;
    Double totalSize = 0.0;
    //adapter created before downloading measurements
    private OnFragmentInteractionListener mListener;



    public ListaMasurariFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lista_masurari, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        mAdapter = new AdapterMasurari(myMeasureList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkbox_list);
        textView = (TextView) rootView.findViewById(R.id.suprafata_totala_recycler_view);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Masurari masurari = myMeasureList.get(position);
//                mAdapter.
                if (masurari.isSelected() == false) {
                    totalSize += masurari.getSize();
                } else {

                    totalSize -= masurari.getSize();
                }
                textView.setText(String.format("%.2f", totalSize) + " ha");
            }

            @Override
            public void onLongClick(View view, int position) {
                Masurari masurari = myMeasureList.get(position);
                intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("numeZona", masurari.getName());
                intent.putExtra("numeCultura", masurari.getCultura());
                intent.putExtra("suprafata", masurari.getSize());
                intent.putExtra("numeProprietar", masurari.getProprietar());
                intent.putExtra("key", masurari.getDataBaseKey());
                intent.putExtra("startPointLat",masurari.getStartPointLat());
                intent.putExtra("startPointLng", masurari.getStartPointLng());
                intent.putExtra("lastUpdate", masurari.getLastUpdated());
                startActivity(intent);
            }

        }));

        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

    }

    public void setMyMeasureList(ArrayList<Masurari> myMeasureList) {
        this.myMeasureList = myMeasureList;
    }

    public void changeObjectArrayList(int pozitie, Masurari m) {
        myMeasureList.set(pozitie, m);
    }
}
