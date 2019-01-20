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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListJobActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    JobsAdapter adapter;
    ArrayList<Jobs> JobsArrayList;
    Button logoutB;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_job);

        db = FirebaseFirestore.getInstance();
        addData();
        mAuth = FirebaseAuth.getInstance();
        mSwipeRefreshLayout = findViewById(R.id.swiper);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                RefreshRecycle();
                addData();
            }
        });
        RefreshRecycle();

        logoutB = (Button) findViewById(R.id.LogoutButton);
        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == logoutB){
                    logout();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        AddJobsActivity.class));
            }
        });
    }

    public void RefreshRecycle(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new JobsAdapter(JobsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListJobActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public void addData(){
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

    private void logout(){
        if(mAuth.getCurrentUser().getEmail().isEmpty() == false){
            mAuth.signOut();
            Intent i = new Intent(ListJobActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Toast.makeText(ListJobActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    }

}
