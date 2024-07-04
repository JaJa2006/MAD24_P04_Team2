package com.example.main_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoViewHolder>{
    ArrayList<Memo> data;
    // Memo adapter to get the data
    public MemoAdapter(ArrayList<Memo> input, Context context) {
        this.data = input;
    }
    // Memo view holder to hold the layout
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.memo_list, parent, false);
        return new MemoViewHolder(item);
    }
    // setting all the buttons of the layout
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        // get the memo data
        Memo memo = data.get(position);
        // check if memo is text or image and set the memo content
        if (memo.isImage.matches("0")) {
            holder.MemoText.setText(memo.MemoContent);
            holder.deleteImage.setVisibility(View.GONE);
            holder.MemoImage.setVisibility(View.GONE);
            holder.MemoImage.setVisibility(View.GONE);
        }else {
            Uri uri;
            uri = Uri.parse(memo.MemoContent);
            holder.Image.setImageURI(uri);
            holder.MemoText.setVisibility(View.GONE);
            holder.deleteText.setVisibility(View.GONE);
        }
        // handle deleting of Memo text
        holder.deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create instant of dbhandler and delete memo from there
                MemoDatabaseHandler dbHandler = new MemoDatabaseHandler(v.getContext(), null, null, 1);
                dbHandler.DeleteMemo(memo);
                // refresh the page
                Context context = holder.deleteText.getContext();
                Intent refresh = new Intent(context, Memo_Page.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(refresh);
                ((Memo_Page)context).finish();
            }
        });
        // handle deleting of Memo image
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create instant of dbhandler and delete memo from there
                MemoDatabaseHandler dbHandler = new MemoDatabaseHandler(v.getContext(), null, null, 1);
                dbHandler.DeleteMemo(memo);
                // refresh the page
                Context context = holder.deleteImage.getContext();
                Intent refresh = new Intent(context, Memo_Page.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(refresh);
                ((Memo_Page)context).finish();
            }
        });
    }
    public int getItemCount() {
        return data.size();
    }
}
