package com.ailuridaes.simpledice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DiceRoll extends AppCompatActivity {
    private Random m_rand;
    private ArrayList<ImageView> diceViews = new ArrayList<>();
    private static Map<String, Integer> images = new HashMap<String, Integer>(20);

    static {
        images.put("dice_white_1", R.drawable.dice_white_1);
        images.put("dice_white_2", R.drawable.dice_white_2);
        images.put("dice_white_3", R.drawable.dice_white_3);
        images.put("dice_white_4", R.drawable.dice_white_4);
        images.put("dice_white_5", R.drawable.dice_white_5);
        images.put("dice_white_6", R.drawable.dice_white_6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dice_roll);

        m_rand = new Random(System.currentTimeMillis());

        diceViews.add((ImageView) findViewById(R.id.dice1_img));
        diceViews.add((ImageView) findViewById(R.id.dice2_img));
        diceViews.add((ImageView) findViewById(R.id.dice3_img));
        diceViews.add((ImageView) findViewById(R.id.dice4_img));
        diceViews.add((ImageView) findViewById(R.id.dice5_img));
        diceViews.add((ImageView) findViewById(R.id.dice6_img));


        LinearLayout diceLayout = (LinearLayout) findViewById(R.id.dice_roll_ll);
        diceLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // check if in center of screen
                    int vWidth = v.getWidth();
                    int vHeight = v.getHeight();
                    if (event.getX() < vWidth / 4 || event.getX() > vWidth * 3/4) {
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

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
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
        for (ImageView d : diceViews) {
            total += rollDie(d);
        }
        return total;
    }


    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
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
