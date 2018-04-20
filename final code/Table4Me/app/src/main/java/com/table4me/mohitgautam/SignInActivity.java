package com.table4me.mohitgautam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity
{
    private EditText email,pass;
    private Button mBTN;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth=FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        email = findViewById(R.id.LoginEmail);
        pass =findViewById(R.id.LoginPassword);
        mBTN=findViewById(R.id.SignINBtn);

        mBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mProgressDialog = new ProgressDialog(SignInActivity.this);
                mProgressDialog.setTitle("Signing In");
                mProgressDialog.setMessage("Please wait, verifying Credentials");
                mProgressDialog.setCanceledOnTouchOutside(false);
                VerifyAndSignIn();
            }
        });

        RelativeLayout SignUpSwitchText = findViewById(R.id.SignUpSwitchText);

        SignUpSwitchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void VerifyAndSignIn()
    {
        String Email,Password;

        Email=email.getText().toString();
        Password=pass.getText().toString();

        if(Email.isEmpty() || Password.isEmpty())
        {
            Toast.makeText(SignInActivity.this,"Either Email Or Password Left Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        else
        {
            mProgressDialog.show();
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(SignInActivity.this, "Signed In Successfully.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this,ChoosePreferenceActivity.class);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }
                            else
                            {
                                mProgressDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "Sign In failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(SignInActivity.this,ChoosePreferenceActivity.class);
            startActivity(intent);
        }
    }
}
