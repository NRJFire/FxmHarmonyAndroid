package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.dto.PostDTO;
import com.sofac.fxmharmony.dto.UserDTO;
import com.sofac.fxmharmony.server.Server;
import com.sofac.fxmharmony.server.type.ServerResponse;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.AppUserID;
import com.sofac.fxmharmony.view.fragment.ContentFragment;
import com.sofac.fxmharmony.view.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.R.attr.data;
import static com.orm.SugarRecord.findById;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.TYPE_GROUP;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    private static long backPressed;
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.findViewById(R.id.idNavDrawNameManager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();

        avatarImage = (ImageView) header.findViewById(R.id.navAvatarImage);
        userName = (TextView) header.findViewById(R.id.idNavDrawNameManager);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        new Server<ArrayList<PostDTO>>().getListPosts("", new Server.AnswerServerResponse<ArrayList<PostDTO>>() {
            @Override
            public void processFinish(Boolean isSuccess, ServerResponse<ArrayList<PostDTO>> answerServerResponse) {
                if(isSuccess) {
                    if (answerServerResponse.getDataTransferObject() != null) {
                        PostDTO.saveInTx(answerServerResponse.getDataTransferObject());
                    }
                } else {
                    Toast.makeText(NavigationActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMethods.putAvatarIntoImageView(this, avatarImage);
        //userName.setText(AppMethods.getUserName(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            Timber.e("!!!!!! onActivityResult");
        }
    }


    private void setupViewPager(ViewPager viewPager) { // Заполнение ViewPager
        UserDTO userDTO = findById(UserDTO.class, new AppUserID(this).getID());

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.idNotification:
                tabLayout.getTabAt(0).select();
                item.setChecked(true);
                break;
            case R.id.idSettingItem:
                Intent intentSettings = new Intent(NavigationActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.idExitItem:
                Intent intentSplashActivity = new Intent(this, SplashActivity.class);
                SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IS_AUTHORIZATION, false);
                editor.apply();
                intentSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editor.commit();
                startActivity(intentSplashActivity);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


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
