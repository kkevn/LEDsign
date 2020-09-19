package com.kkevn.ledsign;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kkevn.ledsign.bluetooth.BluetoothDialogFragment;
import com.kkevn.ledsign.bluetooth.ConnectedThread;
import com.kkevn.ledsign.bluetooth.PairedListView;
import com.kkevn.ledsign.ui.configurators.ConfiguratorListeners;
import com.kkevn.ledsign.ui.create.CreateFragment;
import com.kkevn.ledsign.ui.create.Effect;
import com.kkevn.ledsign.ui.create.SelectEffectListView;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    /* UI objects */
    private TextView tv_status;
    private ProgressBar pb;
    private Menu toolbar_menu;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private static NavController navController;
    FloatingActionButton fab;

    private int prev_page = -1;
    private int curr_page;

    private boolean showOptions = true;
    private boolean currentProfileSaved = true;

    Vector<Effect> effects_list = new Vector<>();
    static SelectEffectListView selv;

    /* Bluetooth objects */
    public static ConnectedThread ct;
    public static BluetoothAdapter mBTAdapter;
    public static ArrayAdapter mBTArrayAdapter;
    static ArrayList<BluetoothDevice> mPairedDevices = new ArrayList<>();   // ArrayList over Set for get()
    public static Handler handler;
    public static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // defines for identifying shared types between calling functions
    final public static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    final public static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    final public static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    static public final int MESSAGE_WRITE = 4;
    static public final int MESSAGE_TOAST = 5;

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
                //CreateFragment.addEffect("???", "test");
                new SelectEffectDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        pb = findViewById(R.id.pb_loader);
        tv_status = navigationView.getHeaderView(0).findViewById(R.id.tv_status);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_new_profile, R.id.nav_load_profile, R.id.nav_settings,
                R.id.nav_config_scrolling_text)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        curr_page = navController.getCurrentDestination().getId();

        onFragmentChange();


        /* Bluetooth code */

        //mBTArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        mBTArrayAdapter = new PairedListView(getApplicationContext(), mPairedDevices);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        handler = new Handler(){
            public void handleMessage(android.os.Message msg){
                /*ifo (msg.what == MESSAGE_READ) o{
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //mReadBuffer.setText(readMessage);
                }*/

                if (msg.what == CONNECTING_STATUS) {

                    if (msg.arg1 == 0) {
                        pb.setVisibility(View.VISIBLE);
                    }
                    else if (msg.arg1 == 1) {
                        try {
                            //tv_status.setText(getString(R.string.status_connected) + " [" + (String) (msg.obj) + "]");
                            //tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorConnected));
                        } catch (NullPointerException npe) {
                            Log.e(this.getClass().getSimpleName(), "error finding text view", npe);
                        }
                        tv_status.setText(getString(R.string.status_connected) + " [" + (String) (msg.obj) + "]");
                        tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorConnected));
                        pb.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Connected to " + (String) (msg.obj),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            //tv_status.setText(getString(R.string.status_disconnected));
                            //tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisconnected));
                        } catch (NullPointerException npe) {
                            Log.e(this.getClass().getSimpleName(), "error finding text view", npe);
                        }
                        tv_status.setText(getString(R.string.status_disconnected));
                        tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisconnected));
                        pb.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Failed to connect",Toast.LENGTH_SHORT).show();
                    }
                }

                if (msg.what == MESSAGE_WRITE) {
                    Toast.makeText(getApplicationContext(),"Wrote command",Toast.LENGTH_SHORT).show();
                }
            }
        };

        notifyMissingBluetooth();

        populateEffects();
    }

    public static void navigateToFragment(String selected_effect) {

        switch (selected_effect) {
            case Effect.TEXT_SCROLL:
                navController.navigate(R.id.nav_config_scrolling_text);
                break;
            default:
                navController.navigate(R.id.nav_new_profile);
                break;
        }
    }

    public static void navigateToFragmentWithBundle(String selected_effect, int pos, String params) {

        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putString("params", params);

        switch (selected_effect) {
            case Effect.TEXT_SCROLL:
                navController.navigate(R.id.nav_config_scrolling_text, args);
                break;
            default:
                navController.navigate(R.id.nav_new_profile);
                break;
        }
    }

    private void populateEffects() {
        for (Effect.Effect_Types e: Effect.Effect_Types.values()) {
            effects_list.add(new Effect(e.toString()));
        }
        selv = new SelectEffectListView(this, effects_list);
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
                    new SaveProfileDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
                }

                // attempt to change the toolbar's save button depending on current fragment in view
                try {
                    if (destination.getId() == R.id.nav_new_profile) {
                        //toolbar.getMenu().findItem(R.id.action_save).setVisible(true);
                        fab.show();
                        showOptions = true;
                    } else {
                        //toolbar.getMenu().findItem(R.id.action_save).setVisible(false);
                        fab.hide();
                        showOptions = false;
                    }

                    // enable or disable save button and 3-dot options menu
                    for (int i = 0; i < toolbar_menu.size(); i++) {
                        toolbar_menu.getItem(i).setVisible(showOptions);
                    }
                } catch (NullPointerException npe) {
                    Log.e(this.getClass().getSimpleName(), "error finding toolbar's save button", npe);
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

        toolbar_menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // customize menu so that it can be icon or in menu, tap on title to edit prof

        toolbar_menu.findItem(R.id.menu_prof_upload).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prof_save:
                // User chose the "Save Profile" item, save profile to disk
                String name = "test01";

                Snackbar.make(getCurrentFocus(), "Profile \'"+ name + "\' saved", Snackbar.LENGTH_SHORT).show();
                return true;

            case R.id.menu_prof_bt:
                // launch bluetooth dialog
                bluetoothOn();
                //new BluetoothDialogFragment().show(getSupportFragmentManager(), "MainActivity");
                return true;

            case R.id.menu_prof_rename:
                //handler.obtainMessage(MainActivity.CONNECTING_STATUS, 0, -1).sendToTarget();
                //pb.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Rename", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_prof_reset:
                //pb.setVisibility(View.GONE);
                Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_prof_upload:

                try {
                    if (MainActivity.ct.isAlive()) {
                        MainActivity.ct.write("0".getBytes());
                    }
                    else {
                        Toast.makeText(getApplicationContext(),R.string.notify_failed_upload,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ioe) {
                    Log.e(this.getClass().getSimpleName(), "error writing to socket", ioe);
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent Data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // prompt user with list of paired devices to connect to
                new BluetoothDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
            } else {
                Toast.makeText(getApplicationContext(), R.string.notify_require_bt, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Checks if device has support for bluetooth connectivity. Will notify userif no support
     * detected or will prompt user to enable bluetooth if not already enabled. If already enabled,
     * a dialog of paired devices will be shown asking the user to choose which to connect to.
     */
    private void bluetoothOn() {
        try {
            // check for bluetooth support on device
            if (mBTAdapter != null) {

                // check if user needs to enable bluetooth
                if (!mBTAdapter.isEnabled()) {

                    // request bluetooth to be enabled
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {

                    // draw the dialog box containing paired devices that can be connected to
                    new BluetoothDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
                }
            } else {

                // notify the user of their device missing bluetooth support
                notifyMissingBluetooth();
            }
        } catch (NullPointerException npe) {
            Log.e(this.getClass().getSimpleName(), "error finding bluetooth adapter", npe);
        }
    }

    /**
     * Disable any existing bluetooth connections and the bluetooth adapter. Also update the
     * bluetooth status indicator.
     */
    private void bluetoothOff() {
        try {

            // TODO

            mBTAdapter.disable();
            tv_status.setText(getString(R.string.status_disconnected));
            tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisconnected));
        } catch (NullPointerException npe) {
            Log.e(this.getClass().getSimpleName(), "error finding bluetooth adapter", npe);
        }
    }

    /**
     * Refreshes the list of paired bluetooth devices and adds them to the bluetooth array adapter.
     * If no device is found, a fake device is added as a flag to the BluetoothDialogFragment to
     * know when to notify the user of no devices found.
     */
    public static void pairedDevicesList() {
        try {

            // clear the list to prevent duplicates upon population
            mBTArrayAdapter.clear();

            // create an ArrayList of the paired bluetooth devices on this device
            mPairedDevices = new ArrayList<>(mBTAdapter.getBondedDevices());

            // check if bluetooth is enabled and there is at least one paired device
            if (mBTAdapter.isEnabled() && mPairedDevices.size() > 0) {

                // iterate over each paired device
                for (BluetoothDevice bt : mPairedDevices) {
                    mBTArrayAdapter.add(bt);
                }
            } else {
                mBTArrayAdapter.add(mBTAdapter.getRemoteDevice("-1"));
            }
        } catch (NullPointerException npe) {
            Log.e("MainActivity", "error finding bluetooth adapter", npe);
        }
    }

    /**
     * Notifies the user with a toast that the current device does not have support for
     * bluetooth connectivity.
     */
    private void notifyMissingBluetooth() {
        try {
            if (mBTAdapter == null) {
                Toast.makeText(getApplicationContext(), R.string.notify_missing_bt, Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException npe) {
            Log.e(this.getClass().getSimpleName(), "error finding bluetooth adapter", npe);
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            mBTAdapter.disable(); // turn off
            //mBluetoothStatus.setText("Bluetooth disabled");
            Toast.makeText(getApplicationContext(),"Bluetooth turned Off", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException npe) {
            Log.e(this.getClass().getSimpleName(), "error finding bluetooth adapter", npe);
        }
    }*/

    public static void manageMyConnectedSocket(BluetoothSocket socket) {
        ct = new ConnectedThread(socket, handler);
        ct.start();
    }
}