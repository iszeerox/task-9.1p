package com.example.task91p;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createNewAdvertButton = findViewById(R.id.newAdvertButton);
        createNewAdvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OnCreateAdvert.class));
            }
        });

        Button showAllItemsButton = findViewById(R.id.showAllButton);
        showAllItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdvertAllShow.class));
            }
        });

        Button showOnMapsButton = findViewById(R.id.showOnMapsButton);
        showOnMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllLocationOnMaps.class));
            }
        });
    }
}
