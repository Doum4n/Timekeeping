package com.example.timekeeping;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.timekeeping.databinding.FragmentCustomCalenderViewBinding;
import com.example.timekeeping.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomCalenderView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomCalenderView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentCustomCalenderViewBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private byte month = 1;

    private OnMonthChangeListener callback;

    public void setOnMonthChangeListener(OnMonthChangeListener listener) {
        this.callback = listener;
    }

    public CustomCalenderView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomCalenderView.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomCalenderView newInstance(String param1, String param2) {
        CustomCalenderView fragment = new CustomCalenderView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_custom_calender_view, container, false);
        binding = FragmentCustomCalenderViewBinding.bind(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnNextMonth.setOnClickListener(v -> {
            if (month < 12) {
                month++;
                if (callback != null) {
                    callback.onMonthChanged(month);
                }
            }
        });

        binding.btnPreviousMonth.setOnClickListener(v -> {
            if (month > 1) {
                month--;
            }
            if (callback != null) {
                callback.onMonthChanged(month);
            }
        });
    }

    public interface OnMonthChangeListener {
        void onMonthChanged(int month);
    }
}