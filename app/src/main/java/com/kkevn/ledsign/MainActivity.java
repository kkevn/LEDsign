package com.kkevn.ledsign;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkevn.ledsign.bluetooth.BluetoothDialogFragment;
import com.kkevn.ledsign.bluetooth.ConnectedThread;
import com.kkevn.ledsign.bluetooth.PairedListView;
import com.kkevn.ledsign.ui.create.CreateFragment;
import com.kkevn.ledsign.ui.create.Effect;
import com.kkevn.ledsign.ui.create.SelectEffectListView;
import com.kkevn.ledsign.ui.help.HelpViewPagerFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    /* UI objects */
    private TextView tv_status;
    private ProgressBar pb;
    private Menu toolbar_menu;
    public Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private static NavController navController;
    FloatingActionButton fab;

    private static String currentProfileName = "New Profile";
    private static boolean doSaveProfile = false;

    private static String loadProfileName = "";
    private static boolean doLoadProfile = false;

    private int prev_page = -1;
    private int curr_page;

    private boolean showOptions = true;
    private static boolean currentProfileSaved = true;

    private SharedPreferences sharedPreferences;

    Vector<Effect> effects_list = new Vector<>();
    static SelectEffectListView selv;

    private Gson gson;

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

    final public static int UPDATE_TOOLBAR = 7;

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

        //NavigationView navigationView = findViewById(R.id.nav_view);

        // apply the app theme based on current preference selection for the theme
        String theme = sharedPreferences.getString(getString(R.string.pref_theme_key), "");
        if (theme.equalsIgnoreCase("LIGHT")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //navigationView.setItemTextAppearance(R.drawable.drawer_item);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setTheme(theme.equalsIgnoreCase("LIGHT") ? R.style.CustomLightTheme : R.style.CustomDarkTheme);
        //setTheme(theme.equalsIgnoreCase("LIGHT") ? R.style.CustomLightTheme : R.style.CustomDarkTheme);

        // apply layout to main activity
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // update the default profile name to what is found in Settings
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentProfileName = sharedPreferences.getString(getString(R.string.pref_default_name_key), "");

        currentProfileName = generateNextAvailableProfileName();
        toolbar.setTitle(currentProfileName);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

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
                R.id.nav_new_profile, R.id.nav_load_profile, R.id.nav_settings, R.id.nav_help, R.id.nav_about,
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

    public static void navigateToFragmentWithBundle(String selected_effect, int pos, String params) {

        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putString("params", params);

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

    public static void navigateToHelpFragmentWithBundle(int category, int index, int length) {

        Bundle args = new Bundle();
        args.putInt(HelpViewPagerFragment.CATEGORY, category);
        args.putInt(HelpViewPagerFragment.CATEGORY_INDEX, index);
        args.putInt(HelpViewPagerFragment.CATEGORY_LENGTH, length);

        navController.navigate(R.id.nav_help_item, args);
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
                //if (prev_page == R.id.nav_new_profile  /*&& currentProfileSaved == false*/) {
                //    new SaveProfileDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
                //}

                if (prev_page == R.id.nav_load_profile && doLoadProfile == true) {
                    loadProfile(loadProfileName);
                    doLoadProfile = false;
                    //toolbar.setTitle(loadProfileName.replace(".json", ""));
                }

                // detect user navigating away from the edit profile fragment (but not from configurator fragments)
                switch (curr_page) {
                    case R.id.nav_load_profile:
                    case R.id.nav_settings:
                    case R.id.nav_help:
                    case R.id.nav_about:

                        // prompt a save profile only if the current profile has unsaved changes
                        if (!currentProfileSaved && prev_page == R.id.nav_new_profile) {
                            //new SaveProfileDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());

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

                // attempt to change the toolbar's save button depending on current fragment in view
                try {
                    if (destination.getId() == R.id.nav_new_profile) {
                        //toolbar.getMenu().findItem(R.id.action_save).setVisible(true);
                        fab.show();
                        showOptions = true;
                        toolbar.setTitle(currentProfileName);   // set toolbar to current profile name when retuning to this activity
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

    /**/
    public static void updateProfileName(String newProfileName) {
        currentProfileName = newProfileName;
    }

    /**/
    public static void giveSaveSignal() {
        doSaveProfile = true;
    }

    /**/
    public static void setProfileUnsaved() {
        currentProfileSaved = false;
    }

    /**/
    public static void giveLoadSignal(String profileToLoad) {
        doLoadProfile = true;
        //loadProfileName = currentProfileName = profileToLoad;
        loadProfileName = profileToLoad;
        currentProfileName = profileToLoad.replace(".json", "");
        navigateToFragment("");
    }

    /**/
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

    /**/
    private String saveProfile() {

        String filename;

        // check if the effects list is empty before attempting to save it
        if (CreateFragment.getList().isEmpty()) {
            filename = "<empty>";
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

    /**/
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

    // https://www.youtube.com/watch?v=EcfUkjlL9RI
    private String saveProfile2() {

        String filename = generateNextAvailableProfileName();
        String fileContents = "test string/\ntest again";

        FileOutputStream fos = null;

        try {
            // write contents here
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            //fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(fileContents.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            filename = "<error>";
        } catch (IOException e) {
            e.printStackTrace();
            filename = "<error>";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    filename = "<error>";
                }
            }
        }
        return filename;
    }

    private void loadProfile2(String filename) {
        FileInputStream fis = null;
        try {
            //fis = getApplicationContext().openFileInput(filename);
            fis = openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                //effects_list.add(new Effect("", ""));
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // error opening file
        } finally {
            //String contents = stringBuilder.toString();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

        // enable or disable save button and 3-dot options menu
        for (int i = 0; i < toolbar_menu.size(); i++) {
            toolbar_menu.getItem(i).setVisible(showOptions);
        }

        //toolbar_menu.findItem(R.id.menu_prof_upload).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prof_save:
                // User chose the "Save Profile" item, save profile to disk
                String filename = saveProfile();

                // show snackbar message according to saveProfile() result
                if (filename.equals("<error>"))
                    Snackbar.make(getCurrentFocus(), "Error saving profile", Snackbar.LENGTH_INDEFINITE).show();
                else if (filename.equals("<empty>"))
                    Snackbar.make(getCurrentFocus(), "Cannot save empty profiles.", Snackbar.LENGTH_INDEFINITE).show();
                else
                    Snackbar.make(getCurrentFocus(), "Profile \'" + filename + "\' saved", Snackbar.LENGTH_SHORT).show();

                return true;

            case R.id.menu_prof_bt:
                // launch bluetooth dialog
                bluetoothOn();
                //new BluetoothDialogFragment().show(getSupportFragmentManager(), "MainActivity");
                return true;

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

            case R.id.menu_prof_clear:
                CreateFragment.removeEffects();
                return true;

            case R.id.menu_prof_reset:
                //pb.setVisibility(View.GONE);
                Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
                try {
                    if (MainActivity.ct.isAlive()) {
                        MainActivity.ct.write("<RESET{00000;},>".getBytes());
                    }
                    else {
                        Toast.makeText(getApplicationContext(),R.string.notify_failed_upload,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ioe) {
                    Log.e(this.getClass().getSimpleName(), "error writing to socket", ioe);
                }
                return true;

            case R.id.menu_prof_upload:

                try {
                    if (MainActivity.ct.isAlive()) {
                        //MainActivity.ct.write("0".getBytes());
                        //MainActivity.ct.write(CreateFragment.parseList().getBytes());
                        String to_bt_effects = "<" + CreateFragment.parseList() + ">";
                        String to_bt[] = to_bt_effects.split("(?<=,)");
                        for (String s : to_bt) {
                            //System.out.println(s);
                            MainActivity.ct.write(s.getBytes());
                        }
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