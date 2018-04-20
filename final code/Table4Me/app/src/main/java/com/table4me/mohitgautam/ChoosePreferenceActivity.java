package com.table4me.mohitgautam;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ChoosePreferenceActivity extends AppCompatActivity
{
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    boolean key=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_preference);

        getSupportActionBar().hide();
        Button location,cusine;

        location=findViewById(R.id.LocationBtn);
        cusine=findViewById(R.id.CusineBtn);

        location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    getLocationPermission();
                }
                if(key)
                {
                    Intent intent = new Intent(ChoosePreferenceActivity.this,ChooseByLocationActivity.class);
                    startActivity(intent);
                }

            }
        });


        cusine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    getLocationPermission();
                }
                if(key)
                {
                    Intent intent = new Intent(ChoosePreferenceActivity.this,ChooseCuisineActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {

    }

    ///////////////////////////////////// Get Runtime Location Permission For Android Marshmello And Above////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(ChoosePreferenceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            askForPermission();
        }
        else {key=true;}
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForPermission()
    {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED)
        {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                showMessageOKCancel("Allow Access To Location Info",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }

            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(ChoosePreferenceActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ChoosePreferenceActivity.this, " Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                    key = true;
                    Intent intent = new Intent(ChoosePreferenceActivity.this,ChooseByLocationActivity.class);
                    startActivity(intent);
                    // Permission Granted put your code here
                }
                else
                {
                    // Permission Denied
                    Toast.makeText(ChoosePreferenceActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(ChoosePreferenceActivity.this,SignInActivity.class);
            startActivity(intent);
        }
    }
}
