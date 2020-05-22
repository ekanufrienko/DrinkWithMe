package com.rufus.drinkwithme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

public class FragmentWithButtons extends Fragment implements View.OnClickListener {

    private OnFragmentWithButtonsClickListener listener;
    private TextView commentTextView;
    private ProgressBar progressBar;
    private EditText alcoAmountEditText;
    private Spinner alcoSpinner;
    private TextView progressTextView;
    private SharedPreferences sharedPref;

    public FragmentWithButtons() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        Button drinkButton = view.findViewById(R.id.drinkButton);
        Button predictButton = view.findViewById(R.id.predictButton);
        Button assessButton = view.findViewById(R.id.assessButton);
        Button clearButton = view.findViewById(R.id.clearButton);
        commentTextView = view.findViewById(R.id.commentTextView);
        progressBar = view.findViewById(R.id.progressBar);
        progressTextView = view.findViewById(R.id.progressTextView);
        alcoAmountEditText = view.findViewById(R.id.alcoAmountEditText);
        alcoSpinner = view.findViewById(R.id.alcoSpinner);

        drinkButton.setOnClickListener(this);
        predictButton.setOnClickListener(this);
        assessButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        Context context = getActivity();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String[] drinks = getResources().getStringArray(R.array.drinks);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (String drink : drinks) {
            if (!sharedPref.contains(drink)) {
                editor.putInt(drink, Integer.parseInt(drink.replaceAll("[^0-9]", "")));
            }
        }
        editor.apply();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProgress();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (v.getId()) {
            case R.id.assessButton:
                listener.onStartMakingTest();
                break;
            case R.id.drinkButton:
                editor.putFloat("alcoMass", (float) (sharedPref.getFloat("alcoMass", 0f)
                        + sharedPref.getInt(alcoSpinner.getSelectedItem().toString(), 0)
                        * 0.01 * Float.parseFloat(alcoAmountEditText.getText().toString())));
                editor.apply();
                refreshProgress();
                break;
            case R.id.predictButton:
                //не реализована
                break;
            case R.id.clearButton:
                editor.putFloat("alcoMass", 0f);
                editor.apply();
                refreshProgress();
        }
    }

    void refreshProgress() {

        SharedPreferences.Editor editor = sharedPref.edit();
        float alcoMass = sharedPref.getFloat("alcoMass", 0);
        float viewmarkCoeff = (sharedPref.getBoolean("isMale", true) ? 0.7f : 0.6f);
        float ppm = (float) (alcoMass / ((float) sharedPref.getInt("weight", 30) * viewmarkCoeff
                        + (sharedPref.getInt("height", 30) - 150)* 0.1));

        editor.putFloat("ppm", ppm);
        editor.apply();

        progressBar.setProgress((int) ppm * 20);
        alcoAmountEditText.setText("0");
        progressTextView.setText(String.format(Locale.US, "%.2f", ppm));
        if (ppm >= 0 && ppm < 0.2) {
            commentTextView.setText(R.string.step_0_02);
        } else if (ppm >= 0.2 && ppm < 0.3) {
            commentTextView.setText(R.string.step_02_03);
        } else if (ppm >= 0.3 && ppm < 0.6) {
            commentTextView.setText(R.string.step_03_06);
        } else if (ppm >= 0.6 && ppm < 1) {
            commentTextView.setText(R.string.step_06_1);
        } else if (ppm >= 1 && ppm < 2) {
            commentTextView.setText(R.string.step_1_2);
        } else if (ppm >= 2 && ppm < 3) {
            commentTextView.setText(R.string.step_2_3);
        } else if (ppm >= 3 && ppm < 4) {
            commentTextView.setText(R.string.step_3_4);
        } else if (ppm >= 4 && ppm < 5) {
            commentTextView.setText(R.string.step_4_5);
        } else {
            commentTextView.setText(R.string.step_5);
        }
    }

    public interface OnFragmentWithButtonsClickListener {
        void onStartMakingTest();
    }

    @Override
    public void onAttach(Context context) {
        this.listener = (OnFragmentWithButtonsClickListener) context;
        super.onAttach(context);
    }
}
