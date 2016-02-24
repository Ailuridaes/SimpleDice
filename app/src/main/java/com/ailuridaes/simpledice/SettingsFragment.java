package com.ailuridaes.simpledice;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class SettingsFragment extends Fragment {
    private SharedPreferences mPrefs;
    private NumberPicker numDicePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sliding_menu_frame, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the shared preferences
        mPrefs = ((DiceRoll) getActivity()).getPrefs();

        // Setup numberPicker
        numDicePicker = (NumberPicker) getView().findViewById(
                R.id.settings_number_dice_picker);
        numDicePicker.setValue(mPrefs.getInt(getString(R.string.key_number_dice), getResources()
                .getInteger(R.integer.default_number_dice)));
        numDicePicker.setMinValue(getResources().getInteger(R.integer.settings_number_dice_min));
        numDicePicker.setMaxValue(getResources().getInteger(R.integer.settings_number_dice_max));

        numDicePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mPrefs.edit().putInt(getString(R.string.key_number_dice), newVal)
                        .apply();
            }
        });


    }


}
