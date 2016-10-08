package com.simple.bubbleview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simple.bubbleviewlibrary.BubbleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ItemModel> itemModels = new ArrayList<>();
    private Random random = new Random();
    private int[] circleColors = {Color.parseColor("#98F5FF"),Color.parseColor("#87CEFF"),
            Color.parseColor("#8B658B"),Color.parseColor("#B22222")};
    private int[] textColors = {Color.parseColor("#E0FFFF"),Color.parseColor("#FF3030"),
            Color.parseColor("#FF3030"),Color.parseColor("#000000")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        for (int i = 0; i < 50; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.setMsgCount(String.valueOf(random.nextInt(50)));
            itemModel.setCircleColor(circleColors[random.nextInt(circleColors.length)]);
            itemModel.setTextColor(textColors[random.nextInt(textColors.length)]);
            itemModels.add(itemModel);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter());
    }


    class Adapter extends RecyclerView.Adapter<ViewHodler> {

        @Override
        public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.item_recycler, null);
            return new ViewHodler(view);
        }

        @Override
        public void onBindViewHolder(ViewHodler holder, int position) {
            ItemModel itemModel = itemModels.get(position);
            holder.tv.setText(itemModel.getMsgCount());
            holder.bubbleView.setText(itemModel.getMsgCount());
            holder.bubbleView.setTextColor(itemModel.getTextColor());
            holder.bubbleView.setCircleColor(itemModel.getCircleColor());
        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }
    }

    static class ViewHodler extends RecyclerView.ViewHolder {

        private TextView tv;
        private BubbleView bubbleView;

        public ViewHodler(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            bubbleView = (BubbleView) itemView.findViewById(R.id.bubbleView);
        }
    }

    class ItemModel {
        private String msgCount;
        private int textColor;
        private int circleColor;

        public int getCircleColor() {
            return circleColor;
        }

        public void setCircleColor(int circleColor) {
            this.circleColor = circleColor;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public String getMsgCount() {
            return msgCount;
        }

        public void setMsgCount(String msgCount) {
            this.msgCount = msgCount;
        }
    }
}
