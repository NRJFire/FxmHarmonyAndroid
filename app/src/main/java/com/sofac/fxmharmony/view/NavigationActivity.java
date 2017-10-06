package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.Server;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.CheckAuthorization;
import com.sofac.fxmharmony.view.fragment.ContentFragment;
import com.sofac.fxmharmony.view.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.sofac.fxmharmony.Constants.AVATAR_IMAGE_SIZE;
import static com.sofac.fxmharmony.Constants.TYPE_GROUP;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;

    private ImageView avatarImage;
    private TextView userName;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setTitle(getString(R.string.app_name));

        progressBar.showView();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.findViewById(R.id.idNavDrawNameManager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();

        avatarImage = (ImageView) header.findViewById(R.id.navAvatarImage);
        avatarImage.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        Glide.with(this)
                .load("https://s5.postimg.org/6zrmvcblz/avatar3.png")
                .bitmapTransform(new CropCircleTransformation(this))
                .error(R.drawable.no_avatar)
                .override(AVATAR_IMAGE_SIZE, AVATAR_IMAGE_SIZE)
                .into(avatarImage);

        userName = (TextView) header.findViewById(R.id.idNavDrawNameManager);
        userName.setText(userDTO.getLogin());

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        new Server<ArrayList<PostDTO>>().getListPosts("", new Server.AnswerServerResponse<ArrayList<PostDTO>>() {
            @Override
            public void processFinish(Boolean isSuccess, ServerResponse<ArrayList<PostDTO>> answerServerResponse) {
                progressBar.dismissView();
                if(isSuccess) {
                    if (answerServerResponse.getDataTransferObject() != null) {
                        PostDTO.deleteAll(PostDTO.class);
                        PostDTO.saveInTx(answerServerResponse.getDataTransferObject());
                        setupViewPager(viewPager);
                    }
                } else {
                    Toast.makeText(NavigationActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) { // Заполнение ViewPager

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContentFragment(), "PUSH");

        if (userDTO.isAdmin() || userDTO.isAccessLeaderGroup()) {
            Bundle bundleGroupLeader = new Bundle();
            bundleGroupLeader.putString(TYPE_GROUP, "leadergroup");
            GroupFragment groupFragmentLeader = new GroupFragment();
            groupFragmentLeader.setArguments(bundleGroupLeader);
            adapter.addFragment(groupFragmentLeader, "leader\ngroup");
        }
        if (userDTO.isAdmin() || userDTO.isAccessMemberGroup()) {
            Bundle bundleGroupLeader = new Bundle();
            bundleGroupLeader.putString(TYPE_GROUP, "membergroup");
            GroupFragment groupFragmentMember = new GroupFragment();
            groupFragmentMember.setArguments(bundleGroupLeader);
            adapter.addFragment(groupFragmentMember, "member\ngroup");
        }
        if (userDTO.isAdmin() || userDTO.isAccessStaffGroup()) {
            Bundle bundleGroupLeader = new Bundle();
            bundleGroupLeader.putString(TYPE_GROUP, "staffgroup");
            GroupFragment groupFragmentStaff = new GroupFragment();
            groupFragmentStaff.setArguments(bundleGroupLeader);
            adapter.addFragment(groupFragmentStaff, "staff\ngroup");
        }

        viewPager.setAdapter(adapter);
    }


    //
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.idWebSiteSOFAC:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL+"control/")));
                //tabLayout.getTabAt(0).select();
                item.setChecked(true);
                break;
//            case R.id.idSettingItem:
//                Intent intentSettings = new Intent(NavigationActivity.this, SettingsActivity.class);
//                startActivity(intentSettings);
//                item.setChecked(true);
//                break;
            case R.id.idExitItem:
                new CheckAuthorization(this).setAuthorization(false);
                startActivity(new Intent(this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                item.setChecked(true);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private static long backPressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.ToastLogOut), Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }


}
