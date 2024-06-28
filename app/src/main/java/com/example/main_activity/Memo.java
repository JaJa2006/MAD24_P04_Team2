package com.example.main_activity;

import java.util.ArrayList;
// class for storing memo
public class Memo {
    public String MemoContent;
    public String isImage;
    public int MemoID;
    public Memo() {
    }
    public Memo(String MemoContent,String isImage) {
        this.MemoContent = MemoContent;
        this.isImage = isImage;
    }
    public Memo(String MemoContent,String isImage, int MemoID) {
        this.MemoContent = MemoContent;
        this.isImage = isImage;
        this.MemoID = MemoID;
    }
}
