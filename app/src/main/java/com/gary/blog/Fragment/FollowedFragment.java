package com.gary.blog.Fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gary.blog.Activity.PostActivity;
import com.gary.blog.Activity.UserInfoActivity;
import com.gary.blog.Constant;
import com.gary.blog.Data.Post;
import com.gary.blog.Data.User;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.Utils.NetWorkUtil;
import com.gary.blog.WebService.BaseClient;
import com.gary.blog.Wedgit.CircleImage;
import com.gary.blog.Wedgit.CubeLayout;
import com.gary.blog.Wedgit.MyLinearLayout;
import com.gary.blog.Wedgit.MyRecyclerView;
import com.gary.blog.Wedgit.MyRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.gary.blog.Constant.POSTS;
import static com.gary.blog.Constant.bkDic;

/**
 * Created by hasee on 2017/5/5.
 */

public class FollowedFragment extends Fragment{

    private final static String TAG = "FollowedFragment";

    private MyRefreshLayout refreshLayout;
    private MyRecyclerView recyclerView;
    private PostAdapter postAdapter;
    private adapterObserver observer;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        refreshLayout = (MyRefreshLayout) view.findViewById(R.id.refresh_layout);
//        refreshLayout.setBackgroundColor(getResources().getColor(R.color.lightgray));
//        emptyView = (LinearLayout) view.findViewById(R.id.empty_view);
//        emptyText = (TextView) view.findViewById(R.id.empty_text);
//        if (user == null) {
//            emptyText.setText("please login first...");
//        } else {
//            emptyText.setText("refreshing...");
//        }
        recyclerView = (MyRecyclerView) view.findViewById(R.id.posts_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
//        StaggeredGridLayoutManager layoutManager = new
//                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return (int) Math.ceil(DataManager.getInstance(getActivity()).getPosts()
//                        .get(position).getSubject().length() / 32.0f);
//            }
//        });

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position % 3 == 0 ? 3 : 2;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//            DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyItemDecoration(12));

        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {

        if (Constant.user == null) {
            return ;
        }

        observer = new adapterObserver();
        postAdapter = new PostAdapter();
        postAdapter.registerAdapterDataObserver(observer);
        recyclerView.setAdapter(postAdapter);
        //        isRefresh = false;
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (refreshLayout.isRefreshing()) {
//                    Toast.makeText(getActivity(), "正在加载",
//                            Toast.LENGTH_SHORT);
//                } else {
                if (NetWorkUtil.isNetWorkOpened(getContext())) {
                    BaseClient.get(POSTS, null, new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
//                                refreshLayout.setRefreshing(true);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            ArrayList<Post> posts = null;
                            try {
                                posts = JsonUtil.getEntityList(
                                        response.getString("posts"), Post.class);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (posts == null) {
                                Log.e(TAG, "posts == null");
                            } else {
                                postAdapter.setPosts(posts);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
//                            Toast.makeText(getContext(), "请检查你的网络",
//                                    Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        };
        refreshLayout.setOnRefreshListener(refreshListener);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshListener.onRefresh();
            }
        });
    }

//Define PostHolder
private class PostHolder extends RecyclerView.ViewHolder {

    private TextView postSubject, posterName;
    private CircleImage posterIcon;
    private CardView cardView;
    private MyLinearLayout postLayout, posterLayout, commentLayout;
    private CubeLayout cubeLayout;
    private User user;


    public PostHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_layout);
        postSubject = (TextView) itemView.findViewById(R.id.followed_subject);
        posterName = (TextView) itemView.findViewById(R.id.followed_username);
        posterIcon = (CircleImage) itemView.findViewById(R.id.followed_icon);
        cubeLayout = (CubeLayout) itemView.findViewById(R.id.cube_layout);
        posterLayout = (MyLinearLayout) itemView.findViewById(R.id.poster_info);
        postLayout = (MyLinearLayout) itemView.findViewById(R.id.post_info);
        commentLayout = (MyLinearLayout) itemView.findViewById(R.id.comment_info);
    }

    public void bindPost(final Post post) {
        if (post == null) {
            return ;
        }
        BaseClient.getAbs(post.getAuthorURL(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    user = JsonUtil.getEntity(response.getString("user"), User.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int idx = user.getUsername().charAt(user.getUsername().length() - 1);
                idx = (idx % bkDic.length);
//                cardView.setBackground(getActivity().getResources().getDrawable(
//                        bkDic[idx]));
                for (int i = 0; i < cubeLayout.getChildCount(); i++) {
                    View v = cubeLayout.getChildAt(i);
//                    v.setBackground(getActivity().getResources().getDrawable(
//                            bkDic[idx]));
                    v.setBackground(getActivity().getResources().getDrawable(
                            R.drawable.pure_c1));
                }

                ImageLoader.getInstance().displayImage(user.getGravatarURL(), posterIcon);

                posterName.setText(user.getUsername());

                posterLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = UserInfoActivity.newIntent(getActivity());
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                });
//                if (user.getLocati+on() == null) {
//                    subject += "Unknown Space";
//                } else {
//                    subject += user.getLocation();
//                }

//                SpannableStringBuilder style = new SpannableStringBuilder(subject);
//                style.setSpan(new ForegroundColorSpan(Color.GRAY),
//                        0, subject.indexOf('\n'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                posterName.setText("Anonymous");
                String subject = "";
//                SpannableStringBuilder style = new SpannableStringBuilder(subject);
//                style.setSpan(new ForegroundColorSpan(Color.GRAY),
//                        0, subject.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                postSubject.setText(subject);
            }
        });

        String subject = "";
        subject = post.getSubject();
        postSubject.setText(subject);
        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PostActivity.newIntent(getActivity());
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PostActivity.newIntent(getActivity());
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}


//Define PostAdapter
private class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    private ArrayList<Post> posts;

    public PostAdapter() {
        posts = new ArrayList<>();
    }


    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(
                R.layout.list_item_followed, parent, false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        final Post post = posts.get(position);
        holder.bindPost(post);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

}

private class MyItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public MyItemDecoration (int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildPosition(view) != -1) {
            outRect.top = space;
            outRect.right = space;
            outRect.left = space;
            outRect.bottom = space;
        }
    }
}

private class adapterObserver extends RecyclerView.AdapterDataObserver {
    @Override
    public void onChanged() {
        super.onChanged();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
        }
    }
}
