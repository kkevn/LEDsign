/**
 * MainActivity is the activity containing the logic for handling and communicating the entire
 * application between all of its components, such as all fragments and threads.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceManager;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkevn.ledsign.bluetooth.BluetoothDialogFragment;
import com.kkevn.ledsign.bluetooth.ConnectedThread;
import com.kkevn.ledsign.bluetooth.PairedListViewAdapter;
import com.kkevn.ledsign.ui.create.CreateFragment;
import com.kkevn.ledsign.ui.create.Effect;
import com.kkevn.ledsign.ui.create.SelectEffectListViewAdapter;
import com.kkevn.ledsign.ui.help.HelpViewPagerFragment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    /* UI objects */
    private TextView tv_status;
    private ProgressBar progressBar;
    private Menu toolbar_menu;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private static NavController navController;
    private FloatingActionButton floatingActionButton;

    /* Effect objects */
    private Vector<Effect> effects_list = new Vector<>();
    static SelectEffectListViewAdapter selectEffectListViewAdapter;

    /* Profile variables */
    private static String currentProfileName = "New Profile";
    private static boolean doSaveProfile = false;
    private static String loadProfileName = "";
    private static boolean doLoadProfile = false;
    private static boolean currentProfileSaved = true;

    /* Miscellaneous */
    private int prevPage = -1;
    private int currPage;
    private boolean showOptions = true;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    /* Bluetooth objects */
    public static ConnectedThread connectedThread;
    public static BluetoothAdapter mBTAdapter;
    public static ArrayAdapter mBTArrayAdapter;
    private static ArrayList<BluetoothDevice> mPairedDevices = new ArrayList<>();
    public static Handler handler;
    public static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    final public static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    final public static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    final public static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    final public static int MESSAGE_WRITE = 4;  // used in bluetooth handler to verify message writes

    /**
     * Returns the theme called by the activity. Checks the current theme preference and color
     * accent to update the styling accordingly.
     *
     * @return {Resources.Theme} Theme to use.
     */
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();

        // get the accent color selection preference
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int accent = sharedPreferences.getInt(getString(R.string.pref_color_key), 0);

        // get the possible accent color choices and their respective styles
        int[] accents = getResources().getIntArray(R.array.accents);
        int[] accent_styles = {R.style.SelectableAccentColorRed, R.style.SelectableAccentColorGreen, R.style.SelectableAccentColorBlue, R.style.SelectableAccentColorPurple, R.style.SelectableAccentColorGray,
                R.style.SelectableAccentColorRed2, R.style.SelectableAccentColorGreen2, R.style.SelectableAccentColorBlue2, R.style.SelectableAccentColorPurple2, R.style.SelectableAccentColorGray2,
                R.style.SelectableAccentColorRed3, R.style.SelectableAccentColorGreen3, R.style.SelectableAccentColorBlue3, R.style.SelectableAccentColorPurple3, R.style.SelectableAccentColorGray3};

        // apply the color accent style that matches the color found in Preferences
        for (int i = 0; i < accents.length; i++) {
            if (accent == accents[i]) {
                theme.applyStyle(accent_styles[i], true);
            }
        }

        return theme;
    }

    /**
     * Creates the activity and initializes all relevant objects.
     *
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get a reference to the app's shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // set default accent color on first run of app
        String COLOR_KEY = getResources().getString(R.string.pref_color_key);
        if (sharedPreferences.getInt(COLOR_KEY, 0) == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(COLOR_KEY, getResources().getIntArray(R.array.accents)[7]);
            editor.commit();
        }

        // apply the app theme based on current preference selection of app theme
        String THEME_KEY = getResources().getString(R.string.pref_theme_key);
        String theme = sharedPreferences.getString(THEME_KEY, "");
        AppCompatDelegate.setDefaultNightMode(theme.equalsIgnoreCase("0") ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        //setTheme(theme.equalsIgnoreCase("0") ? R.style.CustomLightTheme : R.style.CustomDarkTheme);

        // ---

        // apply layout to main activity
        setContentView(R.layout.activity_main);

        // find the Toolbar in this activity's layout and apply its adapter and touch callback
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

        // update the default profile name to what is found in Settings
        currentProfileName = sharedPreferences.getString(getString(R.string.pref_default_name_key), "");
        currentProfileName = generateNextAvailableProfileName();
        toolbar.setTitle(currentProfileName);

        // update default values for preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // initialize the Gson object
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        // find the FloatingActionButton in this activity's layout and apply its listener
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // prompt a list of effects to select from
                new SelectEffectDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
            }
        });

        // find the DrawerLayout and NavigationView in this activity's layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // find the TextView in the NavigationView's layout
        tv_status = navigationView.getHeaderView(0).findViewById(R.id.tv_status);

        // pass navigation menu IDs so they'll be considered as top level destinations
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_new_profile, R.id.nav_load_profile, R.id.nav_settings, R.id.nav_help, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // update reference to the current navigation destination
        currPage = navController.getCurrentDestination().getId();

        // listen for fragment changes
        onFragmentChange();

        // ---

        // find the ProgressBar in this activity's layout
        progressBar = findViewById(R.id.pb_loader);

        // create the Bluetooth adapter and get a handle on the Bluetooth radio
        mBTArrayAdapter = new PairedListViewAdapter(getApplicationContext(), mPairedDevices);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        // setup handler to handle communication between threads
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                // Bluetooth connection was being established
                if (msg.what == CONNECTING_STATUS) {

                    // attempting to connect
                    if (msg.arg1 == 0) {

                        // reveal indefinite progress bar
                        progressBar.setVisibility(View.VISIBLE);

                    } else if (msg.arg1 == 1) {

                        // connection successful...

                        // update Bluetooth status message and hide progress bar
                        tv_status.setText(getString(R.string.status_connected) + " [" + (String) (msg.obj) + "]");
                        tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorConnected));
                        progressBar.setVisibility(View.GONE);

                        Snackbar.make(getCurrentFocus(), "Successfully connected to \'" + (String) (msg.obj) + "\'", Snackbar.LENGTH_SHORT).show();

                    } else {

                        // connection failed or disconnected...

                        // update Bluetooth status message and hide progress bar
                        tv_status.setText(getString(R.string.status_disconnected));
                        tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisconnected));
                        progressBar.setVisibility(View.GONE);

                        Snackbar.make(getCurrentFocus(), "Failed to connect to device", Snackbar.LENGTH_LONG).show();
                    }
                }

                // Bluetooth message was being written
                if (msg.what == MESSAGE_WRITE) {

                    // message failed to send
                    if (msg.arg1 == 0) {
                        Snackbar.make(getCurrentFocus(), "Device failed to receive outgoing command", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(getCurrentFocus(), "Device received outgoing command", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        };

        // check if the current device supports Bluetooth
        notifyMissingBluetooth();

        // build the internal list of available effects
        populateEffects();
    }

    /**
     * Inflate a new menu object with the proper XML layout.
     *
     * @param {Menu} menu: Menu object to inflate.
     *
     * @return {boolean} Creation status.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // save reference to the menu and inflate it
        toolbar_menu = menu;
        getMenuInflater().inflate(R.menu.options, menu);

        return true;
    }

    /**
     * Prepares the menu object once it has been created.
     *
     * @param {Menu} menu: Menu object to prepare.
     *
     * @return {boolean} Preparation status.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // show or hide toolbar action items based on showOptions status
        for (int i = 0; i < toolbar_menu.size(); i++) {
            toolbar_menu.getItem(i).setVisible(showOptions);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Listener for which toolbar menu items were selected.
     *
     * @param {MenuItem} item: MenuItem selected.
     *
     * @return {boolean} Selection status.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // hamburger or back arrow pressed
            case android.R.id.home:
                // toolbar occasionally resets its own color so force the color
                toolbar.setBackgroundColor(Color.BLACK);
                return false;

            // save button pressed
            case R.id.menu_prof_save:

                // save the current profile and obtain its filename
                String filename = saveProfile();

                // create a default SnackBar
                Snackbar sb = Snackbar.make(getCurrentFocus(), "", Snackbar.LENGTH_INDEFINITE);

                // update SnackBar message according to saveProfile() result
                if (filename.equals("<error>")) {
                    sb.setText("Error writing profile");
                    sb.setAction("RETRY", e -> {sb.dismiss();saveProfile();});
                } else if (filename.equals("<empty>")) {
                    sb.setText("Effects list cannot be empty");
                    sb.setAction("OK", e -> {sb.dismiss();});
                } else if (filename.equals("<blank>")) {
                    sb.setText("Profile names cannot be blank");
                    sb.setAction("OK", e -> {sb.dismiss();});
                } else {
                    sb.setText("Profile \'" + filename + "\' successfully saved");
                    sb.setDuration(Snackbar.LENGTH_SHORT);
                }

                // reveal the SnackBar
                sb.show();
                return true;

            // Bluetooth button pressed
            case R.id.menu_prof_bt:

                // launch bluetooth dialog
                enableBlueTooth();
                return true;

            // 3-dots menu rename button pressed
            case R.id.menu_prof_rename:

                // setup FragmentManager and rename dialog objects
                FragmentManager fm = getSupportFragmentManager();
                RenameProfileDialogFragment rpdf = new RenameProfileDialogFragment();

                // send current profile name via Bundle to rename dialog
                Bundle args = new Bundle();
                args.putString("profile_name", "" + toolbar.getTitle());
                rpdf.setArguments(args);

                // reveal the rename dialog
                rpdf.show(fm, this.getClass().getSimpleName());

                // set listener for when rename dialog is dismissed
                fm.executePendingTransactions();
                rpdf.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        // update new profile name if it was changed
                        if (!currentProfileName.equals(toolbar.getTitle())) {

                            // rename the file from old to new profile name in the directory
                            File file = new File(getApplicationContext().getFilesDir(), toolbar.getTitle().toString() + ".json");
                            if (file.exists())
                                file.renameTo(new File(getApplicationContext().getFilesDir(), currentProfileName + ".json"));

                            // update toolbar with new profile name when dialog is dismissed
                            toolbar.setTitle(currentProfileName);
                        }

                        // prevent dialog from reappearing when resuming app
                        rpdf.dismiss();
                    }
                });
                return true;

            // 3-dots menu clear button pressed
            case R.id.menu_prof_clear:

                // clear the current profile of its effects
                CreateFragment.removeEffects();
                return true;

            // 3-dots menu reset button pressed
            case R.id.menu_prof_reset:

                // attempt to send a reset signal to the Arduino
                try {

                    // send signal if connection still active
                    if (MainActivity.connectedThread.isAlive()) {
                        MainActivity.connectedThread.write("<RESET{00000;},>".getBytes());
                    } else {
                        Snackbar.make(getCurrentFocus(), "Failed to reset device", Snackbar.LENGTH_LONG).show();
                    }

                } catch (Exception ioe) {
                    Log.e(this.getClass().getSimpleName(), "error writing to socket", ioe);
                    Snackbar.make(getCurrentFocus(), "No Bluetooth connection established", Snackbar.LENGTH_LONG).show();
                }
                return true;

            // 3-dots menu upload button pressed
            case R.id.menu_prof_upload:

                // attempt to send current profile to the Arduino
                try {

                    // send profile if connection still active
                    if (MainActivity.connectedThread.isAlive()) {

                        // surround the parsed profile with the start and end flags for the Arduino
                        String to_bt_effects = "<" + CreateFragment.parseList() + ">";

                        // split the effects into an array
                        String to_bt[] = to_bt_effects.split("(?<=,)");

                        // send the effects one at a time to the Arduino to not overload it in one go
                        for (String s : to_bt) {
                            MainActivity.connectedThread.write(s.getBytes());
                        }
                    } else {
                        Snackbar.make(getCurrentFocus(), "Failed to upload profile to device", Snackbar.LENGTH_LONG).show();
                    }

                } catch (Exception ioe) {
                    Log.e(this.getClass().getSimpleName(), "error writing to socket", ioe);
                    Snackbar.make(getCurrentFocus(), "No Bluetooth connection established", Snackbar.LENGTH_LONG).show();
                }
                return true;

            default:
                // user's action was not recognized, invoke superclass to handle it
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when navigating up within the navigation hierarchy.
     *
     * @return {boolean} Navigate up status.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    /**
     * Called when an activity launched exits. Used to confirm the Bluetooth permissions and prompt
     * a connection to a Bluetooth device.
     *
     * @param {int} requestCode: Request code passed.
     * @param {int} resultCode: Result code passed.
     * @param {Intent} Data: Intent that can return result data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent Data) {

        // check request responding to
        if (requestCode == REQUEST_ENABLE_BT) {

            // ensure the request was successful
            if (resultCode == RESULT_OK) {

                // prompt user with list of paired devices to connect to
                new BluetoothDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());

            } else {
                Snackbar.make(getCurrentFocus(), "Bluetooth must be allowed to upload profiles", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Navigates to the fragment containing the configurator for the specified effect. If no effect
     * is recognized, will simply navigate back to the profile creation page.
     *
     * @param {String} selected_effect: Effect that was selected.
     */
    public static void navigateToFragment(String selected_effect) {

        // navigate to the selected effect's respective configurator
        switch (selected_effect) {
            case Effect.TEXT_SCROLL:
                navController.navigate(R.id.nav_config_scrolling_text);
                break;
            case Effect.COLOR_SOLID:
                navController.navigate(R.id.nav_config_solid_color);
                break;
            case Effect.COLOR_RAINBOW:
                navController.navigate(R.id.nav_config_wave_color);
                break;
            case Effect.COLOR_FADE:
                navController.navigate(R.id.nav_config_fade_color);
                break;
            case Effect.COLOR_WIPE:
                navController.navigate(R.id.nav_config_color_wipe);
                break;
            case Effect.THEATER_CHASE:
                navController.navigate(R.id.nav_config_theater_chase);
                break;
            default:
                navController.navigate(R.id.nav_new_profile);
                break;
        }
    }

    /**
     * Navigates to the fragment containing the configurator for the specified effect. Bundles the
     * selected effect's information and passes it along to the configurator to use as default
     * values to prepare for editing. If no effect is recognized, will simply navigate back to the
     * profile creation page.
     *
     * @param {String} selected_effect: Effect that was selected.
     * @param {int} pos: Position of effect in list.
     * @param {String} params: Parameters of effect that was selected.
     */
    public static void navigateToFragmentWithBundle(String selected_effect, int pos, String params) {

        // bundle the specified effect's current information
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putString("params", params);

        // navigate to the selected effect's respective configurator
        switch (selected_effect) {
            case Effect.TEXT_SCROLL:
                navController.navigate(R.id.nav_config_scrolling_text, args);
                break;
            case Effect.COLOR_SOLID:
                navController.navigate(R.id.nav_config_solid_color, args);
                break;
            case Effect.COLOR_RAINBOW:
                navController.navigate(R.id.nav_config_wave_color, args);
                break;
            case Effect.COLOR_FADE:
                navController.navigate(R.id.nav_config_fade_color, args);
                break;
            case Effect.COLOR_WIPE:
                navController.navigate(R.id.nav_config_color_wipe, args);
                break;
            case Effect.THEATER_CHASE:
                navController.navigate(R.id.nav_config_theater_chase, args);
                break;
            default:
                navController.navigate(R.id.nav_new_profile);
                break;
        }
    }

    /**
     * Navigates to a help item fragment for the help item selected at the given category.
     *
     * @param {int} category: Category index of help topics.
     * @param {int} index: Position of help item in category.
     * @param {int} length: Size of category.
     */
    public static void navigateToHelpFragmentWithBundle(int category, int index, int length) {

        // bundle the parameters given
        Bundle args = new Bundle();
        args.putInt(HelpViewPagerFragment.CATEGORY, category);
        args.putInt(HelpViewPagerFragment.CATEGORY_INDEX, index);
        args.putInt(HelpViewPagerFragment.CATEGORY_LENGTH, length);

        // navigate to the help item page
        navController.navigate(R.id.nav_help_item, args);
    }

    /**
     * Populates the list of all available effects for the prompt for selecting effects.
     */
    private void populateEffects() {

        // add the effect for each one found
        for (Effect.Effect_Types e: Effect.Effect_Types.values()) {
            effects_list.add(new Effect(e.toString()));
        }

        // create the select effect list adapter
        selectEffectListViewAdapter = new SelectEffectListViewAdapter(this, effects_list);
    }

    /**
     * Attempts to detect changes of navigation. Hides or reveals action items that should only be
     * visible during profile creation.
     */
    private void onFragmentChange() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                // update fragment history
                prevPage = currPage;
                currPage = destination.getId();

                // check if a profile should be loaded
                if (prevPage == R.id.nav_load_profile && doLoadProfile == true) {
                    loadProfile(loadProfileName);
                    doLoadProfile = false;
                }

                // detect user navigating away from the edit profile fragment (but not from configurator fragments)
                switch (currPage) {
                    case R.id.nav_load_profile:
                    case R.id.nav_settings:
                    case R.id.nav_help:
                    case R.id.nav_about:

                        // prompt a save profile only if the current profile has unsaved changes
                        if (!currentProfileSaved && prevPage == R.id.nav_new_profile) {

                            // setup FragmentManager and save profile dialog objects
                            FragmentManager fm = getSupportFragmentManager();
                            SaveProfileDialogFragment spdf = new SaveProfileDialogFragment();

                            // reveal the save profile dialog
                            spdf.show(fm, this.getClass().getSimpleName());

                            // set listener for when save profile dialog is dismissed
                            fm.executePendingTransactions();
                            spdf.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {

                                    // save profile if it was requested
                                    if (doSaveProfile == true) {
                                        saveProfile();
                                        doSaveProfile = false;
                                    }

                                    // prevent dialog from reappearing when resuming app
                                    spdf.dismiss();
                                }
                            });
                        }
                        break;
                }

                // reveal or hide action items based on whether or not at profile creation
                try {
                    if (destination.getId() == R.id.nav_new_profile) {
                        floatingActionButton.show();
                        showOptions = true;
                        toolbar.setTitle(currentProfileName);
                    } else {
                        floatingActionButton.hide();
                        showOptions = false;
                    }

                    // show or hide toolbar acton items
                    for (int i = 0; i < toolbar_menu.size(); i++) {
                        toolbar_menu.getItem(i).setVisible(showOptions);
                    }
                } catch (NullPointerException npe) {
                    Log.e(this.getClass().getSimpleName(), "error finding toolbar", npe);
                }
            }
        });
    }

    /**
     * Updates the reference to the new profile name.
     *
     * @param {String} newProfileName: New profile name.
     */
    public static void updateProfileName(String newProfileName) {
        currentProfileName = newProfileName;
    }

    /**
     * Updates the save flag for informing the activity that a profile save is requested.
     */
    public static void giveSaveSignal() {
        doSaveProfile = true;
    }

    /**
     * Updates the saved flag for informing the activity that a profile has unsaved changes.
     */
    public static void setProfileUnsaved() {
        currentProfileSaved = false;
    }

    /**
     * Updates the load flag for informing the activity that a profile load is requested. Also
     * updates the current profile name to the one being requested to load and navigates back to
     * profile creation.
     *
     * @param {String} profileToLoad: Name of profile to load.
     */
    public static void giveLoadSignal(String profileToLoad) {
        doLoadProfile = true;
        loadProfileName = profileToLoad;

        // remove the extension from the profile name
        currentProfileName = profileToLoad.replace(".json", "");

        // navigate back to profile creation page
        navigateToFragment("");
    }

    /**
     * Returns the next available unique profile name based on the default profile name preference.
     *
     * @return {String} Unique profile name.
     */
    private String generateNextAvailableProfileName() {

        // get the default profile name as standalone
        String defaultProfileName = currentProfileName;

        // get the default name as the new profile name
        String newProfileName = defaultProfileName;

        // get list of profile filenames in app's internal storage directory
        ArrayList<String> files = new ArrayList(Arrays.asList(getApplicationContext().fileList()));

        // sort filenames in natural order with original/unique filenames ahead of their duplicates
        files.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {

                // remove extension from current filenames before comparing
                s = s.substring(0, s.lastIndexOf('.'));
                t1 = t1.substring(0, t1.lastIndexOf('.'));

                // label original/unique filenames with a zero
                if (!s.contains("(")) {
                    s = s.concat(" (0)");
                }
                if (!t1.contains("(")) {
                    t1.concat(" (0)");
                }

                // return case insensitive comparison
                return String.CASE_INSENSITIVE_ORDER.compare(s, t1);
            }
        });

        // first duplicates start at '2'
        int duplicate = 2;

        // iterate over existing file names
        for (String file: files) {

            // remove extension from current filename before comparing
            file = file.substring(0, file.lastIndexOf('.'));

            // if any existing file names already have the current proposed name, increase duplicate label
            if (file.equals(newProfileName)) {
                newProfileName = defaultProfileName + " (" + duplicate++ + ")";
            }
        }

        // return the first unique new profile name
        return newProfileName;
    }

    /**
     * Writes the contents of the current profile to storage and returns its filename.
     *
     * @return {String} Saved profile name.
     */
    private String saveProfile() {

        String filename;

        // check if the effects list is empty before attempting to save it
        if (CreateFragment.getList().isEmpty()) {
            filename = "<empty>";
        } else if (toolbar.getTitle().toString().trim().isEmpty()) {
            filename = "<blank>";
        } else {

            // get the current profile name with a JSON file extension
            filename = toolbar.getTitle() + ".json";

            try {
                // ready a new file in this app's internal storage directory with a filename
                File file = new File(getApplicationContext().getFilesDir(), filename);

                // setup a FileWriter stream for the above file
                Writer writer = new FileWriter(file);

                // write the effects list as a JSON list object to the specified file
                gson.toJson(CreateFragment.getList(), writer);

                // close writer to prevent memory leaks
                writer.close();

                // update save status flag
                currentProfileSaved = true;

            } catch (IOException e) {
                e.printStackTrace();
                filename = "<error>";
            }
        }

        // return filename (or error result)
        return filename;
    }

    /**
     * Attempts to load the specified filename and update the effects list based on its contents.
     *
     * @param {String} filename: Name of profile to load.
     */
    private void loadProfile(String filename) {
        try {

            // fetch the file in this app's internal storage directory with its filename
            File x = new File(getApplicationContext().getFilesDir(), filename);

            // setup a FileReader stream for the above file
            Reader reader = new FileReader(x);

            // read the JSON list object into an Effect object array
            Effect[] effects = gson.fromJson(reader, Effect[].class);

            // clear profile creation page and fill with effects from specified file
            CreateFragment.removeEffects();
            for (Effect e : effects) {
                CreateFragment.addEffect(e.getType(), e.getParam());
            }

            // close reader to prevent memory leaks
            reader.close();

            // update save status flag (prevents save dialog from unnecessarily prompting)
            currentProfileSaved = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if device has support for bluetooth connectivity. Will notify user if no support
     * detected or will prompt user to enable bluetooth if not already enabled. If already enabled,
     * a dialog of paired devices will be shown asking the user to choose which to connect to.
     */
    private void enableBlueTooth() {
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
    private void disableBlueTooth() {
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
     * Notifies the user with a SnackBar message that the current device does not have support for
     * bluetooth connectivity.
     */
    private void notifyMissingBluetooth() {
        try {

            // first check for valid BlueTooth adapter
            if (mBTAdapter == null) {
                Snackbar sb = Snackbar.make(floatingActionButton, "Device is missing Bluetooth support", Snackbar.LENGTH_INDEFINITE);
                sb.setAction("OK", e -> sb.dismiss());
                sb.show();
            }
        } catch (NullPointerException npe) {
            Log.e(this.getClass().getSimpleName(), "error finding bluetooth adapter", npe);
        }
    }

    /**
     * Creates and starts a new ConnectedThread with the given socket to initiate communication
     * between this and the target Bluetooth device.
     */
    public static void manageMyConnectedSocket(BluetoothSocket socket) {
        connectedThread = new ConnectedThread(socket, handler);
        connectedThread.start();
    }

    /**
     * Attempts to launch the default browser at the specified web URL based on the view that was
     * specified. Used exclusively in the About page where tapping a CardView should navigate to
     * that item's URL.
     *
     * @param {View} v: Given view.
     */
    public void openLink(View v) {

        String url;

        // fetch URL for which about page item was clicked
        switch (v.getId()) {
            case R.id.cv_supportlibrary:
                url = getResources().getString(R.string.support_link);
                break;
            case R.id.cv_expandabletext:
                url = getResources().getString(R.string.expandabletext_link);
                break;
            case R.id.cv_gson:
                url = getResources().getString(R.string.gson_link);
                break;
            case R.id.cv_materialdrawer:
                url = getResources().getString(R.string.materialdrawer_link);
                break;
            case R.id.cv_processing:
                url = getResources().getString(R.string.processing_link);
                break;
            case R.id.cv_swipereveal:
                url = getResources().getString(R.string.swipereveal_link);
                break;
            default:
                url = getResources().getString(R.string.app_link);
        }

        // set intent for the default device browser to view the selected link
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}