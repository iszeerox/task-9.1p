package com.example.task91p;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdvertRemove extends AppCompatActivity {

    private TextView Detail;
    private DatabaseClass databaseClass;
    private TextView Head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advert_remove);
        databaseClass = new DatabaseClass(this);
        Head = findViewById(R.id.head);
        Detail = findViewById(R.id.detail);
        // Get the id from the Database
        int id = getIntent().getIntExtra("id", -1);

        if (id != -1) {
            Advert advert = databaseClass.getDataById(id);
            //Check  the daatbase is empty or not
            if (advert != null) {
                //If Database is not empty then it select the record
                Head.setText(advert.getIsLostOrFound() + ": " + advert.getName());
                StringBuilder detailBuilder = new StringBuilder();
                detailBuilder.append(advert.getDate()).append("\n");
                detailBuilder.append(advert.getLocation()).append("\n");
                detailBuilder.append(advert.getPhone()).append("\n");
                detailBuilder.append(advert.getDescription());
                Detail.setText(detailBuilder.toString());
            }
        }
        Button removeButton = findViewById(R.id.remove);
        //It will delete the data from the database using its ID
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != -1) {
                    databaseClass.deleteDataById(id);
                    finish();
                }
            }
        });
    }
}
