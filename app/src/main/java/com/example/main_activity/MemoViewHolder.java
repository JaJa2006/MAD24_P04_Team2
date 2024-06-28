package com.example.main_activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

// viewholder for the recycler view containing the memo
public class MemoViewHolder extends RecyclerView.ViewHolder{
    TextView MemoText;
    ImageView deleteText;
    TextView MemoImage;
    ImageView deleteImage;
    ImageView Image;
    public MemoViewHolder(View itemView) {
        super(itemView);
        MemoText = itemView.findViewById(R.id.tvMemoText);
        deleteText = itemView.findViewById(R.id.tvDeleteText);
        MemoImage = itemView.findViewById(R.id.tvMemoImage);
        deleteImage = itemView.findViewById(R.id.tvDeleteImage);
        Image = itemView.findViewById(R.id.tvImage);
    }
}
