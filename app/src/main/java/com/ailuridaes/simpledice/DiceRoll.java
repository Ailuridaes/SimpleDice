package com.ailuridaes.simpledice;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import com.github.tbouron.shakedetector.library.ShakeDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DiceRoll extends SlidingFragmentActivity implements
OnSharedPreferenceChangeListener {
    private Random m_rand;
    private SharedPreferences mPrefs;
    private int mNumDice;
    private ArrayList<ImageView> diceViews = new ArrayList<>();
    private static Map<String, Integer> images = new HashMap<String, Integer>(20);
    private SettingsFragment mLogFragRight;
    private SettingsFragment mLogFragLeft;

    static {
        images.put("dice_white_1", R.drawable.dice_white_1);
        images.put("dice_white_2", R.drawable.dice_white_2);
        images.put("dice_white_3", R.drawable.dice_white_3);
        images.put("dice_white_4", R.drawable.dice_white_4);
        images.put("dice_white_5", R.drawable.dice_white_5);
        images.put("dice_white_6", R.drawable.dice_white_6);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupPreferences();
        setContentView(R.layout.dice_roll);

        m_rand = new Random(System.currentTimeMillis());

        diceViews.add((ImageView) findViewById(R.id.dice1_img));
        diceViews.add((ImageView) findViewById(R.id.dice2_img));
        diceViews.add((ImageView) findViewById(R.id.dice3_img));
        diceViews.add((ImageView) findViewById(R.id.dice4_img));
        diceViews.add((ImageView) findViewById(R.id.dice5_img));
        diceViews.add((ImageView) findViewById(R.id.dice6_img));
        diceViews.add((ImageView) findViewById(R.id.dice7_img));
        diceViews.add((ImageView) findViewById(R.id.dice8_img));

        setNumberDice(mPrefs.getInt(getString(R.string.key_number_dice), R.integer.default_number_dice));

        LinearLayout diceLayout = (LinearLayout) findViewById(R.id.dice_roll_ll);
        diceLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // check if in center of screen
                    int vWidth = v.getWidth();
                    int vHeight = v.getHeight();
                    if (event.getX() < vWidth / 4 || event.getX() > vWidth * 3 / 4) {
                        return false;
                    } else if (event.getY() < vHeight / 3 || event.getY() > vHeight * 2 / 3) {
                        return false;
                    } else {
                        rollDice();
                        return true;
                    }
                } else {
                    return false;
                }
            }
        });

        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                rollDice();
            }
        });

        ShakeDetector.updateConfiguration(1.75f, 3);

        // Make sliding menu fragments
        setBehindContentView(R.layout.sliding_menu_frame);
        getSlidingMenu().setSecondaryMenu(R.layout.sliding_menu_frame2);
        createSlidingMenus();
    }

    private int rollDie(ImageView diceView) {
        int sides = 6;
        int result = 1 + m_rand.nextInt(sides);

        int resId = images.get("dice_white_" + Integer.toString(result));
        diceView.setImageResource(resId);

        return result;
    }

    private int rollDice() {
        int total = 0;
        for (int i=0; i<mNumDice; i++) {
            total += rollDie(diceViews.get(i));
        }
        return total;
    }

    private void setNumberDice(int numDice) {
        for (int i=0; i<numDice; i++) {
            ((View)diceViews.get(i).getParent()).setVisibility(View.VISIBLE);
            if (i%2==0) {
                ((View)diceViews.get(i).getParent().getParent()).setVisibility(View.VISIBLE);
            }
        }
        for (int i=numDice; i<diceViews.size(); i++){
            ((View)diceViews.get(i).getParent()).setVisibility(View.GONE);
            if (i%2==0) {
                ((View)diceViews.get(i).getParent().getParent()).setVisibility(View.GONE);
            }
        }
        mNumDice = numDice;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == getString(R.string.key_number_dice)) {
            setNumberDice(mPrefs.getInt(key, getResources()
                    .getInteger(R.integer.default_number_dice)));
        }

    }

    private void setupPreferences() {
        mPrefs = getPreferences(Context.MODE_PRIVATE);

        // Fill SharedPreferences with default information if they don't exist
        if (mPrefs.getAll().size() == 0) {
            SharedPreferences.Editor edit = mPrefs.edit();

            edit.putInt(getString(R.string.key_number_dice), getResources()
                    .getInteger(R.integer.default_number_dice));

            edit.commit();
        }
    }

    /**
     * Get the SharedPreferences of this Activity.
     *
     * @return SharedPreferences object associated with this Activity.
     */
    public SharedPreferences getPrefs() {
        return mPrefs;
    }

    /**
     * Create the sliding menus for this activity. If savedInstanceState is not
     * null, the menu fragment can simply be retrieved from the fragment
     * manager.
     *
     * //@param savedInstanceState
     *            If the activity is being re-initialized after previously being
     *            shut down then this Bundle contains the data it most recently
     *            supplied in onSaveInstanceState(Bundle). Note: Otherwise it is
     *            null.
     */
    private void createSlidingMenus() {
        SlidingMenu menu = getSlidingMenu();
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setFadeDegree(0.35f);
        menu.setMenu(R.layout.sliding_menu_frame);
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        /*
        menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
        menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        menu.setSecondaryShadowDrawable(R.drawable.sliding_menu_shadow_right);
        */

        /*
        menu.setOnClosedListener(new OnClosedListener() {
            @Override
            public void onClosed() {
                closeOptions();
            }
        });
        */

        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(this.getId(), new SettingsFragment(),
                                this.getTag() + "_OPTIONS").addToBackStack(null)
                        .commit();
            }
        });


        Fragment optionsLeft = getFragmentManager().findFragmentByTag(
                "LEFT_OPTIONS");
        Fragment optionsRight = getFragmentManager().findFragmentByTag(
                "RIGHT_OPTIONS");
        mLogFragRight = (SettingsFragment) getFragmentManager()
                .findFragmentByTag("RIGHT");
        mLogFragLeft = (SettingsFragment) getFragmentManager()
                .findFragmentByTag("LEFT");

        FragmentTransaction ft;

        // Only create new fragments if they don't exist
        if (mLogFragRight == null || mLogFragLeft == null) {
            mLogFragRight = new SettingsFragment();
            mLogFragLeft = new SettingsFragment();
            ft = getFragmentManager().beginTransaction();
            ft = ft.replace(R.id.sliding_menu_frame2, mLogFragRight, "RIGHT");
            ft = ft.replace(R.id.sliding_menu_frame, mLogFragLeft, "LEFT");
            ft.commit();
        }

        ft = getFragmentManager().beginTransaction();

        // Restore the options fragments if they exist
        if (optionsRight != null) {
            ft = ft.replace(R.id.sliding_menu_frame2, optionsRight,
                    "RIGHT_OPTIONS");
        }

        if (optionsLeft != null) {
            ft = ft.replace(R.id.sliding_menu_frame, optionsLeft,
                    "LEFT_OPTIONS");
        }

        // If there are any changes to be done, commit them
        if (!ft.isEmpty()) {
            ft.commit();
        }

        getFragmentManager().executePendingTransactions();

    }

    private boolean closeOptions() {
        //this.recreate();
        //In SimpleLife, this method pops back the stack if user has navigated within SlidingMenu
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
        mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dice_roll, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
