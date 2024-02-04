package com.cools01.aitaskmanager.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

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
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private EditText editTextSearch, editTextAddItem;
    private Button buttonSearch, buttonAddItem;
    private RecyclerView recyclerViewResults;
    private ResultAdapter resultAdapter;
    private List<String> searchResults;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize views
        editTextSearch = root.findViewById(R.id.editTextSearch);
        buttonSearch = root.findViewById(R.id.buttonSearch);
        editTextAddItem = root.findViewById(R.id.editTextAddItem);
        buttonAddItem = root.findViewById(R.id.buttonAddItem);
        recyclerViewResults = root.findViewById(R.id.recyclerViewResults);

        // Set up RecyclerView
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchResults = new ArrayList<>();
        resultAdapter = new ResultAdapter(searchResults);
        recyclerViewResults.setAdapter(resultAdapter);

        // Set up button click listeners
        buttonSearch.setOnClickListener(v -> searchInDatabase());
        buttonAddItem.setOnClickListener(v -> addItemToDatabase());

        return root;
    }

    private void searchInDatabase() {
        String searchText = editTextSearch.getText().toString().toLowerCase();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items");

        Query query = databaseRef.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchResults.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemName = snapshot.child("name").getValue(String.class);
                    if (containsWholeWord(itemName.toLowerCase(), searchText)) {
                        searchResults.add(itemName);
                    }
                }
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private boolean containsWholeWord(String itemName, String searchText) {
        String[] words = itemName.split("\\s+"); // Split into words
        for (String word : words) {
            if (word.equals(searchText)) {
                return true;
            }
        }
        return false;
    }



    private void addItemToDatabase() {
        String newItemName = editTextAddItem.getText().toString();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items");
        String itemId = databaseRef.push().getKey(); // Generate a unique key for the new item

        databaseRef.child(itemId).child("name").setValue(newItemName);

        // Optional: Clear the input field after adding the item
        editTextAddItem.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
