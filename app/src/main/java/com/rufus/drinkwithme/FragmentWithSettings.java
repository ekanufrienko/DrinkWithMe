package com.rufus.drinkwithme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentWithSettings extends Fragment {

    private TextView weightValueTextView;
    private TextView heightValueTextView;
    private SharedPreferences sharedPref;
    private FragmentWithSettings.OnFragmentWithSettingsClickListener listener;

    public FragmentWithSettings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SeekBar weightSeekBar;
        SeekBar heightSeekBar;
        RadioGroup genderRadioGroup;
        RadioButton maleRadioButton;
        RadioButton femaleRadioButton;
        weightValueTextView = view.findViewById(R.id.weightValueTextView);
        heightValueTextView = view.findViewById(R.id.heightValueTextView);
        weightSeekBar = view.findViewById(R.id.weightSeekBar);
        heightSeekBar = view.findViewById(R.id.heightSeekBar);
        genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        maleRadioButton = view.findViewById(R.id.radioButton_male);
        femaleRadioButton = view.findViewById(R.id.radioButton_female);

        Context context = getActivity();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        weightSeekBar.setProgress(sharedPref.getInt("weight", 30));
        heightSeekBar.setProgress(sharedPref.getInt("height", 30));
        weightValueTextView.setText(String.valueOf(sharedPref.getInt("weight", 30)));
        heightValueTextView.setText(String.valueOf(sharedPref.getInt("height", 30)));
        if (sharedPref.getBoolean("isMale", true)) {
            maleRadioButton.setChecked(true);
        } else {
            femaleRadioButton.setChecked(true);
        }

        weightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weightValueTextView.setText(String.valueOf(progress));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("weight", progress);
                editor.apply();
                listener.onRefresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightValueTextView.setText(String.valueOf(progress));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("height", progress);
                editor.apply();
                listener.onRefresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = sharedPref.edit();

                switch (checkedId) {
                    case R.id.radioButton_female:
                        editor.putBoolean("isMale", false);
                        break;
                    case R.id.radioButton_male:
                        editor.putBoolean("isMale", true);
                        break;
                }
                editor.apply();
                listener.onRefresh();
            }
        });

        return view;
    }

    public interface OnFragmentWithSettingsClickListener {
        void onRefresh();
    }

    @Override
    public void onAttach(Context context) {
        this.listener = (FragmentWithSettings.OnFragmentWithSettingsClickListener) context;
        super.onAttach(context);
    }
}
