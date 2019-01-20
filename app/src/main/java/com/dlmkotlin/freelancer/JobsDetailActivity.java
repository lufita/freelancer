package com.dlmkotlin.freelancer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JobsDetailActivity extends AppCompatActivity {

    String name, phone, email, jobsname, jobsdetail, taken;
    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone";
    private static final String JOBS_NAME_KEY = "JobsName";
    private static final String JOBS_DETAIL_KEY = "JobsDetail";
    private static final String TAKEN = "Taken";
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button  buttont, buttond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_detail);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Intent intent = getIntent();
        final String vname = intent.getStringExtra("Name");
        final String vjobsName = intent.getStringExtra("JobsName");
        final String vjobsDetail = intent.getStringExtra("JobsDetail");
        final String vemail = intent.getStringExtra("Email");
        final String vphone = intent.getStringExtra("Phone");
        final String vtaken = intent.getStringExtra("Taken");
        try {
            if (user.getEmail().equalsIgnoreCase(vemail)) {
                setContentView(R.layout.activity_jobs_detail_edit);
                buttond = (Button) findViewById(R.id.Delete);
                buttond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == buttond) {
                            Delete(vname, vjobsName, vjobsDetail, vemail, vphone);
                        }
                    }
                });
                buttont = (Button) findViewById(R.id.Edit);
                buttont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == buttont) {
                            Intent intent = new Intent(v.getContext(), EditJobsActivity.class);
                            intent.putExtra("Name", vname);
                            intent.putExtra("Phone", vphone);
                            intent.putExtra("Email", vemail);
                            intent.putExtra("JobsName", vjobsName);
                            intent.putExtra("JobsDetail", vjobsDetail);
                            intent.putExtra("Taken", vtaken);
                            v.getContext().startActivity(intent);
                        }
                    }
                });
            } else if (user.getEmail().equalsIgnoreCase(vtaken)) {
                setContentView(R.layout.activity_jobs_detail_untaken);
                buttont = (Button) findViewById(R.id.Untaken);
                buttont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == buttont) {
                            Untake(vname, vjobsName, vjobsDetail, vemail, vphone);
                        }
                    }
                });
            } else if (user.getEmail().isEmpty() == false && vtaken.isEmpty() == true) {
                setContentView(R.layout.activity_jobs_detail);
                buttont = (Button) findViewById(R.id.Taken);
                buttont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == buttont) {
                            Taken(vname, vjobsName, vjobsDetail, vemail, vphone);
                        }
                    }
                });
            } else {
                setContentView(R.layout.activity_jobs_detail_nonlogin);
            }
        }
        catch (Exception e){
            setContentView(R.layout.activity_jobs_detail_nonlogin);
        }
        TextView tname = (TextView) findViewById(R.id.Name);
        tname.setText(vname);
        TextView tjobsname = (TextView) findViewById(R.id.JobsName);
        tjobsname.setText(vjobsName);
        TextView tjobsdetail = (TextView) findViewById(R.id.JobsDetail);
        tjobsdetail.setText(vjobsDetail);
        TextView tjobsemail = (TextView) findViewById(R.id.EmailDetail);
        tjobsemail.setText(vemail);
        TextView tjobsphone = (TextView) findViewById(R.id.PhoneDetail);
        tjobsphone.setText(vphone);

    }

    public void callNumber(View view) {
        TextView edtPhone = (TextView) findViewById(R.id.PhoneDetail);
        Uri uri = Uri.parse("tel:"+edtPhone.getText().toString());
        Intent in = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(in);
    }

    public void showEmail(View view) {
        TextView edtEmail = (TextView) findViewById(R.id.EmailDetail);
        Uri uri = Uri.parse("mailto:"+edtEmail.getText().toString());
        Intent in = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(in);
    }

    private void Taken(String Name, String JobsName, String JobsDetail, String Email, String Phone){
         Map<String, Object> newJobs = new HashMap<>();
        newJobs.put(NAME_KEY, Name);
        newJobs.put(EMAIL_KEY, Email);
        newJobs.put(PHONE_KEY, Phone);
        newJobs.put(JOBS_NAME_KEY, JobsName);
        newJobs.put(JOBS_DETAIL_KEY, JobsDetail);
        newJobs.put(TAKEN, user.getEmail());
        try {
            db.collection("JobsList").document(Email + "_" +JobsName).update(newJobs);
            Toast.makeText(JobsDetailActivity.this, "Jobs Taken", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(JobsDetailActivity.this, ListJobActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        catch(Exception e){
            Toast.makeText(JobsDetailActivity.this, "Fail to take Jobs", Toast.LENGTH_SHORT).show();
        }
    }

    public void Untake(String Name, String JobsName, String JobsDetail, String Email, String Phone){
        //Toast.makeText(JobsDetailActivity.this, Name + Email + Phone + JobsName + JobsDetail + user.getEmail(), Toast.LENGTH_SHORT).show();
        Map<String, Object> newJobs = new HashMap<>();
        newJobs.put(NAME_KEY, Name);
        newJobs.put(EMAIL_KEY, Email);
        newJobs.put(PHONE_KEY, Phone);
        newJobs.put(JOBS_NAME_KEY, JobsName);
        newJobs.put(JOBS_DETAIL_KEY, JobsDetail);
        newJobs.put(TAKEN, "");
        try {
            db.collection("JobsList").document(Email + "_" +JobsName).update(newJobs);
            Toast.makeText(JobsDetailActivity.this, "Jobs Untaken", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(JobsDetailActivity.this, ListJobActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        catch(Exception e){
            Toast.makeText(JobsDetailActivity.this, "Fail to untake Jobs", Toast.LENGTH_SHORT).show();
        }
    }

    public void Delete(String Name, String JobsName, String JobsDetail, String Email, String Phone){
        //Toast.makeText(JobsDetailActivity.this, Name + Email + Phone + JobsName + JobsDetail + user.getEmail(), Toast.LENGTH_SHORT).show();
        Map<String, Object> newJobs = new HashMap<>();
        newJobs.put(NAME_KEY, Name);
        newJobs.put(EMAIL_KEY, Email);
        newJobs.put(PHONE_KEY, Phone);
        newJobs.put(JOBS_NAME_KEY, JobsName);
        newJobs.put(JOBS_DETAIL_KEY, JobsDetail);
        try {
            db.collection("JobsList").document(Email + "_" +JobsName).delete();
            Toast.makeText(JobsDetailActivity.this, "Jobs " + JobsName + " Deleted", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(JobsDetailActivity.this, ListJobActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        catch(Exception e){
            Toast.makeText(JobsDetailActivity.this, "Fail to delete Jobs", Toast.LENGTH_SHORT).show();
        }
    }
}
