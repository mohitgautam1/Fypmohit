package com.table4me.mohitgautam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowRestaurantsByCuisineActivity extends AppCompatActivity
{
    private TextView ResturantName,restCount;
    private List<myRestaurantsClass> RestModel = new ArrayList<myRestaurantsClass>();
    private DatabaseReference mEventDataBase;
    private String Cuisine="n/a";
    private ImageView image;
    private int Count=0;
    private RecyclerView ResturantRecycler;
    private FirebaseRecyclerAdapter<myRestaurantsClass, ShowRestaurantsByCuisineActivity.EventsViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurants_by_cuisine);

        getSupportActionBar().hide();

        image=findViewById(R.id.BackChooseCuisine);
        restCount=findViewById(R.id.foundNumber);
        ResturantName=findViewById(R.id.foundType);

        ResturantRecycler=findViewById(R.id.mRecyclerView);


        if(getIntent().getExtras()!=null)
        {
            Bundle extras = getIntent().getExtras();
            Cuisine=extras.getString("mCuisine");
            ResturantName.setText(Cuisine);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ShowRestaurantsByCuisineActivity.this);
        ResturantRecycler.setLayoutManager(mLayoutManager);

        mEventDataBase = FirebaseDatabase.getInstance().getReference().child("VendorRestaurants");
        // Back Button
        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ShowRestaurantsByCuisineActivity.this,ChooseCuisineActivity.class);
                startActivity(intent);
            }
        });

        FindResturant();
    }

    ////////////////////////////////////////////////////////  LOAD Restauurants /////////////////////////////////////////////////////////////////
    private void FindResturant()
    {
        final ProgressDialog progressDialog = new ProgressDialog(ShowRestaurantsByCuisineActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        adapter = new FirebaseRecyclerAdapter<myRestaurantsClass, ShowRestaurantsByCuisineActivity.EventsViewHolder>(myRestaurantsClass.class,
                R.layout.the_resturant_row, ShowRestaurantsByCuisineActivity.EventsViewHolder.class, mEventDataBase)
        {
            @Override
            protected void populateViewHolder(EventsViewHolder viewHolder, myRestaurantsClass model, int position)
            {
                if(model!=null)
                {
                    ///////////// Restauarnt retrieved from firebase here
                    RestModel.add(model);
                    viewHolder.setType(model.getResType());
                    viewHolder.setName(model.getResName());
                    viewHolder.setTags(model.getResTags());
                    viewHolder.setResImage(model.getResImage(),ShowRestaurantsByCuisineActivity.this);

                    ////////////// if the retrieved restaurant has the same type as the type selected in Choose by cusine activity then OK
                    /// otherwise hide that restaurant
                    if(model.getResType().equals(Cuisine))
                    {
                        Count++;
                        restCount.setText(String.valueOf(Count));
                        progressDialog.dismiss();
                    }

                    else
                    {
                        viewHolder.setInVisibility();
                        progressDialog.dismiss();
                    }
                }

            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public ShowRestaurantsByCuisineActivity.EventsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
            {
                ShowRestaurantsByCuisineActivity.EventsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                //////////////////////////////// ///////////Click Listner ///////////////////////////////////////////////////
                viewHolder.setOnClickListener(new ChooseByLocationActivity.EventsViewHolder.ClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(ShowRestaurantsByCuisineActivity.this, ReservationActivity.class);
                        bundle.putString("From", "Cuisine");
                        bundle.putString("Restaurant_Name", RestModel.get(position).getResName());
                        bundle.putString("RestaurantID", RestModel.get(position).getOwnerID());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                });
                return viewHolder;
            }

        };

        ResturantRecycler.setAdapter(adapter);
    }


    public static class EventsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView Name, distance, rType,resTAGS;
        public CardView restCard;
        public ImageView ResImage;

        public EventsViewHolder(View itemView) {
            super(itemView);

            distance = itemView.findViewById(R.id.DistanceText);
            Name = itemView.findViewById(R.id.mRestName);
            rType = itemView.findViewById(R.id.TypeText);
            restCard=itemView.findViewById(R.id.restCard);
            ResImage=itemView.findViewById(R.id.ResImage);
            resTAGS=itemView.findViewById(R.id.ResTAGS);

            distance.setVisibility(View.GONE);

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

        private ChooseByLocationActivity.EventsViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(ChooseByLocationActivity.EventsViewHolder.ClickListener clickListener) {
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

        public void setInVisibility()
        {
            restCard.setVisibility(View.GONE);
        }


        public void setTags(String tags) {
            resTAGS.setText(tags);
        }

        public void setResImage(String path, Context context)
        {
            Picasso.with(context).load(path).placeholder(R.color.colorAccent).into(ResImage);
        }


    }
}
