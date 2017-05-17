package com.gary.blog.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gary.blog.Constant;
import com.gary.blog.Data.Comment;
import com.gary.blog.R;
import com.gary.blog.Utils.NetWorkUtil;
import com.gary.blog.Wedgit.MyAdapterDataObserver;
import com.gary.blog.Wedgit.MyRecyclerView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/18.
 */

public class CommentsFragment extends Fragment{

    private final static String TAG = "CommentsFragment";

    private PullToRefreshView refreshLayout;
//    private LinearLayout emptyView;
    private MyRecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private AdapterObserver adapterObserver;

//    private boolean isRefresh;

    private PullToRefreshView.OnRefreshListener refreshListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.UpdateRecyclerView:
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        refreshLayout = (PullToRefreshView) view.findViewById(R.id.refresh_layout);
//        emptyView = (LinearLayout) view.findViewById(R.id.empty_view);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.posts_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_posts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //custom methods

    public void init() {
        adapterObserver = new AdapterObserver();
        commentAdapter = new CommentAdapter();
        commentAdapter.registerAdapterDataObserver(adapterObserver);
        recyclerView.setAdapter(commentAdapter);
        //        isRefresh = false;
        refreshListener = new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (refreshLayout.isRefreshing()) {
//                    Toast.makeText(getActivity(), "正在加载",
//                            Toast.LENGTH_SHORT);
//                } else {
                if (NetWorkUtil.isNetWorkOpened(getContext())) {

                } else {
                    refreshLayout.setRefreshing(false);
                }
            }
        };
        refreshLayout.setOnRefreshListener(refreshListener);

    }

    //Define CommentHolder
    private class CommentHolder extends RecyclerView.ViewHolder {

        private Comment comment;

        public CommentHolder(View itemView) {
            super(itemView);
        }

        public void bindComment(final Comment comment) {

        }

    }


    //Define CommentAdapter
    private class CommentAdapter extends RecyclerView.Adapter<CommentsFragment.CommentHolder> {

        private ArrayList<Comment> comments;

        public CommentAdapter() {
            comments = new ArrayList<>();
        }


        @Override
        public CommentsFragment.CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(
                    R.layout.list_item_comment, parent, false);

            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentHolder holder, int position) {
            final Comment comment = comments.get(position);

            holder.bindComment(comment);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public void setComments(ArrayList<Comment> comments) {
            this.comments.clear();
            this.comments.addAll(comments);
        }

    }

    private class AdapterObserver extends MyAdapterDataObserver {

        @Override
        protected void checkIfEmpty() {
            if (recyclerView.getAdapter() != null) {
                boolean visible = recyclerView.getAdapter().getItemCount() == 0;
                recyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
//                emptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }

    }
}
