package com.table4me.mohitgautam;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChooseByLocationActivity extends AppCompatActivity
{
    boolean gps_enabled = true;
    boolean network_enabled = false;
    private float shortest = 0, prev = 0, Dist;
    private double Elat, Elon;
    private ProgressBar mResturantProgress;
    private FirebaseRecyclerAdapter<myRestaurantsClass, EventsViewHolder> adapter;
    private List<myRestaurantsClass> RestModel = new ArrayList<myRestaurantsClass>();
    private RecyclerView ResturantRecycler;
    private DatabaseReference mEventDataBase;
    private TextView nearestText;
    private CardView updateLocationCard;
    LocationManager locationManager;
    Double latitude=0.0,longitude=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_by_location);
        getSupportActionBar().hide();
        //////////////////////////////////// Activity Initialization ///////////////////////////////////////////////////////////////////
        mResturantProgress = findViewById(R.id.mResturantProgress);
        ResturantRecycler = findViewById(R.id.ResturantRecycler);
        nearestText = findViewById(R.id.NearestText);
        updateLocationCard = findViewById(R.id.updateLoc);
        updateLocationCard.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ChooseByLocationActivity.this);
        mLayoutManager.setReverseLayout(true);
        ResturantRecycler.setLayoutManager(mLayoutManager);

        mEventDataBase = FirebaseDatabase.getInstance().getReference().child("VendorRestaurants");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////// Get the Users Current Location On Activity Start///////////////////////////////////////
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /////////////////////////////// Permission Check Required by Android OS............
        LocationListener locationListener = new ChooseByLocationActivity.MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Toast.makeText(this, "Getting Location", Toast.LENGTH_SHORT).show();

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        Location mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        ////////////// if Location Retrieved Ok then Set Lat and Lon
        if(mLocation!=null)
        {
            latitude=mLocation.getLatitude();
            longitude=mLocation.getLongitude();
        }

    }

    /*---------- Listener class to get coordinates ------------- */
    //////////////// Required By Location Manager Object ////////////////////
    private class MyLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location loc)
        {

            longitude=loc.getLongitude();
            latitude=loc.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    // Method To Calculate Distance in KM
    protected float getDistance()
    {
        float[] res = new float[1];
        Location.distanceBetween(latitude, longitude, Elat, Elon, res);
        // This function takes into parameter the users current coordinates
        // and the coordinates of the restaurant and then returns the distance in KM's
        float dis = res[0];
        dis = dis / 1000;
        return dis;
    }



    ////////////////////////////////////////////////////////  LOAD Restaurants /////////////////////////////////////////////////////////////////
    private void FindNearestResturant()
    {

        Log.d("mCheck->","Here 2");
        adapter = new FirebaseRecyclerAdapter<myRestaurantsClass, EventsViewHolder>(myRestaurantsClass.class,
                R.layout.the_resturant_row, EventsViewHolder.class, mEventDataBase)
        {
            /// Get the Restaurants Info from the Firebase DB and Populate them one by one in the XML via VIEWHOLDER class
            @Override
            protected void populateViewHolder(EventsViewHolder viewHolder, final myRestaurantsClass model, int position)
            {
                mResturantProgress.setVisibility(View.GONE);
                RestModel.add(model);

                Log.d("mCheck->",model.getResName());
                Elat = model.getLat();
                Elon = model.getLon();
                Dist = getDistance();
                if(Dist>15)
                {
                    viewHolder.SETVISIBILITY();
                    return;
                }
                //////////////// Restaurant Data retrieved from Firebase DB and is Populated in the ViewHolder
                String mdist = String.valueOf(Dist).substring(0, 4);
                viewHolder.setTags(model.getResTags());
                viewHolder.setName(model.getResName());
                viewHolder.setDistance("Distance : " + mdist + " km");
                if (shortest == prev)
                {
                    nearestText.setText("Nearest Resturant : " + model.getResName());
                    shortest = Dist;
                }
                else if (Dist < shortest) // New Distance Less Than Previous Distance
                {
                    nearestText.setText("Nearest Resturant : " + model.getResName());
                    shortest = Dist;
                }
                else if (Dist > shortest) // New Distance Greater Than Previous (No Change)
                {
                    prev = Dist;
                }

                viewHolder.setResImage(model.getResImage(),ChooseByLocationActivity.this);
            }

            @Override
            public EventsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
            {

                ///////////////////////////////// On Click Listner for each restaurant Card//////////////////////////////////////////
                EventsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new EventsViewHolder.ClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(ChooseByLocationActivity.this, ReservationActivity.class);
                        bundle.putString("From","TableChooser");
                        bundle.putString("Restaurant_Name", RestModel.get(position).getResName());
                        bundle.putString("RestaurantID", RestModel.get(position).getOwnerID());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position)
                    {
                    }
                });
                return viewHolder;
            }
        };


        ResturantRecycler.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(latitude!=0.0 && longitude!=0.0)
        {
            //////// If Location is Set then Search For Nearest Restaurant //////////////////////////////
            FindNearestResturant();
        }

    }

    //////////////////// Cleas to Hold the Data from Firebase DB and Populate in the Row XML/////////////////////////
    public static class EventsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView Name, distance, rType,TAGS;
        public ImageView mImage;
        public CardView restCard;
        public EventsViewHolder(View itemView) {
            super(itemView);

            distance = itemView.findViewById(R.id.DistanceText);
            Name = itemView.findViewById(R.id.mRestName);
            rType = itemView.findViewById(R.id.TypeText);
            mImage=itemView.findViewById(R.id.ResImage);
            TAGS=itemView.findViewById(R.id.ResTAGS);
            restCard=itemView.findViewById(R.id.restCard);

            //listener set on ENTIRE ROW, you may set on individual components within a row.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }

        private EventsViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(EventsViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }


        public void setName(String name) {
            Name.setText(name);
        }

        public void setDistance(String dist) {
            distance.setText(dist);
        }

        public void setType(String type) {
            rType.setText(type);
        }


        public void setTags(String tags) {
            TAGS.setText(tags);
        }

        public void setResImage(String path, Context context)
        {
            Picasso.with(context).load(path).placeholder(R.color.colorAccent).into(mImage);
        }

        public void SETVISIBILITY()
        {
            restCard.setVisibility(View.GONE);
        }
    }

}
