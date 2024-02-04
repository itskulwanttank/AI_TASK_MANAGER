package com.cools01.aitaskmanager;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<String> results;

    public ResultAdapter(List<String> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        String result = results.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewResult;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewResult = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String result) {
            textViewResult.setText(result);
        }
    }
}
