package com.table4me.mohitgautam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity
{

    private EditText UserName,UserEmail,UserPassword;
    private Button signup;
    private RelativeLayout switchText;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        UserEmail= findViewById(R.id.UserEmail);
        UserPassword=findViewById(R.id.UserPassword);
        UserName=findViewById(R.id.UserName);
        signup=findViewById(R.id.SignUPBtn);
        switchText=findViewById(R.id.SignUpSwitchText);


        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mProgressDialog = new ProgressDialog(SignUpActivity.this);
                mProgressDialog.setTitle("Signing Up");
                mProgressDialog.setMessage("Please wait, verifying credentials for new account");
                mProgressDialog.setCanceledOnTouchOutside(false);
                VerifyAndSignUp();
            }
        });

        switchText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
             startActivity(intent);
            }
        });



    }

    private void VerifyAndSignUp()
    {
        // In This Method We Will Validate The Credentials Given by user To Create New Account
        Boolean key=true;
        String Name,Email,Password;

        Name = UserName.getText().toString();
        Email=UserEmail.getText().toString();
        Password=UserPassword.getText().toString();

        // Check if email,password and confirm password are not empty
        if(Name.isEmpty() || Email.isEmpty() || Password.isEmpty() )
        {
            MakeMyToast("Please Make Sure No Fields Are Left Empty.");
            key=false;
        }
        //Check If Password Length is NOT less than 6
        else if(Password.length()<6)
        {
            MakeMyToast("Password Should Be Atleast 6 Characters Long.");
            key=false;
        }
        // if everything is OKAY then try Signing Up new User
        if(key)
        {
            mProgressDialog.show();
            register_user(Email,Password,Name);
        }
        else
        {
            mProgressDialog.dismiss();
        }
    }

    private void register_user(final String email, final String password,final String name)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    if(current_user==null)
                    {
                        MakeMyToast("Could Not Get Current User From Firebase");
                        mProgressDialog.dismiss();
                        return;
                    }

                    String uid = current_user.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> UserInfoMAP = new HashMap<>();
                    UserInfoMAP.put("CurrentToken", device_token);
                    UserInfoMAP.put("Email", email);
                    UserInfoMAP.put("Password", password);
                    UserInfoMAP.put("ProfileImage", "default");
                    UserInfoMAP.put("UserName",name);
                    mDatabase.setValue(UserInfoMAP).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                            if(task.isSuccessful())
                            {
                                mProgressDialog.dismiss();
                                Intent mainIntent = new Intent(SignUpActivity.this, ChoosePreferenceActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });
                }
                else
                {
                    mProgressDialog.dismiss();
                    MakeMyToast("SignUp Failed");
                }
            }
        });
    }


    private void MakeMyToast(String message)
    {
        Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_SHORT).show();
    }

}
