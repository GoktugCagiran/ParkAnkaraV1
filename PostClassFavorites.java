package com.example.parkankara;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PostClassFavorites extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> cpName;
    private final ArrayList<String> cpAddress;
    private final ArrayList<String> cpCondition;
    Set<String> favorites;
    Set<String> newFavorites;
    SharedPreferences sharedPreferences;
    ToggleButton toggleButton;

    /**
     *
     * @param cpName name of all the favorited  car parks
     * @param cpAddress address of all the favorited  car parks
     * @param cpCondition condition of all the favorited  car parks
     * @param context context of the parent activity
     */
    public PostClassFavorites(ArrayList<String> cpName, ArrayList<String> cpAddress, ArrayList<String> cpCondition, Activity context) {
        super(context, R.layout.favorite_car_park_info, cpName);
        this.context = context;
        this.cpName = cpName;
        this.cpAddress = cpAddress;
        this.cpCondition = cpCondition;
        sharedPreferences = context.getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);
        favorites = sharedPreferences.getStringSet("CarParkName", new HashSet<String>());
        newFavorites = new HashSet<>();
    }

    /**
     * @param position current index of  all arrays
     * @param convertView the old View to use if possible
     * @param parent The parent that this view will be  attached to
     * @return View corresponding to the data at the specified location
     */
    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View favoriteCarParkInfo = layoutInflater.inflate(R.layout.favorite_car_park_info, null, true);
        //Find components
        TextView cpNameText = favoriteCarParkInfo.findViewById(R.id.favCPName);
        TextView addressText = favoriteCarParkInfo.findViewById(R.id.favCPAddress);
        TextView conditionText = favoriteCarParkInfo.findViewById(R.id.favCPCondition);
        //set method  of components
        cpNameText.setText( cpName.get(position) );
        addressText.setText( cpAddress.get(position) );

        //Checks if the car park is full and then writes the appropriate text to TextView
        if(Integer.parseInt( cpCondition.get(position) ) <= 0)
        {
            conditionText.setText("Car Park Full!!");
        }
        else {
            conditionText.setText("Empty Space: " + cpCondition.get(position));
        }

        toggleButton = (ToggleButton) favoriteCarParkInfo.findViewById(R.id.toggleButton);
        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.favorites));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.see_the_map_background));
                    favorites.remove(cpName.get(position));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.commit();
                    Toast.makeText(context, cpName.get(position) + " removed from favorites", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, FavoriteCarParks.class);
                    context.startActivity(intent);
                }
            }

        });
        return favoriteCarParkInfo;
    }
}