package com.example.intern12.easymeasurements.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intern12 on 15.05.2017.
 */

public class MeasurementsList { // fa conexiunile cu firebase aici !


    //not used at all
    static ArrayList<Masurari> masurari;
    private static DatabaseReference mDatabase;

    public static List<Masurari> getMeasurements() {


        masurari = new ArrayList<>();
      //  masurari = downloadDatabase();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                     masurari.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Masurari masurare = postSnapshot.getValue(Masurari.class);
                        masurari.add(masurare);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

                //
//        masurari.add(new Masurari("Cizma",4.3,"Grau"));//a data to be implemented!
//        masurari.add(new Masurari("Padure",1.3,"Porumb"));
        return masurari;
    }



}
