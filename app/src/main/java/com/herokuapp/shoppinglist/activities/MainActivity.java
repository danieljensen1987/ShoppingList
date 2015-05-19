package com.herokuapp.shoppinglist.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.herokuapp.shoppinglist.R;
import com.herokuapp.shoppinglist.fragments.FragmentNavigationDrawer;
import com.herokuapp.shoppinglist.fragments.HomeFragment;
import com.herokuapp.shoppinglist.fragments.ListsFragment;
import com.herokuapp.shoppinglist.fragments.SettingsFragment;
import com.herokuapp.shoppinglist.preferences.UserLocalStore;

public class MainActivity extends ActionBarActivity {
    private FragmentNavigationDrawer dlDrawer;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocalStore = new UserLocalStore(this);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        // Setup drawer view
        dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), toolbar,
                R.layout.drawer_nav_item, R.id.flContent);
        // Add nav items
        dlDrawer.addNavItem("Home",R.drawable.home,"Home", HomeFragment.class);
        dlDrawer.addNavItem("Lists",R.drawable.list, "Lists", ListsFragment.class);
        dlDrawer.addNavItem("Settings",R.drawable.settings, "Settings", SettingsFragment.class);
        // Select default "Home"
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Boolean test = true;
        if(!userLocalStore.isLoggedIn()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        if (dlDrawer.isDrawerOpen()) {
            // Uncomment to hide menu items
            // menu.findItem(R.id.mi_test).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                userLocalStore.clearUserData();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dlDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }
}
