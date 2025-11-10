package com.adivya.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        TextView callerNameTextView = findViewById(R.id.caller_name_text_view);
        TextView callerNumberTextView = findViewById(R.id.caller_number_text_view);
        FloatingActionButton endCallFab = findViewById(R.id.end_call_fab);

        String name = getIntent().getStringExtra("NAME");
        String number = getIntent().getStringExtra("NUMBER");

        callerNameTextView.setText(name);
        callerNumberTextView.setText(number);

        endCallFab.setOnClickListener(v -> {
            // End the call
            finish();
        });

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
