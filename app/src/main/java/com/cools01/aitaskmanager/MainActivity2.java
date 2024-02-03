package com.cools01.aitaskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView mainTopicRecyclerView;
    private MainTopicAdapter mainTopicAdapter;
    private static final String WORK_TAG = "notification_work";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mainTopicRecyclerView = findViewById(R.id.mainTopicRecyclerView);
        mainTopicAdapter = new MainTopicAdapter(new ArrayList<>());

        mainTopicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainTopicRecyclerView.setAdapter(mainTopicAdapter);

        // Set item click listener for main topics
        mainTopicAdapter.setOnItemClickListener((view, position, key) -> {
            String selectedTopic = key;
              String currentValue = mainTopicAdapter.getItem(position).child("message").getValue(String.class);

                        showLongClickAlert(key , currentValue);
                        //Load subtopics for the selected topic

           // loadSubtopics(selectedTopic);
        });
        mainTopicAdapter.setOnLongClickListener((view, position, key) -> {
            // Show an alert box or perform any action on long click
            String currentValue = mainTopicAdapter.getItem(position).child("message").getValue(String.class);

            showLongClickAlert(key , currentValue);
        });

        // Set TextView click listener for main topics
        mainTopicAdapter.setTopicTextViewClickListener((view, position, key) -> {
            // Handle the click on the TextView here
            // String selectedTopic = key;
            // Toast.makeText(MainActivity2.this, "TextView Clicked for " + selectedTopic, Toast.LENGTH_SHORT).show();
        });

        mainTopicAdapter.setImage1ClickListener((view, position, key) -> {
            // Handle the click on the TextView here
            // String selectedTopic = key;
            // Toast.makeText(MainActivity2.this, "Image 1 Clicked for " + selectedTopic, Toast.LENGTH_SHORT).show();
            // Show the delete topic dialog
            showDeleteTopicDialog(position, key);
        });
        mainTopicAdapter.setImage2ClickListener((view, position, key) -> {
            // Handle the click on the TextView here
            String selectedTopic = key;
            String currentValue = mainTopicAdapter.getItem(position).child("message").getValue(String.class);

            // Toast.makeText(MainActivity2.this, "Image 2 Clicked for " + selectedTopic, Toast.LENGTH_SHORT).show();
            showEditTopicDialog(position, key , currentValue);
        });




     

        // Load main topics from Firebase or any other data source
        loadMainTopics();
        findViewById(R.id.btnAddTopic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTopicDialog();

            }
        });

    }



    

   


    private void loadMainTopics() {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");

        // Attach a listener to read the data from Firebase
        topicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   List<String> mainTopics = new ArrayList<>();
                List<DataSnapshot> mainTopics = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mainTopics.add(snapshot);
                }

                // Update the adapter with the retrieved main topics
                mainTopicAdapter.setMainTopics(mainTopics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }



    private void loadSubtopics(String selectedTopic) {
        // Implement loading subtopics logic and launch SubtopicActivity
//        Intent intent = new Intent(MainActivity2.this, SubtopicActivity.class);
//        intent.putExtra("selectedTopic", selectedTopic);
//        startActivity(intent);
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

    }

    // Inside MainActivity class

// ...

    private void showAddTopicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Topic");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newTopic = input.getText().toString().trim();
            if (!TextUtils.isEmpty(newTopic)) {
                addNewTopic(newTopic);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void addNewTopic(String newTopic) {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
        String newTopicKey = topicsRef.push().getKey();

        // Store the topic as a key with an empty value
        topicsRef.child(newTopicKey).child("message").setValue(newTopic);

    }

    private void showDeleteTopicDialog(int position, String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Topic");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Delete the topic from the database and update the UI
            deleteTopic(key, position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void deleteTopic(String key, int position) {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
        topicsRef.child(key).removeValue();

        // Update the UI
        mainTopicAdapter.removeItem(position);
    }
    private void showEditTopicDialog(int position, String key, String currentValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Topic");

        final EditText input = new EditText(this);
        input.setText(currentValue);  // Pre-fill with the current value
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Edit the topic in the database and update the UI
            String editedTopic = input.getText().toString().trim();
            editTopic(key, position, editedTopic);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void editTopic(String key, int position, String editedTopic) {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
        topicsRef.child(key).child("message").setValue(editedTopic);

        // Update the UI
        mainTopicAdapter.editItem(position, editedTopic);
    }

    private void showLongClickAlert(String key, String currentValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.custom_alert_layout, null);
        builder.setView(customView);

        TextView titleTextView = customView.findViewById(R.id.alertTitle);
        TextView messageTextView = customView.findViewById(R.id.alertMessage);
        Button okButton = customView.findViewById(R.id.okButton);

        titleTextView.setText("Long Click Alert");
        messageTextView.setText(currentValue);

        AlertDialog alertDialog = builder.create();

        okButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }




}