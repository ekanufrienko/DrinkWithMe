package com.rufus.drinkwithme;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class FragmentTest2 extends Fragment implements View.OnClickListener {

    private OnFragmentWithTest2ClickListener listener;
    private Handler uiThreadHandler = new Handler();
    final Random random = new Random();


    public FragmentTest2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test1, container, false);


        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentWithTest2ClickListener {
        void onTest2Complete();
    }


    @Override
    public void onAttach(Context context) {
        this.listener = (OnFragmentWithTest2ClickListener) context;
        super.onAttach(context);
    }

}