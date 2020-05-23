package com.rufus.drinkwithme;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class FragmentTest2 extends Fragment implements View.OnClickListener {

    private OnFragmentWithTest2ClickListener listener;
    private Handler uiThreadHandler = new Handler();
    private final Random random = new Random();

    private TextView counterTextView;
    private Button answerButton;
    private TextView textTest2;
    private Button nextButton;
    private EditText answerEditText;
    private TextView exampleTextView;

    private int counter;
    private int rightAnswer;
    private int currentAnswer;
    private byte operatorChange;

    public FragmentTest2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test2, container, false);

        counterTextView = view.findViewById(R.id.counterTextView);
        answerButton = view.findViewById(R.id.answerButton);
        textTest2 = view.findViewById(R.id.textTest2);
        nextButton = view.findViewById(R.id.nextButton_test2);
        answerEditText = view.findViewById(R.id.answerEditText);
        exampleTextView = view.findViewById(R.id.exampleTextView);

        answerButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        answerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (counter >=0){
                    onClick(answerButton);
                }
                return true;
            }
        });

        rightAnswer = 0;
        counter = 23;
        operatorChange = 1;

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton_test2:
                listener.onTest2Complete();
                break;
            case R.id.answerButton:
                String text = answerEditText.getText().toString();
                if (!text.equals("") && Integer.parseInt(text) == currentAnswer) {
                    rightAnswer++;
                }
        }
        answerEditText.setText("");
        exampleTextView.setText(makeExample());
    }

    private Runnable counterRunnable = new Runnable() {
        @Override
        public void run() {
            if (counter > 0) {
                String counterString = "Timer: " + counter + " sec";
                counterTextView.setText(counterString);
            } else {
                counterTextView.setText("");
            }
            counter--;
            if (counter >= 0) {
                uiThreadHandler.postDelayed(this, 1000);
            } else {
                answerButton.setVisibility(View.INVISIBLE);
                String result = "Result: " + rightAnswer + " correct";
                textTest2.setText(result);
                nextButton.setVisibility(View.VISIBLE);
                answerEditText.setCursorVisible(false);
                answerEditText.setFocusable(false);
            }
        }
    };


    public interface OnFragmentWithTest2ClickListener {
        void onTest2Complete();
    }

    @Override
    public void onResume() {
        super.onResume();
        exampleTextView.setText(makeExample());
        counterRunnable.run();
    }

    private String makeExample() {
        int y;
        int x;
        String example;
        if (operatorChange % 4 == 0) {
            x = random.nextInt() % 100;
            y = random.nextInt() % 100;
            if (y < 0) {
                y = y + 100;
            }
            if (x < 0) {
                x = x + 100;
            }
            currentAnswer = x + y;
            example = x + " + " + y;
        } else if (operatorChange % 4 == 1 || operatorChange % 4 == -3) {
            x = random.nextInt() % 100;
            y = random.nextInt() % 10;
            if (y < 0) {
                y = y + 10;
            } else if (y == 0) {
                y = 1;
            }
            if (x < 0) {
                x = x + 100;
            }
            currentAnswer = x;
            example = x * y + " / " + y;
        } else if (operatorChange % 4 == 2 || operatorChange % 4 == -2) {
            x = random.nextInt() % 100;
            y = random.nextInt() % 10;
            if (y * x < 0) {
                y = -y;
            }
            if (y < 0) {
                y = y + 10;
            }
            if (x < 0) {
                x = x + 100;
            }
            currentAnswer = x * y;
            example = x + " * " + y;
        } else {
            x = random.nextInt() % 100;
            y = random.nextInt() % 100;
            if (y < 0) {
                y = y + 100;
            }
            if (x < 0) {
                x = x + 100;
            }
            if (y < x) {
                currentAnswer = x - y;
                example = x + " - " + y;
            } else {
                currentAnswer = y - x;
                example = y + " - " + x;
            }
        }
        operatorChange++;
        return example;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiThreadHandler.removeCallbacks(counterRunnable);
    }

    @Override
    public void onAttach(Context context) {
        this.listener = (OnFragmentWithTest2ClickListener) context;
        super.onAttach(context);
    }

}