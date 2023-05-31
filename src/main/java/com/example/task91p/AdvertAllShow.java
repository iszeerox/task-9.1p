package com.example.task91p;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdvertAllShow extends AppCompatActivity implements SetOnItemClickListener {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private DatabaseClass databaseClass;
    private TextView noData;

    // Create an instance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advert_all__show);

        databaseClass = new DatabaseClass(this);
        recyclerView = findViewById(R.id.lostAndFound);
        noData = findViewById(R.id.noData);
        // Create an instance of the adapter
        adapter = new Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    //It gets the data from the database and submit the data to the adapter for display
    @Override
    protected void onResume() {
        super.onResume();
        final List<Advert> advertList = databaseClass.getData();
        ArrayList<Advert> list = new ArrayList<>(advertList);
        if (list.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }

        adapter.submit(list);
    }
    //It open the to remove or delete
    @Override
    public void onItemClickListener(Advert Advert) {
        Intent intent = new Intent(AdvertAllShow.this, AdvertRemove.class);
        intent.putExtra("id", Advert.getId());
        startActivity(intent);
    }
}
