package com.thesis.grabtrash.maybefinal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduleActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        DatabaseReference sched;

        sched = FirebaseDatabase.getInstance().getReference();
        sched.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                TextView biosched= (TextView) findViewById(R.id.txt_bioSched);
                TextView biotime = (TextView) findViewById(R.id.txt_time_bio);
                TextView nonbiosched = (TextView) findViewById(R.id.txt_nonbioSched);
                TextView nonbiotime = (TextView) findViewById(R.id.txt_time_nonbio);

                String valuebio = dataSnapshot.child("BioSched").getValue(String.class );
                String valuebiotime = dataSnapshot.child("BioTime").getValue(String.class );

                String valuenonbio = dataSnapshot.child("nonBioSched").getValue(String.class );
                String valuenonbiotime = dataSnapshot.child("nonBioTime").getValue(String.class );

                biosched.setText(valuebio);
                biotime.setText(valuebiotime);

                nonbiosched.setText(valuenonbio);
                nonbiotime.setText(valuenonbiotime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
