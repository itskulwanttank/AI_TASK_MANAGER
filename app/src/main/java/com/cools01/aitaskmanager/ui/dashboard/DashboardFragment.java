package com.cools01.aitaskmanager.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import com.cools01.aitaskmanager.AiTopics;
import com.cools01.aitaskmanager.AiTopicsAdapter;
import com.cools01.aitaskmanager.R;
import com.cools01.aitaskmanager.ResultAdapter;
import com.cools01.aitaskmanager.databinding.FragmentDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private RecyclerView recyclerView;
    private AiTopicsAdapter adapter;
    private List<AiTopics> aiTopicsList;
    private  Button refreshButton;
    private ProgressBar progressBar ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.preTopicRecyclerView);
         refreshButton = root.findViewById(R.id.refreshButton);
         progressBar = root.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        aiTopicsList = new ArrayList<>();
        adapter = new AiTopicsAdapter(getContext(), aiTopicsList);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("ai_topics")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        aiTopicsList.clear();
                        List<AiTopics> allTopics = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            AiTopics aiTopic = dataSnapshot.getValue(AiTopics.class);
                            allTopics.add(aiTopic);
                        }

                        // Shuffle the list of topics
                        Collections.shuffle(allTopics);

                        // Add the first 5 topics to your aiTopicsList
                        int count = Math.min(allTopics.size(), 5);
                        for (int i = 0; i < count; i++) {
                            aiTopicsList.add(allTopics.get(i));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });



        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Start a delayed task to hide progress bar after 2 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        // Refresh topics
                        refreshTopics();
                    }
                }, 1000); // 1 seconds delay
            }
        });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Method to refresh topics
    private void refreshTopics() {
        FirebaseDatabase.getInstance().getReference().child("ai_topics")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        aiTopicsList.clear();
                        List<AiTopics> allTopics = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            AiTopics aiTopic = dataSnapshot.getValue(AiTopics.class);
                            allTopics.add(aiTopic);
                        }

                        // Shuffle the list of topics
                        Collections.shuffle(allTopics);

                        // Add the first 5 topics to your aiTopicsList
                        int count = Math.min(allTopics.size(), 5);
                        for (int i = 0; i < count; i++) {
                            aiTopicsList.add(allTopics.get(i));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }
}
