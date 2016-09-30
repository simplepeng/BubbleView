package com.simple.bubbleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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

        recyclerView.setAdapter(new Adapter());
    }


    class Adapter extends RecyclerView.Adapter<ViewHodler> {


        @Override
        public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHodler holder, int position) {

        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }
    }

    class ViewHodler extends RecyclerView.ViewHolder {

        public ViewHodler(View itemView) {
            super(itemView);
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
