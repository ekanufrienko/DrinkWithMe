package com.rufus.drinkwithme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import java.util.Random;

public class FragmentTest1 extends Fragment implements View.OnClickListener {

    private OnFragmentWithTest1ClickListener listener;
    private Handler uiThreadHandler = new Handler();
    private final Random random = new Random();
    private SharedPreferences sharedPref;
    private int timeForButtonChange;
    private double countOfchange;
    private int prevButtonNumber;
    private int counter = 3;

    private TextView textTest1;
    private Button nextButton;
    private TextView counterTextView;
    private ImageButton[] catchButton = new ImageButton[12];



    public FragmentTest1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test1, container, false);

        Context context = getActivity();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isCaught", false);
        editor.apply();

        counter = 3;
        countOfchange = 0;
        timeForButtonChange = 400;

        textTest1 = view.findViewById(R.id.textTest1);
        nextButton = view.findViewById(R.id.nextButton);
        counterTextView = view.findViewById(R.id.counterTextView);
        catchButton[0] = view.findViewById(R.id.catchButton);
        catchButton[1] = view.findViewById(R.id.catchButton2);
        catchButton[2] = view.findViewById(R.id.catchButton3);
        catchButton[3] = view.findViewById(R.id.catchButton4);
        catchButton[4] = view.findViewById(R.id.catchButton5);
        catchButton[5] = view.findViewById(R.id.catchButton6);
        catchButton[6] = view.findViewById(R.id.catchButton7);
        catchButton[7] = view.findViewById(R.id.catchButton8);
        catchButton[8] = view.findViewById(R.id.catchButton9);
        catchButton[9] = view.findViewById(R.id.catchButton10);
        catchButton[10] = view.findViewById(R.id.catchButton11);
        catchButton[11] = view.findViewById(R.id.catchButton12);

        nextButton.setOnClickListener(this);
        for (ImageButton button : catchButton) {
            button.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextButton) {
            listener.onTest1Complete();
        } else {

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isCaught", true);
            editor.apply();

            double time = countOfchange * timeForButtonChange / 1000;
            String result = "Result: " + time + " sec.";
            textTest1.setText(result);
            for (ImageButton button : catchButton) {
                button.setClickable(false);
            }

            showRunnable.run();
        }
    }

    public interface OnFragmentWithTest1ClickListener {
        void onTest1Complete();
    }

    private Runnable counterRunnable = new Runnable() {
        @Override
        public void run() {
            prevButtonNumber = -1;
            if (counter > 0) {
                counterTextView.setText(String.valueOf(counter));
            } else {
                counterTextView.setText("");
            }
            counter--;
            if (counter >= 0) {
                uiThreadHandler.postDelayed(this, 1000);
            } else {
                gameRunnable.run();
            }
        }
    };

    private Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            if (sharedPref.getBoolean("isCaught", false)) {
                return;
            }
            countOfchange++;
            if (prevButtonNumber != -1) {
                catchButton[prevButtonNumber].setVisibility(View.INVISIBLE);
            }
            prevButtonNumber = random.nextInt() % catchButton.length;
            if (prevButtonNumber < 0) prevButtonNumber = prevButtonNumber + catchButton.length;
            catchButton[prevButtonNumber].setVisibility(View.VISIBLE);
            uiThreadHandler.postDelayed(this, timeForButtonChange);
        }
    };

    private Runnable showRunnable = new Runnable() {
        @Override
        public void run() {
            for (ImageButton button : catchButton) {
                if (button.getVisibility()==View.INVISIBLE){
                    button.setVisibility(View.VISIBLE);
                    uiThreadHandler.postDelayed(this, 100);
                    break;
                }
            }
            catchButton[11].setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        counterRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiThreadHandler.removeCallbacks(counterRunnable);
        uiThreadHandler.removeCallbacks(gameRunnable);
        uiThreadHandler.removeCallbacks(showRunnable);
    }

    @Override
    public void onAttach(Context context) {
        this.listener = (OnFragmentWithTest1ClickListener) context;
        super.onAttach(context);
    }

}
