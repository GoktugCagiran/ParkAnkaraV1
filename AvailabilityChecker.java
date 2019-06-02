package com.example.parkankara;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class AvailabilityChecker extends Service {

    //variables
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Timer timer;
    TimerTask timerTask;
    static double checkLatitude;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CarPark");
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for( DataSnapshot ds : dataSnapshot.getChildren()){
                            try {
                                if ( checkLatitude == Double.parseDouble(String.valueOf(ds.child("latitude").getValue() ) ) ) {
                                    System.out.println(checkLatitude);
                                    if ((Integer.parseInt(String.valueOf(ds.child("fullCapacity").getValue())) - Integer.parseInt(String.valueOf(ds.child("currentCars").getValue()))) <= 0) {
                                        stopService(new Intent(getApplicationContext(), AvailabilityChecker.class));
                                    }
                                }
                            }catch (Exception e)
                            {
                                stopService(new Intent(getApplicationContext(), AvailabilityChecker.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        timer.schedule(timerTask,0,8000);
        return START_STICKY;
    }

    /**
     * Ends the service and calls  the necessary methods
     */
    @Override
    public void onDestroy() {
        timer.cancel();
        Integer choice = getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE).getInt("fullChoice", 0);
        if(!Locations.isFull) {
            if (choice == 1) {
                Toast.makeText(getApplicationContext(), "Park yeri doldu", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Locations.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (choice == 0){
                Toast.makeText(getApplicationContext(), "Park yeri doldu", Toast.LENGTH_LONG).show();
            }
            Locations.isFull = true;
        }


    }
}
