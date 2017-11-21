package com.CobaltConnect1.ui.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.CobaltConnect1.R;
import com.CobaltConnect1.utils.InputUtils;



/**
 * Created by Abhinandan Sharma on 10/7/17.
 */
public abstract class ConnectCobaltBaseActivity extends AppCompatActivity {
    public abstract int getLayoutResourceId();

    public abstract int getNavigationIconId();

    public abstract void onNavigationIconClick(View v);

    public abstract String getActivityTitle();

    public abstract boolean focusAtLaunch();

    public Toolbar getToolbar() {
        return toolbar;
    }

    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (!focusAtLaunch()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if (getNavigationIconId() != 0)
            toolbar.setNavigationIcon(getNavigationIconId());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getActivityTitle());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputUtils.isKeyBoardOpen(ConnectCobaltBaseActivity.this))
                    InputUtils.hideKeyboard(ConnectCobaltBaseActivity.this);
                onNavigationIconClick(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);


    }
}