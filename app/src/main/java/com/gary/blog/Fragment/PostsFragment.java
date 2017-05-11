package com.gary.blog.Fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.ijkplayer.widget.PlayerView;
import com.gary.blog.Activity.PostActivity;
import com.gary.blog.Constant;
import com.gary.blog.Data.DataManager;
import com.gary.blog.Data.Post;
import com.gary.blog.Data.User;
import com.gary.blog.Interface.onRefreshListener;
import com.gary.blog.R;
import com.gary.blog.Utils.ACache;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.Utils.NetWorkUtil;
import com.gary.blog.WebService.BaseClient;
import com.gary.blog.Wedgit.CircleImage;
import com.gary.blog.Wedgit.MyRecyclerView;
import com.gary.blog.Wedgit.MyRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.gary.blog.Constant.ISLIKED;
import static com.gary.blog.Constant.LIKE;
import static com.gary.blog.Constant.LIKE_COUNT;
import static com.gary.blog.Constant.POSTS;
import static com.gary.blog.Constant.UNLIKE;
import static com.gary.blog.Constant.psdf;
import static com.gary.blog.Constant.sdf;

/**
 * Created by hasee on 2016/12/9.
 */

public class PostsFragment extends Fragment implements onRefreshListener {

//    private static PostsFragment postsFragment;

    private final static String TAG = "PostsFragment";

    private MyRefreshLayout refreshLayout;
    private LinearLayout emptyView;
    private TextView emptyText;
    private MyRecyclerView recyclerView;
    private PostAdapter postAdapter;
    private adapterObserver observer;
    private ACache aCache;
//    private ImageView launchImg;

//    private boolean isRefresh;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;

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
//                    Toast.makeText(getContext(), "请检查你的网络",
//                            Toast.LENGTH_SHORT);
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
    public void onRefresh() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshListener.onRefresh();
            }

        });
    }

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
//        emptyView = (LinearLayout) view.findViewById(R.id.empty_view);
//        emptyText = (TextView) view.findViewById(R.id.empty_text);
//        emptyText.setText("refreshing...");
        recyclerView = (MyRecyclerView) view.findViewById(R.id.posts_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyItemDecoration(6));
//        launchImg = (ImageView) getActivity().findViewById(R.id.launch_img);

        aCache = ACache.get(getActivity());

        JSONObject response = aCache.getAsJSONObject(TAG);
        if (response != null) {
            try {
                ArrayList<Post> posts = JsonUtil.getEntityList(
                        response.getString("posts"), Post.class);
                DataManager.getInstance(getActivity()).setPosts(posts);
                postAdapter.setPosts(posts);
                postAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        init();
        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
//                refreshLayout.setRefreshing(true);
                refreshListener.onRefresh();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_posts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //custom methods

    private void init() {
        observer = new adapterObserver();
        postAdapter = new PostAdapter(DataManager.getInstance(getActivity())
            .getPosts());
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
                                DataManager.getInstance(getActivity()).setPosts(posts);
                                postAdapter.setPosts(posts);
                                aCache.put("TAG", response);
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
//                            launchImg.setVisibility(View.GONE);
//                                isRefresh = false;
                        }
                    });
                }



//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                        if (NetWorkUtil.isNetWorkOpened(getActivity())) {
//                            isRefresh = true;
//                            PostsResponse response = PostsService.getPosts();
//                            Message message = new Message();
//                            message.obj = response;
//                            message.what = Constant.UpdateRecyclerView;
//                            handler.sendMessage(message);
//                        } else {
//                            Message message = new Message();
//                            message.what = Constant.NetProblem;
//                            handler.sendMessage(message);
//                        }
//                        }
//                    });
//                    thread.start();
//                }
            }
        };
        refreshLayout.setOnRefreshListener(refreshListener);

