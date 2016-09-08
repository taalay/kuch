package com.tali.admin.kuch.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tali.admin.kuch.R;
import com.tali.admin.kuch.adapter.RecyclerviewAdapter;
import com.tali.admin.kuch.data.ThemeData;
import com.tali.admin.kuch.data.db.DBHelper;
import com.tali.admin.kuch.model.Post;

import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private RecyclerviewAdapter mAdapter;
    private List<Post> list;
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
        mRecyclerView.swapAdapter(recyclerviewAdapter,false);

    }

}
