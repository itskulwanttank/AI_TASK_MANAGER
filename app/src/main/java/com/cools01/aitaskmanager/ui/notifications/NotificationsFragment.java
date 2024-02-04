package com.cools01.aitaskmanager.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.cools01.aitaskmanager.MainTopicAdapter2;
import com.cools01.aitaskmanager.R;

import com.cools01.aitaskmanager.databinding.FragmentNotificationsBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView mainTopicRecyclerView;
    private MainTopicAdapter2 mainTopicAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainTopicRecyclerView = root.findViewById(R.id.mainTopicRecyclerView);
        mainTopicAdapter = new MainTopicAdapter2(new ArrayList<>());

        mainTopicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainTopicRecyclerView.setAdapter(mainTopicAdapter);

        // Set item click listener for main topics
        mainTopicAdapter.setOnItemClickListener((view, position, key) -> {
           // String selectedTopic = key;
            // Load subtopics for the selected topic
           // loadSubtopics(selectedTopic);
            String currentValue = mainTopicAdapter.getItem(position).child("message").getValue(String.class);

            showLongClickAlert(key, currentValue);
        });





        // Load main topics from Firebase or any other data source
        loadMainTopics();

     //   binding.btnAddTopic.setOnClickListener(v -> showAddTopicDialog());

        return root;
    }

    private void showLongClickAlert(String key, String currentValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View customView = getLayoutInflater().inflate(R.layout.custom_alert_layout, null);
        builder.setView(customView);

        TextView titleTextView = customView.findViewById(R.id.alertTitle);
        TextView messageTextView = customView.findViewById(R.id.alertMessage);
        Button okButton = customView.findViewById(R.id.okButton);

        titleTextView.setText("View");
        messageTextView.setText(currentValue);

        AlertDialog alertDialog = builder.create();

        okButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }


    private void loadMainTopics() {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics2");

        // Attach a listener to read the data from Firebase
        topicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
        // Intent intent = new Intent(requireContext(), SubtopicActivity.class);
        // intent.putExtra("selectedTopic", selectedTopic);
        // startActivity(intent);
    }

    private void showAddTopicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Topic");

        final EditText input = new EditText(requireContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Topic");

        final EditText input = new EditText(requireContext());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
