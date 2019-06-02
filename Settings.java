package com.example.parkankara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    private Spinner carParkFullOptions;
    private ArrayAdapter<String> dataAdapterForCarParkFullOptions;
    private ArrayList<String> options;
    SharedPreferences checkFullChoice;
    Integer fullChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        checkFullChoice = getApplicationContext().getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);
        fullChoice = new Integer(checkFullChoice.getInt("fullChoice", 0));

        //Find or create Components
        carParkFullOptions = findViewById(R.id.carParkFullOptions);
        options = new ArrayList<>();
        options.add("Hiçbir şey yapılmasın");
        options.add("Lokasyon menüsüne geri dön");

        //Create the adapter and connect
        dataAdapterForCarParkFullOptions = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        dataAdapterForCarParkFullOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        carParkFullOptions.setAdapter(dataAdapterForCarParkFullOptions);
        carParkFullOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fullChoice = position;
                SharedPreferences.Editor editor = checkFullChoice.edit();
                editor.putInt("fullChoice", fullChoice).apply();
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
