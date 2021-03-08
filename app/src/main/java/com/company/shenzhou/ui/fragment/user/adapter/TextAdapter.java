package com.company.shenzhou.ui.fragment.user.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.company.shenzhou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghejie on 15/11/26.
 */
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    public void setData(ArrayList<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(String string);

        void onLinearItemClick(String string);
    }

    public ArrayList<String> datas = null;
    private ItemClickCallBack clickCallBack;
    private Activity activity;

    public TextAdapter(ArrayList<String> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        viewHolder.mTextView.setText("      " + datas.get(position));
        viewHolder.linear_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.onLinearItemClick(datas.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public LinearLayout linear_item;


        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_item);
            linear_item = (LinearLayout) view.findViewById(R.id.linear_item);

        }
    }
}





















