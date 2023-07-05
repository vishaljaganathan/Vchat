package com.vishcorp.vchat;



        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.cardview.widget.CardView;
        import androidx.core.app.NotificationCompat;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.squareup.picasso.Picasso;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;

public class specialchat extends AppCompatActivity {

    EditText getmessage;
    ImageButton sendmessagebutton;


    androidx.appcompat.widget.Toolbar toolbarofspecificchat;
    ImageView imageviewofspecificuser;
    TextView nameofspecificuser;

    private String enteredmessage;
    Intent intent;
    String recievername,sendername,recieveruid,senderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderarea,recieverarea;

    ImageButton backbuttonofspecialchat;

    RecyclerView messagerecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<messagesenderandreciver> messagesArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialchat);

        getmessage=findViewById(R.id.getmessage);

        sendmessagebutton=findViewById(R.id.imageviewsendmessage);
        toolbarofspecificchat=findViewById(R.id.toolbarspcchat);
        nameofspecificuser=findViewById(R.id.chatname);
        imageviewofspecificuser=findViewById(R.id.prochatimg);
        backbuttonofspecialchat=findViewById(R.id.chatbackbtn);
        messagesArrayList=new ArrayList<>();
        messagerecyclerview=findViewById(R.id.recyclerViewspecialchat);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        messagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(specialchat.this,messagesArrayList);
        messagerecyclerview.setAdapter(messagesAdapter);
        messagerecyclerview.scrollToPosition(0);


        intent=getIntent();

        setSupportActionBar(toolbarofspecificchat);
        toolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Toolbar is Clicked",Toast.LENGTH_SHORT).show();


            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");


        senderuid=firebaseAuth.getUid();
        recieveruid=getIntent().getStringExtra("receiveruid");
        recievername=getIntent().getStringExtra("name");



        senderarea=senderuid+recieveruid;
        recieverarea=recieveruid+senderuid;



        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderarea).child("messages");
        messagesAdapter=new MessagesAdapter(specialchat.this,messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    messagesenderandreciver messages=snapshot1.getValue(messagesenderandreciver.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        backbuttonofspecialchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        nameofspecificuser.setText(recievername);
        String uri=intent.getStringExtra("imageuri");
        if(uri.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"null is recieved",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Picasso.get().load(uri).into(imageviewofspecificuser);
        }




        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage=getmessage.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
                }

                else

                {
                    Date date=new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    messagesenderandreciver messages=new messagesenderandreciver(enteredmessage,firebaseAuth.getUid(),date.getTime(),currenttime);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderarea)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference()
                                            .child("chats")
                                            .child(recieverarea)
                                            .child("messages")
                                            .push()
                                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });

                    getmessage.setText(null);




                }




            }
        });



    }





    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderarea).child("messages");
        messagesAdapter=new MessagesAdapter(specialchat.this,messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    messagesenderandreciver messages=snapshot1.getValue(messagesenderandreciver.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }



}
