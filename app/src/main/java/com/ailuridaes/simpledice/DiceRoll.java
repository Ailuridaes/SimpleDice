package com.ailuridaes.simpledice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DiceRoll extends AppCompatActivity {
    private Random m_rand;
    private ArrayList<ImageView> diceImages = new ArrayList<>();
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

        diceImages.add((ImageView) findViewById(R.id.dice1_img));
        diceImages.add((ImageView) findViewById(R.id.dice2_img));
        diceImages.add((ImageView) findViewById(R.id.dice3_img));
        diceImages.add((ImageView) findViewById(R.id.dice4_img));
        diceImages.add((ImageView) findViewById(R.id.dice5_img));
        diceImages.add((ImageView) findViewById(R.id.dice6_img));


        LinearLayout diceLayout = (LinearLayout) findViewById(R.id.dice_roll_ll);
        diceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ImageView d : diceImages) {
                    rollDie(d);
                }
            }
        });
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
