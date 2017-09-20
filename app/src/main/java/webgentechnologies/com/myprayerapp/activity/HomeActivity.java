package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.Utils.PrefUtils;
import webgentechnologies.com.myprayerapp.fragment.ChangePasswordFrag;
import webgentechnologies.com.myprayerapp.fragment.EditOneFrag;
import webgentechnologies.com.myprayerapp.fragment.PostPrayerAudioFrag;
import webgentechnologies.com.myprayerapp.fragment.PostPrayerTextFrag;
import webgentechnologies.com.myprayerapp.fragment.PostPrayerVideoFrag;
import webgentechnologies.com.myprayerapp.fragment.SearchPrayerFrag;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    public static Context _context;
    LinearLayout _linearLayout_bar_prayers, _linearLayout_bar_edit;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        _context = HomeActivity.this;

        //---------------------adding listeners-------------------//
        _linearLayout_bar_prayers = (LinearLayout) findViewById(R.id.linearLayout_bar_prayers);
        _linearLayout_bar_edit = (LinearLayout) findViewById(R.id.linearLayout_bar_edit);
        _linearLayout_bar_prayers.setVisibility(View.VISIBLE);
        _linearLayout_bar_edit.setVisibility(View.GONE);

        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        ImageView img_post_txt = (ImageView) findViewById(R.id.img_post_txt);
        img_post_txt.setOnClickListener(this);
        ImageView img_post_audio = (ImageView) findViewById(R.id.img_post_audio);
        img_post_audio.setOnClickListener(this);
        ImageView img_post_video = (ImageView) findViewById(R.id.img_post_video);
        img_post_video.setOnClickListener(this);
        //----------------------------------------------------------------//


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
        TextView nav_header_username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        nav_header_username.setText(_userSingletonModelClass.getTxt_fname() + " " + _userSingletonModelClass.getTxt_lname());

        //----------------------------------hide change password option for fb users---------//
        if (_userSingletonModelClass.getTxt_user_access_token() == null) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.setGroupVisible(R.id.group_nav_change_pwd, false);
        }

        //--------------reload page when prayer is set to answered----------------//
        String choice_id = getIntent().getStringExtra("choice_id");
        if (choice_id == null)
            choice_id = "null";

        switch (choice_id) {
            case "R.id.nav_searchPrayer":
                displaySelectedScreen(R.id.nav_searchPrayer);
                break;
            default:
                displaySelectedScreen(R.id.nav_editProfile);
        }
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
        int id = item.getItemId();
        displaySelectedScreen(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        _linearLayout_bar_prayers.setVisibility(View.VISIBLE);
        _linearLayout_bar_edit.setVisibility(View.GONE);
        //initializing the fragment object which is selected
        switch (id) {
            case R.id.nav_editProfile:
                _linearLayout_bar_prayers.setVisibility(View.GONE);
                _linearLayout_bar_edit.setVisibility(View.VISIBLE);
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
                _linearLayout_bar_prayers.setVisibility(View.GONE);
                _linearLayout_bar_edit.setVisibility(View.GONE);
                fragment = new ChangePasswordFrag();
                break;
            case R.id.nav_signOut:
                try{
                    LoginManager.getInstance().logOut();
                }
                catch (Exception e){
                    Toast.makeText(_context, "Err:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                SharedPreferences.Editor editor = getSharedPreferences(PrefUtils._PREF_PRAYER_APP, MODE_PRIVATE).edit();
                editor.putString(PrefUtils._PREF_KEY_LOGIN_ID, null);
                editor.putString(PrefUtils._PREF_KEY_LOGIN_ACCESS_TOKEN, null);
                editor.apply();

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

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
            case R.id.imageButtonNext:
                _userSingletonModelClass.setTxt_fname(EditOneFrag.txt_fname.getText().toString());
                _userSingletonModelClass.setTxt_lname(EditOneFrag.txt_lname.getText().toString());
                _userSingletonModelClass.setTxt_email(EditOneFrag.txt_email.getText().toString());
                _userSingletonModelClass.setTxt_addr1(EditOneFrag.txt_addr1.getText().toString());
                _userSingletonModelClass.setTxt_addr2(EditOneFrag.txt_addr2.getText().toString());
                _userSingletonModelClass.setTxt_city(EditOneFrag.txt_city.getText().toString());
                _userSingletonModelClass.setTxt_phone(EditOneFrag.txt_phone.getText().toString());
                Intent intent = new Intent(HomeActivity.this, EditTwoActivity.class);
                startActivity(intent);
                break;
            case R.id.img_post_txt:
                displaySelectedScreen(R.id.img_post_txt);
                break;
            case R.id.img_post_audio:
                displaySelectedScreen(R.id.img_post_audio);
                break;
            case R.id.img_post_video:
                displaySelectedScreen(R.id.img_post_video);
                break;
        }
    }
}
