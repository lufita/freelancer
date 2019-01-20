package com.dlmkotlin.freelancer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    JobsAdapter adapter;
    ArrayList<Jobs> JobsArrayList;
    Button masukButton;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail().isEmpty()==false) {
                FirebaseAuth.getInstance().signOut();
            }
        }
        catch(Exception e) {

        }

        db = FirebaseFirestore.getInstance();
        showData();
        mSwipeRefreshLayout = findViewById(R.id.swiper);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshRecycle();
                showData();
            }
        });
        RefreshRecycle();
        masukButton = (Button) findViewById(R.id.SignButton);
        masukButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == masukButton) {
                    Masuk();
                }
            }
        });


    }

    public void RefreshRecycle(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new JobsAdapter(JobsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showData(){
     JobsArrayList = new ArrayList<Jobs>();
        db.collection("JobsList").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e !=null)
                {

                }

                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    String name =  documentChange.getDocument().getData().get("Name").toString();
                    String phone   =  documentChange.getDocument().getData().get("Phone").toString();
                    String email = documentChange.getDocument().getData().get("Email").toString();
                    String jobsname = documentChange.getDocument().getData().get("JobsName").toString();
                    String jobsdetail = documentChange.getDocument().getData().get("JobsDetail").toString();
                    String taken = documentChange.getDocument().getData().get("Taken").toString();
                    JobsArrayList.add(new Jobs(name, phone, email, jobsname, jobsdetail, taken));
                }
            }
        });
    }

    public void Masuk() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}