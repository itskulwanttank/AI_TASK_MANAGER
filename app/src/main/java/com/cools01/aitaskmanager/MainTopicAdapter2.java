package com.cools01.aitaskmanager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class MainTopicAdapter2 extends RecyclerView.Adapter<MainTopicAdapter2.ViewHolder> {
    private List<DataSnapshot> mainTopics;  // Change the type to DataSnapshot
    private OnItemClickListener itemClickListener;

    public MainTopicAdapter2(List<DataSnapshot> mainTopics) {
        this.mainTopics = mainTopics;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String key);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_topic2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataSnapshot snapshot = mainTopics.get(position);
        String topicKey = snapshot.getKey();  // Get the key
        String topicValue = snapshot.child("message").getValue(String.class);  // Get the value

        holder.topicTextView.setText(topicValue);



        // Set an OnClickListener for the TextView
        holder.topicTextView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, position, topicKey);  // Send the key to the listener
            }
        });


        // Set an OnClickListener for the entire item (if needed)
        holder.itemView.setOnClickListener(v -> {
            // You can perform a different action for the entire item if needed
        });


    }

    @Override
    public int getItemCount() {
        return mainTopics.size();
    }

    public DataSnapshot getItem(int position) {
        return mainTopics.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topicTextView;
        ImageView imageViewDelete , imageViewEdit  ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topicTextView = itemView.findViewById(R.id.topicTextView);
            imageViewDelete = itemView.findViewById(R.id.editDelete);
            imageViewEdit = itemView.findViewById(R.id.editImage);

        }
    }

    // Inside MainTopicAdapter class

// ...

    public void addItem(DataSnapshot newTopic) {  // Change the type to DataSnapshot
        mainTopics.add(newTopic);
        notifyDataSetChanged();
    }
// Inside MainTopicAdapter class

// ...

    public void removeItem(int position) {
        mainTopics.remove(position);
        notifyDataSetChanged();
    }

    public void setMainTopics(List<DataSnapshot> mainTopics) {
        this.mainTopics = mainTopics;
        notifyDataSetChanged();
    }

    public void editItem(int position, String editedValue) {
        mainTopics.get(position).child("message").getRef().setValue(editedValue);
        notifyDataSetChanged();
    }
}



