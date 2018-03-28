package com.CobaltConnect1.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.CobaltConnect1.R;
import com.CobaltConnect1.ui.fragments.ChangePasswordFragment;
import com.CobaltConnect1.ui.fragments.DashboardFragment;
import com.CobaltConnect1.ui.fragments.EditProfileFragment;
import com.CobaltConnect1.ui.fragments.MyProductsFragment;
import com.CobaltConnect1.ui.fragments.NewlyUpdatedFragment;
import com.CobaltConnect1.ui.fragments.ManageCategoriesfragment;
import com.CobaltConnect1.ui.fragments.ViewProfileFragment;
import com.CobaltConnect1.utils.PrefUtils;


public class NavigationalDrawerActivity extends ConnectCobaltBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{



    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_navigational_drawer;
    }

    @Override
    public int getNavigationIconId() {
        return 0;
    }

    @Override
    public void onNavigationIconClick(View v) {

    }

    @Override
    public String getActivityTitle() {
        return null;
    }

    @Override
    public boolean focusAtLaunch() {
        return false;
    }


    public TextView tvAppTitle;
    TextView tvMerchantId;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvMerchantId = (TextView) findViewById(R.id.merchant_id);
        tvAppTitle = (TextView) findViewById(R.id.tv_app_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvMerchantId.setText(PrefUtils.getUserName(this));
        tvMerchantId.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.dashboard);
        //navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        MenuItem itemid = navigationView.getMenu().findItem(R.id.dashboard);

        if (getFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            onNavigationItemSelected(itemid);
        }

        setUserLoggedIn();
    }




    private void setUserLoggedIn() {
        PrefUtils.storeUserLoggedIn(true, this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getFragmentManager().getBackStackEntryCount() == 0)
        {
            super.onBackPressed();
            tvAppTitle.setText("DashBoard");

        }
        else
        {
            getFragmentManager().popBackStack();

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        switch (id) {


            case R.id.dashboard:
                fragment = new DashboardFragment();
               tvAppTitle.setText(item.getTitle());

                break;

            case R.id.newly_updated:
                fragment = new NewlyUpdatedFragment();
                tvAppTitle.setText(item.getTitle());

                break;

            case R.id.manage_categories:
                fragment = new ManageCategoriesfragment();
                tvAppTitle.setText(item.getTitle());
                break;

            case R.id.my_products:
                fragment = new MyProductsFragment();
                tvAppTitle.setText(item.getTitle());
                break;

            /*case R.id.purchase_orders:
            *//*    fragment = new StockDetailsfragment();
                tvAppTitle.setText(item.getTitle());*//*
                break;

            case R.id.reporting:
                *//*fragment = new StockDetailsfragment();
                tvAppTitle.setText(item.getTitle());*//*
                break;*/

            case R.id.change_password:
                fragment = new ChangePasswordFragment();
                tvAppTitle.setText(item.getTitle());
                break;

            /*case R.id.logout:
                callRestart();
                break;*/

            default:
                fragment = new DashboardFragment();
                getSupportActionBar().setTitle(item.getTitle());
                break;


        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    private void callRestart() {
        //NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.cancel(5555);
        //notificationManager.cancel(4444);
        PrefUtils.storeUserLoggedIn(false, this);
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

        PopupMenu popup = new PopupMenu(NavigationalDrawerActivity.this, tvMerchantId);
        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.view_profile )
                {

                    Fragment fragmentViewProfile = new ViewProfileFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentViewProfile).commit();
                    tvAppTitle.setText(item.getTitle());


                }
                else
                {
                    Fragment fragmentEditProfile = new EditProfileFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentEditProfile).commit();
                    tvAppTitle.setText(item.getTitle());
                }
                return true;
            }
        });

        popup.show();

    }



}
