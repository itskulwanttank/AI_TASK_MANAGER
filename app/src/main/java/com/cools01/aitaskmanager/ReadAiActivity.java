package com.cools01.aitaskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ReadAiActivity extends AppCompatActivity {
    private TextView topicTextView;
    private String topicData;
    private int index = 0;
    private long delay = 1; // Milliseconds per character

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_ai);
        topicTextView = findViewById(R.id.topic_text_view);

        // Get topicData from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            topicData = extras.getString("topicData");
            animateText();
        }
    }

    private void animateText() {
        if (topicData != null && !topicData.isEmpty()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index <= topicData.length()) {
                        topicTextView.setText(topicData.substring(0, index++));
                        handler.postDelayed(this, delay);
                    }
                }
            }, delay);
        }
    }
}

