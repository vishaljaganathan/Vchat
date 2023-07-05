package com.vishcorp.vchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class proactivity extends AppCompatActivity {


    EditText username;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    TextView updateprofile;

    FirebaseFirestore firebaseFirestore;

    ImageView viewuserimage;

    StorageReference storageReference;

    private String ImageURIaccessToken;

    androidx.appcompat.widget.Toolbar toolbarprofile;
    ImageButton backbutton;


    FirebaseStorage firebaseStorage;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proactivity);

        viewuserimage=findViewById(R.id.getuserimageinimageviews);
        username=findViewById(R.id.usernameproactivity);
        updateprofile=findViewById(R.id.movetoupdateprofile);
        firebaseFirestore=FirebaseFirestore.getInstance();
        toolbarprofile=findViewById(R.id.toolbarprofile);
        backbutton=findViewById(R.id.probackbtn);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();


        setSupportActionBar(toolbarprofile);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        storageReference=firebaseStorage.getReference();
        storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIaccessToken=uri.toString();
                Picasso.get().load(uri).into(viewuserimage);

            }
        });

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userprofile userprofile=snapshot.getValue(userprofile.class);
                username.setText(userprofile.getUname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),"Failed To Fetch",Toast.LENGTH_SHORT).show();
            }
        });


        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(proactivity.this,profileupdate.class);
                intent.putExtra("name of user",username.getText().toString());
                startActivity(intent);
            }
        });




    }


    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {

            }
        });

    }
}