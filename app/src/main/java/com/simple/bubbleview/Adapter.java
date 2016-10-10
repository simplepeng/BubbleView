package com.simple.bubbleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simple.bubbleviewlibrary.BubbleView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private List<ItemModel> itemModels;

    public Adapter(Context context, List<ItemModel> itemModels) {
        this.context = context;
        this.itemModels = itemModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_recycler, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemModel itemModel = itemModels.get(position);
        holder.tv.setText(itemModel.getMsgCount());
        holder.bubbleView.setVisibility(itemModel.isVisibility() ? View.VISIBLE : View.GONE);
        holder.bubbleView.setText(itemModel.getMsgCount());
        holder.bubbleView.setTextColor(itemModel.getTextColor());
        holder.bubbleView.setCircleColor(itemModel.getCircleColor());
        holder.bubbleView.setOnAnimationEndListener(new BubbleView.OnAnimationEndListener() {
            @Override
            public void onEnd(BubbleView bubbleView) {
                itemModel.setVisibility(false);
                bubbleView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        BubbleView bubbleView;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            bubbleView = (BubbleView) itemView.findViewById(R.id.bubbleView);
        }
    }
}