//        refreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
////                refreshLayout.setRefreshing(true);
//                listener.onRefresh();
//            }
//        });
    }

    private void updateUI() {
//
//        if (observer == null) {
//
//        }
//
//        if (postAdapter == null) {
//
//        }
//
//        if (recyclerView != null && recyclerView.getAdapter() == null) {
//
//        }


//        if (refreshLayout != null) {
//
//        }



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

        private FrameLayout hintLayout;
        private ImageView imgFirst;
        private LinearLayout dataLayout;
        private ShineButton likeState;
//        private IjkVideoView video;
        private PlayerView video;
        private RelativeLayout videoView;
        private TextView postSubject, posterName, postDate, posterLoc, hintText, likeSum;
        private CircleImage posterIcon;
        private boolean mliked;
        private Post post;

//        private Handler holderHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//
//                    default:
//                        break;
//                }
//            }
//        };

        public PostHolder(View itemView) {
            super(itemView);
            postSubject = (TextView) itemView.findViewById(R.id.post_subject);
            posterName = (TextView) itemView.findViewById(R.id.poster_name);
            postDate = (TextView) itemView.findViewById(R.id.post_date);
            posterIcon = (CircleImage) itemView.findViewById(R.id.poster_icon);
            posterLoc = (TextView) itemView.findViewById(R.id.poster_loc);
            dataLayout = (LinearLayout) itemView.findViewById(R.id.data_layout);
            imgFirst = (ImageView) itemView.findViewById(R.id.img_first);
            hintLayout = (FrameLayout) itemView.findViewById(R.id.hint_layout);
            hintText = (TextView) itemView.findViewById(R.id.hint_text);
            likeState = (ShineButton) itemView.findViewById(R.id.like_state);
            likeSum = (TextView) itemView.findViewById(R.id.like_sum);
//            videoView = (RelativeLayout) itemView.findViewById(R.id.player_view);
            dataLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PostActivity.newIntent(getActivity());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("post", post);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            likeState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constant.user == null) {
                        return ;
                    }
                    final RequestParams params = new RequestParams();
                    params.put("user_id", Constant.user.getId());
                    if (mliked) {
                        BaseClient.post(POSTS + post.getId() + UNLIKE, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                mliked = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();

                                BaseClient.get(POSTS + post.getId() + LIKE_COUNT, params, new JsonHttpResponseHandler() {
                                    int sum = 0;

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);

                                        try {
                                            sum = JsonUtil.getEntity(response.getString("count"), Integer.class);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        likeSum.setText(String.valueOf(sum));
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                    }
                                });
                            }
                        });
                    } else {
                        BaseClient.post(POSTS + post.getId() + LIKE, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                mliked = true;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();

                                BaseClient.get(POSTS + post.getId() + LIKE_COUNT, params, new JsonHttpResponseHandler() {
                                    int sum = 0;

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);

                                        try {
                                            sum = JsonUtil.getEntity(response.getString("count"), Integer.class);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        likeSum.setText(String.valueOf(sum));
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }

        public void bindPost(final Post post) {
            this.post = post;
            likeSum.setText(String.valueOf(post.getLikersSum()));
            postSubject.setText(post.getSubject());
            String dateString = post.getTimeStamp();
            Date date = new Date();
            try {
                date = psdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

//                    try {
//                        date = sdf.parse(dateString);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
            dateString = sdf.format(date);
            postDate.setText(dateString);
            if (post.getImgsPath().size() > 0 || post.getVideoPath() != null) {
                hintLayout.setVisibility(View.VISIBLE);
                if (post.getImgsPath().size() > 0) {
                    hintLayout.setBackgroundColor(getActivity().getResources()
                            .getColor(R.color.white));
                    hintText.setText(post.getImgsPath().size() + "+");
                    ImageLoader.getInstance().displayImage(
                            post.getImgsPath().get(0), imgFirst);
                } else {
                    hintText.setText("Video+");
                }
            }
//            ImageLoader.getInstance().displayImage(user.getGravatarURL(), posterIcon);
//            posterName.setText(user.getUsername());
//            if (user.getLocation() != null) {
//                posterLoc.setText(user.getLocation());
//            }
//            if (post.getVideoPath() != null) {
//                String videoPath = post.getVideoPath().toString();
//                videoView.setVisibility(View.VISIBLE);
//                video = new PlayerView(getActivity(), videoView)
//                        .setTitle("Video")
//                        .setScaleType(PlayStateParams.fitparent)
//                        .forbidTouch(false)
//                        .hideMenu(true)
//                        .setPlaySource(videoPath)
//                        .startPlay();
//            }

//            if (user == null) {
                BaseClient.getAbs(post.getAuthorURL(), null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        User user = null;
                        try {
                            user = JsonUtil.getEntity(response.getString("user"), User.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (user != null) {
                            ImageLoader.getInstance().displayImage(user.getGravatarURL(), posterIcon);
                            posterName.setText(user.getUsername());
                            if (user.getLocation() != null) {
                                posterLoc.setText("From " + user.getLocation());
                            }

                            if (Constant.user != null) {
                                if (user.getId() != Constant.user.getId()) {
                                    likeState.setVisibility(View.VISIBLE);

                                    RequestParams params = new RequestParams();
                                    params.put("user_id", Constant.user.getId());
                                    BaseClient.get(POSTS + post.getId() + ISLIKED, params,
                                            new JsonHttpResponseHandler() {
                                                boolean liked = false;

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    super.onSuccess(statusCode, headers, response);

                                                    try {
                                                        liked = JsonUtil.getEntity(response.getString("liked"), Boolean.class);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    mliked = liked;
                                                    likeState.setChecked(liked);
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    super.onFailure(statusCode, headers, responseString, throwable);
                                                }
                                            });
                                }
                            } else {
                                likeState.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                    posterName.setText("Android");
//                    String subject = "From Unknown Space\n";
//                    SpannableStringBuilder style = new SpannableStringBuilder(subject);
//                    style.setSpan(new ForegroundColorSpan(Color.GRAY),
//                            0, subject.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    postSubject.setText(style);
//                    String date = "Unknown Date";
//                    postDate.setText(date);
                    }
                });
//            } else {
//            }

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

//        private LinkedHashMap<Post, User> map;
        private ArrayList<Post> posts;

//        public PostAdapter(LinkedHashMap<Post, User> map) {
//            this.map = map;
//        }
        public PostAdapter (ArrayList<Post> posts) {
            this.posts = posts;
        }

        @Override
        public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(
                    R.layout.list_item_post, parent, false);
            return new PostHolder(view);
        }

//        @Override
//        public void onBindViewHolder(PostHolder holder, int position) {
//            Iterator<LinkedHashMap.Entry<Post, User> > it = map.entrySet().iterator();
//            for (int i = 0; i < position && it.hasNext(); i++) {}
//            Map.Entry<Post, User> entry = it.next();
//            holder.bindPost(entry.getKey(), entry.getValue());
//        }


        @Override
        public void onBindViewHolder(PostHolder holder, int position) {
            holder.bindPost(posts.get(position));
        }

//        @Override
//        public int getItemCount() {
//            return map.size();
//        }


        @Override
        public int getItemCount() {
            return posts.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void onViewRecycled(PostHolder holder) {
            super.onViewRecycled(holder);
            ImageLoader.getInstance().cancelDisplayTask(holder.posterIcon);
        }

        private void setPosts (ArrayList<Post> posts) {
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
                outRect.top = space + 4;
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
