package com.thesis.grabtrash.maybefinal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final LatLng PORTAL = new LatLng(10.353066, 123.913944);
    private static final LatLng RH = new LatLng(10.355352, 123.910085);
    private static final LatLng SAFAD = new LatLng(10.352764, 123.910513);

    private Marker mPortal;
    private Marker mRh;
    private Marker mSafad;
    private Marker mTruck;
    private ImageView info;

    DatabaseReference stat;

    GoogleMap map;
    private static final String TAG = "MainActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        stat= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        stat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                int status = dataSnapshot.child("Status").getValue(int.class);
                String lat = dataSnapshot.child("Latitude").getValue(String.class);
                String lon = dataSnapshot.child("Longitude").getValue(String.class);

                Float lats = Float.parseFloat(lat);
                Float lons = Float.parseFloat(lon);

                LatLng Truck = new LatLng(lats, lons);


                final int Safads = dataSnapshot.child("SAFAD").getValue(int.class);
                final int Portals = dataSnapshot.child("PORTAL").getValue(int.class);
                final int RHs = dataSnapshot.child("RH").getValue(int.class);

                if (mTruck != null){
                    mTruck.remove();
                }

                if (status == 0){

                    mTruck = map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .position(Truck)
                            .snippet("Full")
                            .title("Truck"));

                }
                else {

                    mTruck = map.addMarker(new MarkerOptions()
                            .position(Truck)
                            .title("Truck")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet("Not Full"));

                }

                Location loctruck = new Location("");
                loctruck.setLatitude(lats);
                loctruck.setLongitude(lons);

                Location locsafad = new Location("");
                locsafad.setLatitude(10.352764);
                locsafad.setLongitude(123.910513);


                Location locrh = new Location("");
                locrh.setLatitude(10.355352);
                locrh.setLongitude(123.910085);


                Location locportal= new Location("");
                locportal.setLatitude(10.353066);
                locportal.setLongitude(123.913944);


                float safaddistance = loctruck.distanceTo(locsafad);
                float portaldistance = loctruck.distanceTo(locportal);
                float rhdistance = loctruck.distanceTo(locrh);

                Log.i(TAG, "onDataChange:Safad " +safaddistance);
                Log.i(TAG, "onDataChange:Portal " +portaldistance);
                Log.i(TAG, "onDataChange:Rh " +rhdistance);




                //100 meters
                if (  115>= portaldistance && portaldistance >= 95 && Portals == 0 ){
                    notifportal();
                }

                if (  115>= safaddistance && safaddistance >= 95 && Safads == 0 ){
                    notifsafad();
                }

                if (  115>= rhdistance && rhdistance >= 95 && RHs == 0 ){
                    notifrh();
                }

                //near

                if (  40>= portaldistance && portaldistance >= 15 && Portals == 0 ){
                    notifportalnear();
                }

                if (  40>= safaddistance && safaddistance >= 15 && Safads == 0 ){
                    notifsafadnear();
                }

                if (  40>= rhdistance && rhdistance >= 15 && RHs == 0 ){
                    notifrhnear();
                }

                //logo

                if (mPortal != null){
                    mPortal.remove();
                }

                if (mRh != null){
                    mRh.remove();
                }

                if (mSafad != null){
                    mSafad.remove();
                }

                if (Portals == 1){

                    mPortal = map.addMarker(new MarkerOptions()
                            .position(PORTAL)
                            .snippet("Garbage Pickup Point A")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.portalafter))
                            .title("Portal"));
                    mPortal.setTag(0);

                }

                else{
                    mPortal = map.addMarker(new MarkerOptions()
                            .position(PORTAL)
                            .snippet("Garbage Pickup Point A")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.portalbefore))
                            .title("Portal"));
                    mPortal.setTag(0);
                }

                if (   RHs == 1 ){

                    mRh = map.addMarker(new MarkerOptions()
                            .position(RH)
                            .snippet("Garbage Pickup Point B")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.rhafter))
                            .title("RH"));
                    mRh.setTag(0);
                }

                else{
                    mRh = map.addMarker(new MarkerOptions()
                            .position(RH)
                            .snippet("Garbage Pickup Point B")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.rhbefore))
                            .title("RH"));
                    mRh.setTag(0);
                }

                if ( Safads == 1 ){
                    mSafad= map.addMarker(new MarkerOptions()
                            .position(SAFAD)
                            .snippet("Garbage Pickup Point C")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.safadafter))
                            .title("SAFAD"));
                    mSafad.setTag(0);

                }

                else {
                    mSafad= map.addMarker(new MarkerOptions()
                            .position(SAFAD)
                            .snippet("Garbage Pickup Point C")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.safadbefore))
                            .title("SAFAD"));
                    mSafad.setTag(0);

                }

                //leaving
                if (  15>= portaldistance && portaldistance >= 5 && Portals == 1 ){
                    notifportalleave();
                }

                if (  15>= safaddistance && safaddistance >= 5 && Safads == 1 ){
                    notifsafadleave();
                }

                if (  15>= rhdistance && rhdistance >= 5 && RHs == 1 ){
                    notifrhleave();
                }

                info = (ImageView) findViewById(R.id.infor);

                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(TrackActivity.this , InfoActivity.class);
                        startActivity(intent2);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(RH));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(RH,15));
    }

    //100meter notif
    private void notifportal(){
        NotificationCompat.Builder notificationBuilderPortal = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You may now throw your garbage at PORTAL collection area")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("It is now 100 meters away for point A (Portal)"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,notificationBuilderPortal.build());


    }

    private void notifrh(){
        NotificationCompat.Builder notificationBuilderRH = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You may now throw your garbage at RH collection areaN")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("It is now 100 meters away from point B (RH)"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager1.notify(0,notificationBuilderRH.build());

    }

    private void notifsafad(){
        NotificationCompat.Builder notificationBuilderSafad = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You may now throw your garbage at SAFAD collection area")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("It is now 100 meters away from point C (Safad)"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager2.notify(0,notificationBuilderSafad.build());

    }

    //near

    private void notifportalnear(){
        NotificationCompat.Builder notificationBuilderPortalnear = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Garbage Truck is now at Portal Collection Area")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Truck has arrived and now collecting the garbage"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager0 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager0.notify(0,notificationBuilderPortalnear.build());


    }

    private void notifrhnear(){
        NotificationCompat.Builder notificationBuilderRHnear = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Garbage Truck is now at RH Collection Area")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Truck has arrived and now collecting the garbage"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager11 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager11.notify(0,notificationBuilderRHnear.build());

    }

    private void notifsafadnear(){
        NotificationCompat.Builder notificationBuilderSafadnear = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Garbage Truck is now at SAFAD Collection Area")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Truck has arrived and now collecting the garbage"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager22 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager22.notify(0,notificationBuilderSafadnear.build());

    }

    //leaving

    private void notifportalleave(){
        NotificationCompat.Builder notificationBuilderPortalleave = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Garbage Collection for PORTAL has ended")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Truck is now moving to next collection point (RH)"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager00 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager00.notify(0,notificationBuilderPortalleave.build());


    }

    private void notifrhleave(){
        NotificationCompat.Builder notificationBuilderRHleave = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Garbage Collection for RH has ended")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Truck is now moving to next collection point (SAFAD)"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager111 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager111.notify(0,notificationBuilderRHleave.build());

    }

    private void notifsafadleave(){
        NotificationCompat.Builder notificationBuilderSafadleave = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Garbage collection for SAFAD has ended")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Garbage collection for the day has finished"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager manager222 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager222.notify(0,notificationBuilderSafadleave.build());

    }
}
