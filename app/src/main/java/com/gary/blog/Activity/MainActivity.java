package com.gary.blog.Activity;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gary.blog.Constant;
import com.gary.blog.Data.Post;
import com.gary.blog.Data.User;
import com.gary.blog.Fragment.CommentsFragment;
import com.gary.blog.Fragment.FollowedFragment;
import com.gary.blog.Fragment.PostsFragment;
import com.gary.blog.MyApplication;
import com.gary.blog.R;
import com.gary.blog.Utils.NetWorkUtil;
import com.gary.blog.Wedgit.CircleImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    //activity wedgit
    private TextView toolbarTitle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ActionBarDrawerToggle drawerToggle;

    //drawerHeader wedgit
    private TextView usernameText, emailText;
    private CircleImage userIcon;

    private ViewPagerAdapter adapter;
    private FragmentManager fm;

    private PostsFragment postsFragment;
    private CommentsFragment commentsFragment;
    private FollowedFragment followedFragment;

    private ArrayList<Post> posts;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Constant.UpdateIcon:
//                    userIcon.setImageBitmap((Bitmap) msg.obj);
//                    break;
//
//            }
//        }
//    };

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = MyApplication.getInstance();

        fm = getSupportFragmentManager();

        navigationView = (NavigationView) findViewById(R.id.navigationview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_left_text);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        postsFragment = new PostsFragment();
//        commentsFragment = new CommentsFragment();
        followedFragment = new FollowedFragment();

        adapter = new ViewPagerAdapter(fm);
        adapter.addFragment(postsFragment, "首页");
        adapter.addFragment(followedFragment, "关注");
//        adapter.addFragment(commentsFragment, "NONE");
//        adapter.addFragment(new UpdateFragment(), "NONE");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        initModels();
        initView();
    }

    @SuppressWarnings("RestrictedApi")
    private void initView() {
        toolbarTitle.setText("Blog");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.tab_1, R.string.tab_2);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        usernameText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        userIcon = (CircleImage) navigationView.getHeaderView(0).findViewById(R.id.user_icon);
        emailText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.user_info:
                        if (Constant.user == null) {
                            Toast.makeText(MainActivity.this, "请先登录",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = UserInfoActivity.newIntent(MainActivity.this);
                            intent.putExtra("user", Constant.user);
                            startActivity(intent);
//                            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    MainActivity.this, userIcon, "user_icon").toBundle());
                        }
                        break;
                    case R.id.logout:
                        if (Constant.user == null) {
                            Toast.makeText(MainActivity.this, "请先登录",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Constant.user = null;
                            usernameText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
                            userIcon = (CircleImage) navigationView.getHeaderView(0).findViewById(R.id.user_icon);
                            emailText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email);

                            usernameText.setText("User Name");
                            userIcon.setImageDrawable(getResources().getDrawable(R.drawable.default_icon));
                            emailText.setText("E-mail");
                            postsFragment.onRefresh();
                        }
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.user == null) {
                    Intent intent = LoginActivity.newIntent(MainActivity.this);
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                            MainActivity.this, userIcon, "user_icon").toBundle());
                } else {
                    Intent intent = UserInfoActivity.newIntent(MainActivity.this);
                    intent.putExtra("user", Constant.user);
                    startActivity(intent);
                }
            }
        });
    }

    @SuppressWarnings("RestrictedApi")
    private void initModels() {
        if (getIntent().hasExtra("user")) {
            Constant.user = (User)getIntent().getSerializableExtra("user");
        }
        if (Constant.user != null) {
            usernameText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
            userIcon = (CircleImage) navigationView.getHeaderView(0).findViewById(R.id.user_icon);
            emailText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email);

            usernameText.setText(Constant.user.getUsername());
            emailText.setText(Constant.user.getEmail());
            if (NetWorkUtil.isNetWorkOpened(MainActivity.this)) {
                ImageLoader.getInstance().displayImage(Constant.user.getGravatarURL(), userIcon);
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (NetWorkUtil.isNetWorkOpened(MainActivity.this)) {
//                            Bitmap bitmap = BitmapService.getBitmap(Constant.user.getGravatarURL());
//                            Message msg = new Message();
//                            msg.obj = bitmap;
//                            msg.what = Constant.UpdateIcon;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                });
//                t.start();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_post:
                if (Constant.user != null) {
                    Intent intent = WritePostActivity.newIntent(MainActivity.this);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    Toast.makeText(MainActivity.this, "请先登录",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    //Define ViewPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter (FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            titles = new ArrayList<>();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {

            super.setPrimaryItem(container, position, object);
        }

    }
}
