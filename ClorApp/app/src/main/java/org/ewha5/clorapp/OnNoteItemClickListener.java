package org.ewha5.clorapp;

import android.view.View;

public interface OnNoteItemClickListener {
    public void onItemClick(ClorAdapter.ViewHolder holder, View view, int position);
}
