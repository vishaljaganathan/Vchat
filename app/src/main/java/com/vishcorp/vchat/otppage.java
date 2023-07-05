package com.vishcorp.vchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otppage extends AppCompatActivity {
    TextView changenumber;

    EditText getotp;

    android.widget .Button verifyotp;
    String enteredotp;

    FirebaseAuth firebaseauth;
    ProgressBar progressbarofotpauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        changenumber = findViewById(R.id.changenumber);
        verifyotp = findViewById(R.id.verifyotp);
        getotp = findViewById(R.id.getotp);
        progressbarofotpauth = findViewById(R.id.otpptobar);

        firebaseauth = FirebaseAuth.getInstance();

        changenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(otppage.this, MainActivity.class);

                startActivity(intent);
            }
        });
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredotp = getotp.getText().toString();
                if (enteredotp.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter the OTP", Toast.LENGTH_SHORT).show();
                } else {
                    progressbarofotpauth.setVisibility(View.VISIBLE);

                    String codereceived = getIntent().getStringExtra("otp");

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codereceived, enteredotp);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())

                {
                    progressbarofotpauth. setVisibility (View. INVISIBLE) ;
                    Toast .makeText (getApplicationContext(), "Login sucess", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(  otppage. this, profile.class);
                    startActivity (intent);
                    finish();

                }

                else {

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        progressbarofotpauth. setVisibility(View. INVISIBLE);
                        Toast .makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });
    }
}
