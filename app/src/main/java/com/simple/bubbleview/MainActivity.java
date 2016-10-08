package com.simple.bubbleview;

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

    RecyclerView recyclerView;
    List<ItemModel> itemModels = new ArrayList<>();
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        for (int i = 0; i < 50; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.setMsgCount(String.valueOf(random.nextInt(50)));
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
            holder.tv.setText(itemModels.get(position).getMsgCount());
            holder.bubbleView.setText(itemModels.get(position).getMsgCount());
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

        public String getMsgCount() {
            return msgCount;
        }

        public void setMsgCount(String msgCount) {
            this.msgCount = msgCount;
        }
    }
}
