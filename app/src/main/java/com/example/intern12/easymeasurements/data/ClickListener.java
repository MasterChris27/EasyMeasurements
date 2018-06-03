package com.example.intern12.easymeasurements.data;

import android.view.View;

/**
 * Created by intern12 on 16.05.2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
