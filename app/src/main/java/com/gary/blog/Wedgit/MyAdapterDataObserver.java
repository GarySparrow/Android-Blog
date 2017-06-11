package com.gary.blog.Wedgit;

import android.support.v7.widget.RecyclerView;

/**
 * Created by hasee on 2016/12/18.
 */

public class MyAdapterDataObserver extends RecyclerView.AdapterDataObserver{

    protected void checkIfEmpty() {

    }

    @Override
    public void onChanged() {
        super.onChanged();
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        checkIfEmpty();
    }

}
