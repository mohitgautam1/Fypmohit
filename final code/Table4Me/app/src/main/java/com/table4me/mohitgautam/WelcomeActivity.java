package com.table4me.mohitgautam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(WelcomeActivity.this,ChoosePreferenceActivity.class);
            startActivity(intent);
        }

        else
        {
            setContentView(R.layout.activity_welcome);
            Button signup,signin;

            signup=findViewById(R.id.SignUpBtn);
            signin=findViewById(R.id.SignInBtn);


            signin.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(WelcomeActivity.this,SignInActivity.class);
                    startActivity(intent);
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(WelcomeActivity.this,SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }

    }


}
