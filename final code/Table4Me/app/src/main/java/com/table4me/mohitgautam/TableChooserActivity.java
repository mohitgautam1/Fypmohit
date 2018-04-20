package com.table4me.mohitgautam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TableChooserActivity extends AppCompatActivity
{
    private boolean selected=false;
    private Button deselect,done;
    private String SeatingInfo,a,b,c;
    private LinearLayout linearLayout;
    private ImageView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t12,t13,t14;
    private String TableNum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_reservation);
        getSupportActionBar().hide();

        deselect=findViewById(R.id.DeSelectBtn);
        done=findViewById(R.id.mDoneBtn);
        linearLayout=findViewById(R.id.TablesContainer);
        t1=findViewById(R.id.Table1);
        t2=findViewById(R.id.Table2);
        t3=findViewById(R.id.Table3);
        t4=findViewById(R.id.Table4);
        t5=findViewById(R.id.Table5);
        t6=findViewById(R.id.Table6);
        t7=findViewById(R.id.Table7);
        t8=findViewById(R.id.Table8);
        t9=findViewById(R.id.Table9);
        t10=findViewById(R.id.Table10);
        t12=findViewById(R.id.Table12);
        t13=findViewById(R.id.Table13);
        t14=findViewById(R.id.Table14);


        final String RestaurantID = getIntent().getStringExtra("RestaurantID");
        final String RestaurantName = getIntent().getStringExtra("Restaurant_Name");

        setReservedTables(RestaurantID);

        deselect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        //////// When Done button is clicked it is checked that all relevant info is filled and then that info os sent to reservation activity....
        //////// ....... In a Bundle

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!selected)
                {
                    SeatingInfo="n/a";
                    a="n/a";
                    b="n/a";
                    c="n/a";
                    Toast.makeText(TableChooserActivity.this,"No Table Selected",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(TableChooserActivity.this,"Table Num : "+a+"\nSeats : "+b+"\nRes Name: "+RestaurantName
                            ,Toast.LENGTH_LONG).show();
                }

                Bundle bundle = new Bundle();
                Intent intent = new Intent(TableChooserActivity.this,ReservationActivity.class);
                bundle.putString("From","TableChooser");
                bundle.putString("TableNumber",a);
                bundle.putString("Seats",b);
                bundle.putString("RestaurantID",RestaurantID);
                bundle.putString("Restaurant_Name",RestaurantName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    // Makes The tables already Reserved Unavailable For Booking
    private void setReservedTables(String ResId)
    {
        ///////////////////////////// Make Database Reference and Point it towards Required Node
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Reservations").child(ResId);
        ProgressDialog progressDialog = new ProgressDialog(TableChooserActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Log.d("MCHECK->","Table Retrieving 2");
                // Run Loop Until You find nodes in specifed database reference
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    TableNum = (String)dataSnapshot.child("Table_Number").getValue();
                    Log.d("MCHECK->","Table Retrieving 3");

                    if(TableNum.equals("Table1"))
                    {
                        t1.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                        t1.setClickable(false);
                    }
                    else if(TableNum.equals("Table2"))
                    {
                        t2.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                        t2.setClickable(false);
                    }
                    else if(TableNum.equals("Table3"))
                    {
                        t3.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                        t3.setClickable(false);
                    }
                    else if(TableNum.equals("Table4"))
                    {
                        t4.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                        t4.setClickable(false);
                    }
                    else if(TableNum.equals("Table5"))
                    {
                        t5.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                        t5.setClickable(false);
                    }
                    else if(TableNum.equals("Table6"))
                    {
                        t6.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                        t6.setClickable(false);
                    }
                    else if(TableNum.equals("Table7"))
                    {
                        t7.setClickable(false);
                        t7.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                    }
                    else if(TableNum.equals("Table8"))
                    {
                        t8.setClickable(false);
                        t8.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                    }
                    else if(TableNum.equals("Table9"))
                    {
                        t9.setClickable(false);
                        t9.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                    }
                    else if(TableNum.equals("Table10"))
                    {
                        t10.setClickable(false);
                        t10.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                    }
                    else if(TableNum.equals("Table12"))
                    {
                        t12.setClickable(false);
                        t12.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                    }
                    else if(TableNum.equals("Table13"))
                    {
                        t13.setClickable(false);
                        t13.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_edge_btn));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressDialog.dismiss();
    }

    public void setReservationTable(View view)
    {
        if(!selected)
        {
            selected=true;
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
            ImageView Table = (ImageView) view;
            SeatingInfo = view.getTag().toString();

            int Seperator=SeatingInfo.indexOf(",");
            a=SeatingInfo.substring(0,Seperator);
            b=SeatingInfo.substring(Seperator+1,SeatingInfo.length());
            Toast.makeText(TableChooserActivity.this,"Table Selected\nTable Number : "+a+"\nSeats : "+b,
                    Toast.LENGTH_SHORT).show();

            Table.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_reserved_table));

        }

        else
        {
            Toast.makeText(TableChooserActivity.this,"Please Deselect Table To Choose New Table",Toast.LENGTH_SHORT).show();
        }
    }


}
