package com.vishcorp.vchat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class chatfrag extends Fragment{


    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;

    ImageView imageviewofuser;

    FirestoreRecyclerAdapter<firebasemodal,NoteViewHolder> chatAdapter;

    RecyclerView recyclerview;



    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chatfragment,container,false);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        recyclerview=view.findViewById(R.id.chatrecycleview);

        //Query query=firebaseFirestore.collection("Users");
        Query query=firebaseFirestore.collection("Users").whereNotEqualTo("uid",firebaseAuth.getUid());
        FirestoreRecyclerOptions<firebasemodal>allusername=new FirestoreRecyclerOptions.Builder<firebasemodal>().setQuery(query, firebasemodal.class).build();

        chatAdapter=new FirestoreRecyclerAdapter<firebasemodal, NoteViewHolder>(allusername){
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodal firebase) {

                noteViewHolder.particularusername.setText(firebase.getName());
                String uri=firebase.getImage();

                Picasso.get().load(uri).into(imageviewofuser);
                if(firebase.getStatus().equals("Online"))
                {
                    noteViewHolder.statusofuser.setText(firebase.getStatus());
                    noteViewHolder.statusofuser.setTextColor(Color.BLUE);
                }
                else
                {
                    noteViewHolder.statusofuser.setText(firebase.getStatus());
                }

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(),specialchat.class);
                        intent.putExtra("name",firebase.getName());
                        intent.putExtra("receiveruid",firebase.getUid());
                        intent.putExtra("imageuri",firebase.getImage());
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatview,parent,false);
                return new NoteViewHolder(view);
            }
        };


        recyclerview.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(chatAdapter);


        return view;




    }


    public class NoteViewHolder extends RecyclerView.ViewHolder
    {

        private TextView particularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.nameofuser);
            statusofuser=itemView.findViewById(R.id.statusofuser);
            imageviewofuser=itemView.findViewById(R.id.imageviewofuser);




        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null)
        {
            chatAdapter.stopListening();
        }
    }
}
