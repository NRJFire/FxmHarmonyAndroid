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
import com.sofac.fxmharmony.dto.ManagerDTO;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.server.Connection;
import com.sofac.fxmharmony.util.AppPreference;
import com.sofac.fxmharmony.view.fragment.PushFragment;
import com.sofac.fxmharmony.view.fragment.GroupFragment;
import com.sofac.fxmharmony.view.fragment.TossFragment;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.sofac.fxmharmony.Constants.AVATAR_IMAGE_SIZE;
import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.LINK_IMAGE;
import static com.sofac.fxmharmony.Constants.NAME_IMAGE;
import static com.sofac.fxmharmony.Constants.PART_AVATAR;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;

    private ImageView avatarImage;
    private TextView textViewUserName;
    private TextView textViewUserStatus;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setTitle(getString(R.string.app_name));

        progressBar.showView();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();

        avatarImage = header.findViewById(R.id.navAvatarImage);
        textViewUserName = header.findViewById(R.id.idNavDrawNameManager);
        textViewUserStatus = header.findViewById(R.id.idNavDrawTypeManager);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnClickListener(view -> {
        });

        new Connection<ManagerDTO>().getManagerInfo(userDTO.getId(), (isSuccess, answerServerResponse) -> {
            if(isSuccess){
                setupUserInfoInHeader(BASE_URL + PART_AVATAR + answerServerResponse.getDataTransferObject().getAvatar(), answerServerResponse.getDataTransferObject().getName(), userDTO.getRole());
            } else {
                setupUserInfoInHeader(BASE_URL + PART_AVATAR + "man-03.jpg", "Name", userDTO.getRole());
            }
            progressBar.dismissView();
        });
        setupViewPager(viewPager);
    }

    public void setupUserInfoInHeader(String userAvatarURL, String userName, String userStatus){
        avatarImage.setOnClickListener( v -> {
            Intent intent = new Intent(this, PreviewPhotoActivity.class);
            intent.putExtra(LINK_IMAGE, userAvatarURL);
            intent.putExtra(NAME_IMAGE,"User Avatar");
            startActivity(intent);
        });
        Glide.with(this)
                .load(userAvatarURL)
                .bitmapTransform(new CropCircleTransformation(this))
                .error(R.drawable.no_avatar)
                .override(AVATAR_IMAGE_SIZE, AVATAR_IMAGE_SIZE)
                .into(avatarImage);
        textViewUserName.setText(userName);
        textViewUserStatus.setText(userStatus);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PushFragment(), "PUSH");
        if (userDTO.isAdmin() || userDTO.isAccessLeaderGroup() || userDTO.isAccessMemberGroup() || userDTO.isAccessStaffGroup()) {
            adapter.addFragment(new GroupFragment(), "Group");
        }
        adapter.addFragment(new TossFragment(), "TOSS");
        viewPager.setAdapter(adapter);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.idWebSiteSOFAC:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL+"control/")));
                break;
            case R.id.idExitItem:
                new AppPreference(this).setAuthorization(false);
                startActivity(new Intent(this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    //INNER CLASS
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
