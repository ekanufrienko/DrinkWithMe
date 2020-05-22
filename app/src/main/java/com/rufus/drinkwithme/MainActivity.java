package com.rufus.drinkwithme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity implements FragmentWithButtons.OnFragmentWithButtonsClickListener,
        FragmentTest1.OnFragmentWithTest1ClickListener, FragmentTest2.OnFragmentWithTest2ClickListener,
        FragmentWithSettings.OnFragmentWithSettingsClickListener{

    private ViewGroup firstLayout;
    private ViewGroup secondLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstLayout = findViewById(R.id.activity_main_first_container);
        secondLayout = findViewById(R.id.activity_main_second_container);

        if (null == savedInstanceState) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            FragmentWithButtons fragment_buttons = new FragmentWithButtons();
            transaction.add(firstLayout.getId(), fragment_buttons, FragmentWithButtons.class.getName());


            FragmentWithSettings fragment_settings = new FragmentWithSettings();
            transaction.add(secondLayout.getId(), fragment_settings, FragmentWithSettings.class.getName());

            transaction.commit();
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStartMakingTest(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentTest1 fragmentTest1 = new FragmentTest1();
        transaction.replace(secondLayout.getId(), fragmentTest1, FragmentTest1.class.getName());
        transaction.commit();
    }

    @Override
    public void onTest1Complete(){
        //FragmentTest2 fragmentTest2 = new FragmentTest2();
        //transaction.replace(secondLayout.getId(), fragmentTest2, FragmentTest2.class.getName());
        //transaction.commit();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentWithSettings fragment = new FragmentWithSettings();
        transaction.replace(secondLayout.getId(), fragment, FragmentWithSettings.class.getName());
        transaction.commit();
    }

    @Override
    public void onTest2Complete() {

    }

    @Override
    public void onRefresh() {
        ((FragmentWithButtons) getSupportFragmentManager().findFragmentByTag(FragmentWithButtons.class.getName()))
                .refreshProgress();
    }
}
