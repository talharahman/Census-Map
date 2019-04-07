package com.example.censusmap.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.censusmap.R;
import com.example.censusmap.repositiory.FragmentInterface;


public final class SplashFragment extends Fragment {

    private FragmentInterface listener;
    public static final int TIMER_DURATION = 5000;
    public static final int TIMER_INTERVAL = 1000;

    public SplashFragment() {}

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterface) {
          //  listener = (FragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement interface");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CountDownTimer countDownTimer = new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
            //    listener.moveToMainScreen();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
