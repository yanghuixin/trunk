package com.witiot.cloudbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.witiot.cloudbox.R;
import com.witiot.cloudbox.model.NewsListIndexRes;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.statusbar.SystemBarTintManager;
import com.witiot.cloudbox.views.yjzx.NewsListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin
 */
public class ChannelViewPageAdapter extends PagerAdapter {

    private List<NewsListIndexRes.ItemBean> items = new ArrayList<>();
    private Context context;
    RecycleViewDivider divider;
    public ChannelViewPageAdapter(Context context) {
        this.context = context;
        int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.layout_margin_1);
         divider = new RecycleViewDivider(
                context, LinearLayoutManager.VERTICAL, spacingInPixels, ContextCompat.getColor(context, R.color.bg_grey));
    }

    public void setData(List<NewsListIndexRes.ItemBean> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size() > 0 ? items.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.item_channel, view, false);
        TextView childMore = (TextView) rootView.findViewById(R.id.child_more);
        TextView childTitle = (TextView) rootView.findViewById(R.id.child_title);
        RelativeLayout child_lay = (RelativeLayout) rootView.findViewById(R.id.child_title_rl);

        RecyclerView childRecyclerview = (RecyclerView) rootView.findViewById(R.id.child_recyclerview);

        childRecyclerview.addItemDecoration(divider);
        NewsListPageAdapter childAdapter = new NewsListPageAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        childRecyclerview.setLayoutManager(layoutManager);
        childAdapter.setData(items.get(position).getData());
        childRecyclerview.setAdapter(childAdapter);
        childTitle.setText(items.get(position).getName());
        childMore.setOnClickListener(new MoreClickListener(items.get(position).getName()));

            if (items.get(position).getName().equals("就医指南")) {
                child_lay.setBackgroundResource( R.mipmap.bk_1_jyzn );
            } else if (items.get(position).getName().equals("健康速递")) {
                child_lay.setBackgroundResource( R.mipmap.bk_2_jksd );
            } else if (items.get(position).getName().equals("保健饮食")) {
                child_lay.setBackgroundResource( R.mipmap.bk_3_bjys );
            } else if (items.get(position).getName().equals("减肥健身")) {
                child_lay.setBackgroundResource( R.mipmap.bk_4_jfjs );
            } else if (items.get(position).getName().equals("心理体验")) {
                child_lay.setBackgroundResource( R.mipmap.bk_5_xlty );
            } else if (items.get(position).getName().equals("整形美容")) {
                child_lay.setBackgroundResource( R.mipmap.bk_6_zxml );
            }


        view.addView(rootView, 0);

        return rootView;
    }

    private class MoreClickListener implements View.OnClickListener{
        private String name;

        public MoreClickListener(String name) {
            this.name = name;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, NewsListActivity.class);
            if (name.equals("健康速递")) {
                intent.putExtra("tabType", "健康速递");
            } else if (name.equals("保健饮食")) {
                intent.putExtra("tabType", "保健饮食");
            } else if (name.equals("减肥健身")) {
                intent.putExtra("tabType", "减肥健身");
            } else if (name.equals("心理体验")) {
                intent.putExtra("tabType", "心理体验");
            } else if (name.equals("整形美容")) {
                intent.putExtra("tabType", "整形美容");
            } else if (name.equals("就医指南")) {
                intent.putExtra("tabType", "就医指南");
            }
            context.startActivity(intent);
        }
    }


    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
