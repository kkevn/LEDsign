package com.kkevn.ledsign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kkevn.ledsign.ui.home.Effect;
import com.kkevn.ledsign.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    FloatingActionButton fab;

    private int prev_page = -1;
    private int curr_page;

    private boolean currentProfileSaved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton*/ fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //HomeFragment.effects_list.add(new Effect("lol", "test"));
                HomeFragment.addEffect("lol", "test");
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_new_profile, R.id.nav_load_profile, R.id.nav_upload_profile)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        curr_page = navController.getCurrentDestination().getId();

        onFragmentChange();
    }

    /**
     * Attempt to change the toolbar's save button's visibility depending on the current fragment in
     * view. It should only be visible on the design fragment when editing effects.
     */
    private void onFragmentChange() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                // update fragment history
                prev_page = curr_page;
                curr_page = destination.getId();

                // prompt user to save profile before navigating away from the edit profile fragment
                if (prev_page == R.id.nav_new_profile /*&& currentProfileSaved == false*/) {
                    new SaveProfileDialogFragment().show(getSupportFragmentManager(), "MainActivity");;
                }

                // attempt to change the toolbar's save button depending on current fragment in view
                try {
                    if (destination.getId() == R.id.nav_new_profile) {
                        toolbar.getMenu().findItem(R.id.action_save).setVisible(true);
                        fab.show();
                    } else {
                        toolbar.getMenu().findItem(R.id.action_save).setVisible(false);
                        fab.hide();
                    }
                } catch (NullPointerException npe) {
                    Log.e("MainActivity", "error finding toolbar's save button", npe);
                }
            }
        });
    }

    /**
     * Inflate a new menu object with the proper XML layout.
     *
     * @param {Menu} menu: Menu object to inflate.
     *
     * @return {boolean} ???.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // User chose the "Save Profile" item, save profile to disk
                String name = "test01";

                Snackbar.make(getCurrentFocus(), "Profile \'"+ name + "\' saved", Snackbar.LENGTH_SHORT).show();
                return true;

            /*case R.id.action_upload:
                // User chose the "Upload Profile" action, upload profile to Arduino
                return true;*/

            case R.id.action_about:
                return true;

            case R.id.action_help:
                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_connect:
                return true;

            case R.id.action_disconnect:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}