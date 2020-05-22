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

public class FragmentWithButtons extends Fragment implements View.OnClickListener {

    private OnFragmentWithButtonsClickListener listener;
    private TextView commentTextView;
    private ProgressBar progressBar;
    private Button drinkButton;
    private Button predictButton;
    private Button assessButton;
    private Button refreshButton;
    private EditText alcoAmountEditText;
    private Spinner alcoSpinner;
    private TextView progressTextView;
    private SharedPreferences sharedPref;
    private Context context;

    public FragmentWithButtons() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        drinkButton = view.findViewById(R.id.drinkButton);
        predictButton = view.findViewById(R.id.predictButton);
        assessButton = view.findViewById(R.id.assessButton);
        commentTextView = view.findViewById(R.id.commentTextView);
        progressBar = view.findViewById(R.id.progressBar);
        refreshButton = view.findViewById(R.id.refreshButton);
        progressTextView = view.findViewById(R.id.progressTextView);
        alcoAmountEditText = view.findViewById(R.id.alcoAmountEditText);
        alcoSpinner= view.findViewById(R.id.alcoSpinner);

        drinkButton.setOnClickListener(this);
        predictButton.setOnClickListener(this);
        assessButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);

        context = getActivity();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        refreshProgress();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assessButton:
                listener.onStartMakingTest();
                break;
            case R.id.drinkButton:
                addPpm();
                break;
            case R.id.predictButton:
                //не реализована
                break;
            case R.id.refreshButton:
                refreshProgress();
        }
    }

    private void addPpm() {
        double ppm = sharedPref.getInt("progress", 0);
        //TODO добавляем напитки
        String alco = alcoSpinner.getSelectedItem().toString();
        //double amount = alcoAmountEditText.getText();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress", (int) ppm * 10);
        editor.apply();
        refreshProgress();
    }

    private void refreshProgress() {

            //высчитываем по времени новое количество >=0

            double ppm = sharedPref.getInt("progress", 0);
            progressBar.setProgress((int) ppm*10);
            alcoAmountEditText.setText("0");
            progressTextView.setText(String.format("%.2f", ppm));
            if (ppm>=0 && ppm <0.2){
                commentTextView.setText(R.string.step_0_02);}
            else  if (ppm>=0.2 && ppm <0.3){
                commentTextView.setText(R.string.step_02_03);
            } else if (ppm>=0.3 && ppm <0.6){
                commentTextView.setText(R.string.step_03_06);
            } else if (ppm>=0.6 && ppm <1){
                commentTextView.setText(R.string.step_06_1);
            } else if (ppm>=1 && ppm <2){
                commentTextView.setText(R.string.step_1_2);
            } else if (ppm>=2 && ppm <3){
                commentTextView.setText(R.string.step_2_3);
            } else if (ppm>=3 && ppm <4){
                commentTextView.setText(R.string.step_3_4);
            } else if (ppm>=4 && ppm <5){
                commentTextView.setText(R.string.step_4_5);
            } else {
                commentTextView.setText(R.string.step_5);
            }
    };

    public interface OnFragmentWithButtonsClickListener {
        void onStartMakingTest();
    }

    @Override
    public void onAttach(Context context) {
        this.listener = (OnFragmentWithButtonsClickListener) context;
        super.onAttach(context);
    }
}
