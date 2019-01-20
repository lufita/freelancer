package com.dlmkotlin.freelancer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditJobsActivity extends AppCompatActivity {

    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone";
    private static final String JOBS_NAME_KEY = "JobsName";
    private static final String JOBS_DETAIL_KEY = "JobsDetail";
    private static final String TAKEN = "Taken";
    FirebaseFirestore db;
    TextView textDisplay;
    TextView message;
    String name, email, phone, jobsname, jobsdetail, taken;
    Button save;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jobs);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Intent intent = getIntent();
        EditText temp;
        TextView take;
        save = findViewById(R.id.save);
        temp = findViewById(R.id.name);
        temp.setText(intent.getStringExtra("Name"));
        temp = findViewById(R.id.phone);
        temp.setText(intent.getStringExtra("Phone"));
        temp = findViewById(R.id.jobsname);
        temp.setText(intent.getStringExtra("JobsName"));
        temp = findViewById(R.id.jobsdetail);
        temp.setText(intent.getStringExtra("JobsDetail"));
        take = findViewById(R.id.taken);
        taken = intent.getStringExtra("Taken");
        if (taken.equalsIgnoreCase("")) {
            take.setText("No Yet Taken");
        } else {
            temp.setText(taken);
        }
        textDisplay = findViewById(R.id.textDisplay);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editJobs();
            }
        });
    }

    private void editJobs() {
        EditText temp;
        TextView take;
        save = findViewById(R.id.save);
        temp = findViewById(R.id.name);
        name = temp.getText().toString();
        email = user.getEmail();
        temp = findViewById(R.id.phone);
        phone = temp.getText().toString();
        temp = findViewById(R.id.jobsname);
        jobsname = temp.getText().toString();
        temp = findViewById(R.id.jobsdetail);
        jobsdetail = temp.getText().toString();
        take = findViewById(R.id.taken);
        taken = temp.getText().toString();
        if (taken.equalsIgnoreCase("")) {
            taken = "";
        } else {
            taken = take.getText().toString();
        }
        String Email = email;
        String Jobsname = jobsname;
        Map<String, Object> newJobs = new HashMap<>();
        newJobs.put(NAME_KEY, name);
        newJobs.put(EMAIL_KEY, user.getEmail());
        newJobs.put(PHONE_KEY, phone);
        newJobs.put(JOBS_NAME_KEY, jobsname);
        newJobs.put(JOBS_DETAIL_KEY, jobsdetail);
        newJobs.put(TAKEN, taken);
        try {
            db.collection("JobsList").document(Email + "_" + Jobsname).update(newJobs);
            Toast.makeText(EditJobsActivity.this, "Jobs Edited", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(EditJobsActivity.this, ListJobActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(EditJobsActivity.this, "Jobs Not Edited", Toast.LENGTH_SHORT).show();
        }
    }
}
