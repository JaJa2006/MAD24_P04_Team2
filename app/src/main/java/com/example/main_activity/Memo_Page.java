package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Memo_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView addMemo = findViewById(R.id.tvAddMemo);
        ImageView ivBack = findViewById(R.id.ivMemoBack);
        // button to go to the create memo page
        addMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createMemo = new Intent(v.getContext(), CreateMemo.class);
                v.getContext().startActivity(createMemo);
            }
        });
        // back button to go back to the main activity page
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // update the page with the data base when the page is reloaded
    @Override
    protected void onResume() {
        super.onResume();
        // get the memo from the data base
        MemoDatabaseHandler dbHandler = new MemoDatabaseHandler(Memo_Page.this, null, null, 1);
        ArrayList<Memo> memos = dbHandler.getMemo();
        // get the recyclerview from the XML
        RecyclerView recyclerView = findViewById(R.id.rvMemo);
        // fill the layout with the information from the data base
        MemoAdapter mAdapter = new MemoAdapter(memos,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}