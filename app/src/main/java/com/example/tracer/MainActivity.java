package com.example.tracer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvwelcome;

    private Button btnsms, btnnotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvwelcome = findViewById(R.id.tvwelcome);
        btnsms = findViewById(R.id.btnsms);
        btnnotification = findViewById(R.id.btnnotification);

        btnsms.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SmsView.class));
        });

        btnnotification.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, NotificationView.class));
        });
    }
}