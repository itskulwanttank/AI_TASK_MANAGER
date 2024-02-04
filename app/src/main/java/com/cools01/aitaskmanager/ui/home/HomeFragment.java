package com.cools01.aitaskmanager.ui.home;

import android.app.AlertDialog;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cools01.aitaskmanager.MainTopicAdapter;
import com.cools01.aitaskmanager.R;
import com.cools01.aitaskmanager.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView mainTopicRecyclerView;
    private MainTopicAdapter mainTopicAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        mainTopicRecyclerView = root.findViewById(R.id.mainTopicRecyclerView);
        mainTopicAdapter = new MainTopicAdapter(new ArrayList<>());

        mainTopicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainTopicRecyclerView.setAdapter(mainTopicAdapter);

        // Set item click listener for main topics
        mainTopicAdapter.setOnItemClickListener((view, position, key) -> {
            String selectedTopic = key;
            String currentValue = mainTopicAdapter.getItem(position).child("message").getValue(String.class);

            showLongClickAlert(key, currentValue);
            // Load subtopics for the selected topic
            // loadSubtopics(selectedTopic);
        });

        // Load main topics from Firebase or any other data source
        loadMainTopics();

        root.findViewById(R.id.btnAddTopic).setOnClickListener(v -> showAddTopicDialog());

        return root;
    }

    private void loadMainTopics() {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");

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

    private void showAddTopicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Topic");

        final EditText input = new EditText(getActivity());
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

    private void showLongClickAlert(String key, String currentValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
