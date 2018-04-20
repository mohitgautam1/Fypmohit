package com.table4me.mohitgautam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReservationActivity extends AppCompatActivity
{
    private TimePicker timePicker1;
    private Calendar calendar;
    private TextView time,title;
    private String format;
    private Button mBtn,SeatBtn;
    private Button ReserveBtn;
    private String theTime,Restaurant,from,TableNum,mSeats,RestaurantID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();

        ReserveBtn=findViewById(R.id.ReserveBtn);
        mBtn=findViewById(R.id.TimeBtn);
        time=findViewById(R.id.timeText);
        timePicker1 = findViewById(R.id.TimePicker);
        title=findViewById(R.id.ReservationRestaurant);
        SeatBtn=findViewById(R.id.SeatingPlanBtn);

        if(getIntent().getExtras()!=null)
        {
            Bundle extras = getIntent().getExtras();

            from = getIntent().getStringExtra("From");

            if(from.equals("TableChooser"))
            {
                TableNum = extras.getString("TableNumber");
                RestaurantID=extras.getString("RestaurantID");
                mSeats=extras.getString("Seats");
                Restaurant=extras.getString("Restaurant_Name");
            }

            else
            {
                Restaurant=getIntent().getStringExtra("Restaurant_Name");
                RestaurantID=getIntent().getStringExtra("RestaurantID");
                title.setText(Restaurant+" Reservation");
            }

        }

        time.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        ReserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setTime();
                MakeReservation();
            }
        });

        SeatBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // Use These Conditions To filter the restaurant seating plans for table selection and showing different table Layouts for Different Restaurants
//                if(Restaurant.equals("Second Cup"))
//                {
//
//                }
                Intent intent = new Intent(ReservationActivity.this,TableChooserActivity.class);
                intent.putExtra("RestaurantID",RestaurantID);
                intent.putExtra("Restaurant_Name",Restaurant);
                startActivity(intent);
            }
        });
    }


    private void MakeReservation()
    {
        String uid = mAuth.getCurrentUser().getUid();
        //////// Make a Node In Database For Reservations that will have subnode of the Restaurant that got the booking
        /// and that will have another child node that will be the Reservation Maker (User)
        /// 1 booking per USER per Restaurant
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Reservations")
                                     .child(RestaurantID)
                                     .child(uid);

        final ProgressDialog mProgressDialog = new ProgressDialog(ReservationActivity.this);
        mProgressDialog.setTitle("Making Your Reservation");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        HashMap<String, String> ReservationInfoMAP = new HashMap<>();
        ReservationInfoMAP.put("ReserverEmail", mAuth.getCurrentUser().getEmail());
        ReservationInfoMAP.put("RestaurantName", Restaurant);
        Log.d("MCHECK->",Restaurant);
        ReservationInfoMAP.put("Reservation_Time", theTime);
        ReservationInfoMAP.put("Table_Number", TableNum);
        ReservationInfoMAP.put("Reservation_Seats", mSeats);
        ReservationInfoMAP.put("ReserverID",uid);

        mDatabase.setValue(ReservationInfoMAP).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    Intent mainIntent = new Intent(ReservationActivity.this, ChoosePreferenceActivity.class);
                    startActivity(mainIntent);
                    Toast.makeText(ReservationActivity.this,"Your Booking Was Successful",Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
                else {mProgressDialog.dismiss();}
            }
        });
    }

    public void setTime()
    {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);
    }


    public void showTime(int hour, int min)
    {
        time.setVisibility(View.VISIBLE);

        if (hour == 0)
        {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12)
        {
            format = "PM";
        }
        else if (hour > 12)
        {
            hour -= 12;
            format = "PM";
        }
        else
            {
            format = "AM";
        }

         theTime = hour+" : "+min+" "+format;
         time.setText("Selected Time : "+theTime);
    }
}
