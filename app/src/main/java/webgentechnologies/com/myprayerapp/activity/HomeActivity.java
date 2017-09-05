package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.fragment.ChangePasswordFrag;
import webgentechnologies.com.myprayerapp.fragment.EditOneFrag;
import webgentechnologies.com.myprayerapp.fragment.PostPrayerAudioFrag;
import webgentechnologies.com.myprayerapp.fragment.PostPrayerTextFrag;
import webgentechnologies.com.myprayerapp.fragment.PostPrayerVideoFrag;
import webgentechnologies.com.myprayerapp.fragment.SearchPrayerFrag;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout linearLayout_bar_prayers, linearLayout_bar_edit;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    public static Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        _context = HomeActivity.this;
        setCustomClickListeners();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (userclass.getTxt_user_access_token() == null) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.setGroupVisible(R.id.group_nav_change_pwd, false);
        }
        //add this line to display nav_editProfile when the activity is loaded

        String choice_id = getIntent().getStringExtra("choice_id");
        if (choice_id == null)
            choice_id = "null";

        switch (choice_id) {
            case "R.id.nav_searchPrayer":
                displaySelectedScreen(R.id.nav_searchPrayer);
                break;
            default:
                displaySelectedScreen(R.id.nav_searchPrayer);

        }
    }


    private void setCustomClickListeners() {
        linearLayout_bar_prayers = (LinearLayout) findViewById(R.id.linearLayout_bar_prayers);
        linearLayout_bar_edit = (LinearLayout) findViewById(R.id.linearLayout_bar_edit);
        linearLayout_bar_prayers.setVisibility(View.VISIBLE);
        linearLayout_bar_edit.setVisibility(View.GONE);

        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userclass.setTxt_fname(EditOneFrag.txt_fname.getText().toString());
                userclass.setTxt_lname(EditOneFrag.txt_lname.getText().toString());
                userclass.setTxt_email(EditOneFrag.txt_email.getText().toString());
                userclass.setTxt_addr1(EditOneFrag.txt_addr1.getText().toString());
                userclass.setTxt_addr2(EditOneFrag.txt_addr2.getText().toString());
                userclass.setTxt_city(EditOneFrag.txt_city.getText().toString());
                userclass.setTxt_phone(EditOneFrag.txt_phone.getText().toString());
                Intent intent = new Intent(HomeActivity.this, EditTwoActivity.class);
                startActivity(intent);
            }
        });
        ImageView img_post_txt = (ImageView) findViewById(R.id.img_post_txt);
        img_post_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySelectedScreen(R.id.img_post_txt);
            }
        });
        ImageView img_post_audio = (ImageView) findViewById(R.id.img_post_audio);
        img_post_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySelectedScreen(R.id.img_post_audio);
            }
        });
        ImageView img_post_video = (ImageView) findViewById(R.id.img_post_video);
        img_post_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySelectedScreen(R.id.img_post_video);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        displaySelectedScreen(id);
//        if (id == R.id.nav_editProfile) {
//            // Handle the camera action
//            startActivity(new Intent(HomeActivity.this,EditOneActivity.class));
////            HomeActivity.this.finish();
//        } else if (id == R.id.nav_postPrayer) {
//
//        } else if (id == R.id.nav_searchPrayer) {
//
//        } else if (id == R.id.nav_changePwd) {
//            startActivity(new Intent(HomeActivity.this,ChangePasswordFrag.class));
////            HomeActivity.this.finish();
//        }
//        else if (id == R.id.nav_signOut) {
//        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
//            HomeActivity.this.finish();
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int id) {
        //creating fragment object
        Fragment fragment = null;
        linearLayout_bar_prayers.setVisibility(View.VISIBLE);
        linearLayout_bar_edit.setVisibility(View.GONE);
        //initializing the fragment object which is selected
        switch (id) {
            case R.id.nav_editProfile:
                linearLayout_bar_prayers.setVisibility(View.GONE);
                linearLayout_bar_edit.setVisibility(View.VISIBLE);
                fragment = new EditOneFrag();
                break;
            case R.id.nav_postPrayer:
            case R.id.img_post_txt:
                fragment = new PostPrayerTextFrag();
                break;
            case R.id.img_post_audio:
                fragment = new PostPrayerAudioFrag();
                break;
            case R.id.img_post_video:
                fragment = new PostPrayerVideoFrag();
                break;
            case R.id.nav_searchPrayer:
                fragment = new SearchPrayerFrag();
                break;

            case R.id.nav_changePwd:
                linearLayout_bar_prayers.setVisibility(View.GONE);
                linearLayout_bar_edit.setVisibility(View.GONE);
                fragment = new ChangePasswordFrag();
                //startActivity(new Intent(HomeActivity.this,ChangePasswordFrag.class));
                break;
            case R.id.nav_signOut:
                UserSingletonModelClass.get_userSingletonModelClass().destroyUser();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                HomeActivity.this.finish();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }
}
