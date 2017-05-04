package com.gary.blog.Fragment;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gary.blog.Constant;
import com.gary.blog.Data.DataManager;
import com.gary.blog.Data.Post;
import com.gary.blog.Data.User;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.WebService.BaseClient;
import com.gary.blog.Wedgit.CircleImage;
import com.gary.blog.Wedgit.MyRecyclerView;
import com.gary.blog.Wedgit.MyRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/18.
 */

public class FollowedFragment extends Fragment{

    private final static String TAG = "FollowedFragment";

    private MyRefreshLayout refreshLayout;
    private LinearLayout emptyView;
    private MyRecyclerView recyclerView;
    private PostAdapter postAdapter;
    private TextView emptyText;
    private adapterObserver observer;

    private boolean isRefresh;

    private SwipeRefreshLayout.OnRefreshListener listener;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Constant.UpdateRecyclerView:
//                    isRefresh = true;
//                    PostsResponse postsResponse = (PostsResponse)msg.obj;
//                    if (postsResponse != null) {
//                        final ArrayList<Post> posts;
//                        posts = postsResponse.getPosts();
//                        if (posts == null) {
//                            Log.e(TAG, "posts == null");
//                        } else {
//                            DataManager.getInstance(getActivity()).setPosts(posts);
//                            postAdapter.setPosts(posts);
//                            postAdapter.notifyDataSetChanged();
//                        }
//                    }
//                    refreshLayout.setRefreshing(false);
//                    isRefresh = false;
//                    break;
//                case Constant.NetProblem:
//                    refreshLayout.setRefreshing(false);
//                    isRefresh = false;
//                    Toast.makeText(getContext(), "请检查你的网络",
//                            Toast.LENGTH_SHORT);
//                    break;
//                case Constant.FAILURE:
//                    refreshLayout.setRefreshing(false);
//                    isRefresh = false;
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


//    public static PostsFragment getInstance() {
//        if (postsFragment == null) return new PostsFragment();
//        return postsFragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyItemDecoration(6));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
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

    public void updateUI() {

        if (observer == null) {
            observer = new adapterObserver();
        }

        if (postAdapter == null) {
            postAdapter = new PostAdapter();
            postAdapter.registerAdapterDataObserver(observer);
        }

        if (recyclerView != null && recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(postAdapter);
        }

        isRefresh = false;

        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isRefresh) {
//                    Toast.makeText(getActivity(), "正在加载",
//                            Toast.LENGTH_SHORT);
                } else {
                    if (Constant.user != null) {
                        BaseClient.getAbs(Constant.user.getFollowedPostsURL(), null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                ArrayList<Post> posts = null;
                                try {
                                    posts = JsonUtil.getEntityList(response.getString("posts"), Post.class);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (posts == null) {
                                    Log.e(TAG, "posts == null");
                                } else {
                                    DataManager.getInstance(getActivity()).setPosts(posts);
                                    postAdapter.setPosts(posts);
                                    postAdapter.notifyDataSetChanged();
                                }
                                refreshLayout.setRefreshing(false);
                                isRefresh = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                refreshLayout.setRefreshing(false);
                                isRefresh = false;
                                Toast.makeText(getContext(), "请检查你的网络",
                                        Toast.LENGTH_SHORT);
                            }
                        });
                    }
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (NetWorkUtil.isNetWorkOpened(getActivity())) {
//                                if (Constant.user != null) {
//                                    isRefresh = true;
//                                    PostsResponse response = PostsService.getPosts(
//                                            Constant.user.getFollowedPostsURL());
//                                    Message message = new Message();
//                                    message.obj = response;
//                                    message.what = Constant.UpdateRecyclerView;
//                                    handler.sendMessage(message);
//                                } else {
//                                    Message message = new Message();
//                                    message.what = Constant.FAILURE;
//                                    handler.sendMessage(message);
//                                }
//                            } else {
//                                Message message = new Message();
//                                message.what = Constant.NetProblem;
//                                handler.sendMessage(message);
//                            }
//                        }
//                    });
//                    thread.start();
                }
            }
        };

        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(listener);


            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                    listener.onRefresh();
                }
            });
        }



//        observer = new adapterObserver();
//        postAdapter = new PostAdapter(DataManager.getInstance(getActivity()).getPosts());
//        recyclerView.setAdapter(postAdapter);
    }

    private void checkIfEmpty() {
        if (recyclerView.getAdapter() != null) {
            boolean visible = recyclerView.getAdapter().getItemCount() == 0;
            refreshLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
            emptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    //Define PostHolder
    private class PostHolder extends RecyclerView.ViewHolder {

        private TextView postSubject, posterName;
        private CircleImage posterIcon;
        private Post post;
        private User user;


        public PostHolder(View itemView) {
            super(itemView);
            postSubject = (TextView) itemView.findViewById(R.id.followed_subject);
            posterName = (TextView) itemView.findViewById(R.id.followed_username);
            posterIcon = (CircleImage) itemView.findViewById(R.id.followed_icon);
        }

        public void bindPost(final Post post) {
            this.post = post;
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
                    ImageLoader.getInstance().displayImage(user.getGravatarURL(), posterIcon);
                    String subject = "From ";
                    posterName.setText(user.getUsername());
                    if (user.getLocation() == null) {
                        subject += "Unknown Space";
                    } else {
                        subject += user.getLocation();
                    }
                    subject += '\n' + post.getSubject();
                    SpannableStringBuilder style = new SpannableStringBuilder(subject);
                    style.setSpan(new ForegroundColorSpan(Color.GRAY),
                            0, subject.indexOf('\n'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    postSubject.setText(style);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    posterName.setText("Android");
                    String subject = "From Unknown Space\n";
                    SpannableStringBuilder style = new SpannableStringBuilder(subject);
                    style.setSpan(new ForegroundColorSpan(Color.GRAY),
                            0, subject.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    postSubject.setText(style);
                }
            });
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if (NetWorkUtil.isNetWorkOpened(getContext())) {
//                        UserResponse response = UserService.getUsers(post.getAuthorURL());
//                        Message msg = new Message();
//                        msg.obj = response;
//                        msg.what = Constant.UpdateData;
//                        holderHandler.sendMessage(msg);
//                    }
//                }
//            });
//            t.start();
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

    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public MyItemDecoration (int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildPosition(view) != -1) {
                outRect.top = space + 2;
                outRect.right = space;
                outRect.left = space;
            }
        }
    }

    private class adapterObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
//            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
//            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
//            checkIfEmpty();
        }
    }
}
