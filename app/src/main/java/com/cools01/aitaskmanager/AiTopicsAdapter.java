package com.cools01.aitaskmanager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AiTopicsAdapter extends RecyclerView.Adapter<AiTopicsAdapter.ViewHolder> {
    private List<AiTopics> aiTopicsList;
    private Context context;

    public AiTopicsAdapter(Context context, List<AiTopics> aiTopicsList) {
        this.context = context;
        this.aiTopicsList = aiTopicsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ai_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AiTopics aiTopic = aiTopicsList.get(position);
        holder.topicName.setText(aiTopic.getTopicName());
        holder.topicData.setText(aiTopic.getTopicData());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadAiActivity.class);
                intent.putExtra("topicName", aiTopic.getTopicName());
                intent.putExtra("topicData", aiTopic.getTopicData());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aiTopicsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView topicName , topicData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topicName = itemView.findViewById(R.id.topic_name);
            topicData = itemView.findViewById(R.id.topic_data);
        }
    }
}

