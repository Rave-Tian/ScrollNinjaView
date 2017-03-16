package com.rave.scrollninjaview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rave.library.NinjaViewHelper;
import com.rave.library.NotifyingRecyclerview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class RecyclerViewActivity extends Activity {
    private NotifyingRecyclerview recyclerview;
    private FrameLayout framelayout;
    private NinjaViewHelper mNinjaViewHelper;
    private RecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initView();
        fillAdapter();
    }

    private void initView() {
        framelayout = (FrameLayout) findViewById(R.id.framelayout);
        recyclerview = (NotifyingRecyclerview) findViewById(R.id.recyclerview);

        mNinjaViewHelper = new NinjaViewHelper(this, NinjaViewHelper.ViewPosition.BOTTOM);
        View ninjaView = mNinjaViewHelper.createNinjaViewOnRecyclerview(framelayout, R.id.recyclerview, R.layout.poppyview);
    }

    private void fillAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dataList.add(i + "");
        }

        adapter = new RecyclerAdapter(dataList, RecyclerViewActivity.this);
        recyclerview.setAdapter(adapter);
    }

    static class RecyclerAdapter extends RecyclerView.Adapter {
        private List<String> mDataList;
        private Context mContext;


        public RecyclerAdapter(List<String> list, Context context) {
            this.mContext = context;
            this.mDataList = list;
        }

        public void addAll(List<String> list) {
            int lastIndex = this.mDataList.size();
            if (this.mDataList.addAll(list)) {
                notifyItemRangeInserted(lastIndex, list.size());
            }
        }

        public void removeAndNotify(int position) {
            this.mDataList.remove(position);
            notifyDataSetChanged();
        }

        public void refreshAdapter(List<String> list) {
            this.mDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_song, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            final ViewHolder holder = (ViewHolder) viewHolder;
            holder.rootView.setText("RecyclerView" + position);
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView rootView;

            public ViewHolder(View rootView) {
                super(rootView);
                this.rootView = (TextView) rootView.findViewById(R.id.text);
            }

        }

    }


}
