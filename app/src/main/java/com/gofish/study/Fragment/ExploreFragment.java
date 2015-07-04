package com.gofish.study.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gofish.study.R;

public class ExploreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        String content = getArguments().getString("content");
        ((TextView)view.findViewById(R.id.txt_content)).setText(content);
        return view;
    }

    public static ExploreFragment newInstance(String content) {
        ExploreFragment fragment = new ExploreFragment();

        Bundle args = new Bundle();
        args.putString("content", content);
        fragment.setArguments(args);
        return fragment;
    }
}
