package com.vishcorp.vchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText pnum;
    android.widget.Button sendotp;
    CountryCodePicker ccp;
    String countrycode;
    String phonenum;

    FirebaseAuth firebaseAuth;

    ProgressBar mainprobar;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;
    String codesend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ccp = findViewById(R.id.ccp);
        sendotp = findViewById(R.id.sendotp);
        pnum = findViewById(R.id.pnum);
        mainprobar = findViewById(R.id.probar);

        firebaseAuth = FirebaseAuth.getInstance();
        countrycode = ccp.getSelectedCountryCodeWithPlus();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode=ccp.getSelectedCountryCodeWithPlus();
            }
        });
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num;
                num = pnum.getText().toString();
                if(num.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Your Mobile number",Toast.LENGTH_SHORT).show();
                }
                else if(num.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Enter a valid Mobile number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mainprobar.setVisibility(view.VISIBLE);
                    phonenum = countrycode+num;
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phonenum)
                            .setTimeout(30L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(callback)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });
        callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String cs, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent( cs, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP is sent",Toast.LENGTH_SHORT).show();
                mainprobar.setVisibility(View.INVISIBLE);
                codesend = cs;
                Intent intent = new Intent(MainActivity.this,otppage.class);
                intent.putExtra("otp",codesend);
                startActivity(intent);

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent = new Intent(MainActivity.this,chatspace.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}