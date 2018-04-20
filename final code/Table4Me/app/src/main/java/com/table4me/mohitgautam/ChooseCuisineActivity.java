package com.table4me.mohitgautam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class ChooseCuisineActivity extends AppCompatActivity
{

    private CardView card1,card2,card3,card4,card5;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cuisine);

        getSupportActionBar().hide();

        image=findViewById(R.id.BackChooseCuisine);
        card1=findViewById(R.id.Card1);
        card2=findViewById(R.id.Card2);
        card3=findViewById(R.id.Card3);
        card4=findViewById(R.id.Card4);
        card5=findViewById(R.id.Card5);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ChooseCuisineActivity.this,ChoosePreferenceActivity.class);
                startActivity(intent);
            }
        });


    }

    public void CardClickListner(View view)
    {
        if(view!=null)
        {
            CardView card = (CardView)view;
            String cusisne = view.getTag().toString();

            Bundle bundle = new Bundle();
            Intent intent = new Intent(ChooseCuisineActivity.this,ShowRestaurantsByCuisineActivity.class);
            bundle.putString("mCuisine",cusisne);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }



}
