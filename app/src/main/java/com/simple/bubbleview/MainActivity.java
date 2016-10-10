package com.simple.bubbleview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ItemModel> itemModels = new ArrayList<>();
    private Random random = new Random();
    private int[] circleColors = {Color.RED, Color.parseColor("#98F5FF"), Color.parseColor("#87CEFF"),
            Color.parseColor("#8B658B"), Color.parseColor("#B22222")};
    private int[] textColors = {Color.BLACK, Color.WHITE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        for (int i = 0; i < 50; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.setMsgCount(String.valueOf(i));
            itemModel.setCircleColor(circleColors[random.nextInt(circleColors.length)]);
            itemModel.setTextColor(textColors[random.nextInt(textColors.length)]);
            itemModels.add(itemModel);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(MainActivity.this, itemModels));
    }

}
