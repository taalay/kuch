package com.tali.admin.kuch.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tali.admin.kuch.R;
import com.tali.admin.kuch.adapter.RecyclerviewAdapter;
import com.tali.admin.kuch.data.db.DBHelper;
import com.tali.admin.kuch.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private RecyclerviewAdapter mAdapter;
    private List<Post> list;
    private List<Integer> lists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        list = DBHelper.getInstance(getContext()).getAllPosts();
        mAdapter = new RecyclerviewAdapter(getActivity(), list);
        mAdapter.setOnItemClickListener(getAdapterListener());
        mRecyclerView.setAdapter(mAdapter);
        lists = new ArrayList<>();
        System.out.println("testtest");
        List<Integer> temp = new ArrayList<>();
        temp.add(5);
        temp.add(5);
        temp.add(5);
        temp.add(4);
        temp.add(4);
        temp.add(9);
        temp.add(9);
        temp.add(6);
        temp.add(22);
        temp.add(22);
        temp.add(7);
        System.out.println(makeVideoLists(temp));


        return inflate;
    }

    private RecyclerviewAdapter.OnItemClickListener getAdapterListener() {
        return new RecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
            }
        };
    }
    @Override
    public void onResume() {
        super.onPause();
        list = DBHelper.getInstance(getContext()).getAllPosts();
        RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(getActivity(), list);
        mRecyclerView.swapAdapter(recyclerviewAdapter, false);

    }
    public List<Integer> makeVideoLists(List<Integer> videoObjects){
        boolean isr = false;
        List<Integer> temp = new ArrayList<>();
        Arrays.sort(videoObjects.toArray());
        int tempInt;
        for (int i = 0; i < videoObjects.size(); i++) {
            tempInt=videoObjects.get(i);
            for (int j = i+1; j < videoObjects.size(); j++) {

                if(tempInt ==videoObjects.get(j)){
                    videoObjects.remove(j);
                    isr=true;
                }else {

                }
            }
            if (!isr){
                lists.add(videoObjects.get(i));
                temp.add(videoObjects.get(i));
                isr = false;
            }
        }
        return videoObjects;
    }

    private static int[] myRemoveDuplicates(Integer[] values) {
        boolean mask[] = new boolean[values.length];
        int removeCount = 0;

        for (int i = 0; i < values.length; i++) {
            if (!mask[i]) {
                int tmp = values[i];

                for (int j = i + 1; j < values.length; j++) {
                    if (tmp == values[j]) {
                        mask[j] = true;
                        removeCount++;
                    }
                }
            }
        }

        int[] result = new int[values.length - removeCount];

        for (int i = 0, j = 0; i < values.length; i++) {
            if (!mask[i]) {
                result[j++] = values[i];
            }
        }

        return result;
    }
}
