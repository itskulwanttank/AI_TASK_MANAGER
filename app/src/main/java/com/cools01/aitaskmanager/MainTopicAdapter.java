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

public class MainTopicAdapter extends RecyclerView.Adapter<MainTopicAdapter.ViewHolder> {
    private List<DataSnapshot> mainTopics;  // Change the type to DataSnapshot
    private OnItemClickListener itemClickListener;

    public MainTopicAdapter(List<DataSnapshot> mainTopics) {
        this.mainTopics = mainTopics;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String key);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public interface OnTopicTextViewClickListener {
        void onTopicTextViewClick(View view, int position, String key);
    }
    public interface OnImage1ClickListener {
        void onImage1Click(View view, int position, String key);
    }

    public interface OnImage2ClickListener {
        void onImage2Click(View view, int position, String key);
    }


    private OnTopicTextViewClickListener topicTextViewClickListener;
    private OnImage1ClickListener image1ClickListener;
    private OnImage2ClickListener image2ClickListener;
    public interface OnLongClickListener {
        void onLongClick(View view, int position, String key);
    }

    private OnLongClickListener longClickListener;

    public void setOnLongClickListener(OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setTopicTextViewClickListener(OnTopicTextViewClickListener listener) {
        this.topicTextViewClickListener = listener;
    }
    public void setImage1ClickListener(OnImage1ClickListener listener) {
        this.image1ClickListener = listener;
    }

    public void setImage2ClickListener(OnImage2ClickListener listener) {
        this.image2ClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_topic, parent, false);
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

        // Set an OnClickListener for Image1
        holder.imageViewDelete.setOnClickListener(v -> {
            if (image1ClickListener != null) {
                image1ClickListener.onImage1Click(v, position, topicKey);  // Send the key to the listener
            }
        });

        // Set an OnClickListener for Image2
        holder.imageViewEdit.setOnClickListener(v -> {
            if (image2ClickListener != null) {
                image2ClickListener.onImage2Click(v, position, topicKey);  // Send the key to the listener
            }
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


