package com.thesis.grabtrash.maybefinal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        DatabaseReference info;

        info = FirebaseDatabase.getInstance().getReference().child("Info");

        info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                TextView plate= (TextView) findViewById(R.id.plate);
                TextView driver = (TextView) findViewById(R.id.driver);
                TextView helper = (TextView) findViewById(R.id.helper_wan);
                TextView helperr = (TextView) findViewById(R.id.helper_tow);
                TextView helperrr= (TextView) findViewById(R.id.helper_tree);

                String Plate = dataSnapshot.child("Plate").getValue(String.class );
                String Driver = dataSnapshot.child("Driver").getValue(String.class );
                String Helper = dataSnapshot.child("helper").getValue(String.class );
                String Helperr = dataSnapshot.child("helperr").getValue(String.class );
                String Helperrr = dataSnapshot.child("helperrr").getValue(String.class );

                plate.setText(Plate);
                driver.setText(Driver);
                helper.setText(Helper);
                helperr.setText(Helperr);
                helperrr.setText(Helperrr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
