package com.company.shenzhou.ui.fragment.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.shenzhou.R;
import com.company.shenzhou.bean.dbbean.VideoDBBean;
import com.company.shenzhou.bean.dbbean.VideoDBBean01;
import com.company.shenzhou.utils.db.VideoDBUtils;
import com.company.shenzhou.view.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * LoveLin
 * <p>
 * Describe视频设备管理界面的adapter
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private ArrayList<VideoDBBean01> mList;
    private Context mContext;

    public VideoAdapter(ArrayList<VideoDBBean01> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setListAndNotifyDataSetChanged(List list) {
//        List list = VideoDBUtils.queryAll(VideoDBBean.class);
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    //点击事件的回调监听
    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.mClickCallBack = clickCallBack;
    }


    public interface ClickCallBack {
        void onItemCallBack(VideoDBBean01 bean, ArrayList<VideoDBBean01> mList, int position, String delete);
    }

    private ClickCallBack mClickCallBack;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_swipemenulayout_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        VideoDBBean01 bean = mList.get(position);
        //viewHolder.tv_video_title.setText("标题是===" + bean.getTitle() + "");
        //viewHolder.tv_video_information.setText("我是备注信息" + bean.getMakeMessage() + "");
        viewHolder.tv_video_title.setText("" + bean.getTitle() );
        viewHolder.tv_video_make.setText("备注:" + bean.getMakeMessage());
        viewHolder.tv_video_type.setText(""+bean.getType());

        viewHolder.linear_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickCallBack != null) {
                    mClickCallBack.onItemCallBack(bean, mList, position, "video");
                }
            }
        });

        //删除
        viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VideoDBUtils.deleteData(bean);
                //setListAndNotifyDataSetChanged();
                //查找数据库update用户表--删除当前用户
                if (mClickCallBack != null) {
                    mClickCallBack.onItemCallBack(bean, mList, position, "delete");
                }
            }
        });

        //修改数据
        viewHolder.reInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框修改数据--updateDB
                mClickCallBack.onItemCallBack(bean, mList, position, "reInput");
            }
        });      //修改数据
        viewHolder.swipeMenuLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框修改数据--updateDB
                mClickCallBack.onItemCallBack(bean, mList, position, "all");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SwipeMenuLayout swipeMenuLay;
        public LinearLayout linear_item;
        public TextView tv_video_title;
        public TextView tv_video_type;
        public TextView tv_video_make;
        public Button delBtn;
        public Button reInputBtn;

        public ViewHolder(@NonNull View view) {
            super(view);
            swipeMenuLay = (SwipeMenuLayout) view.findViewById(R.id.swipeMenuLay);
            linear_item = (LinearLayout) view.findViewById(R.id.linear_item);
            tv_video_title = (TextView) view.findViewById(R.id.tv_video_title);
            tv_video_type = (TextView) view.findViewById(R.id.tv_video_type);
            tv_video_make = (TextView) view.findViewById(R.id.tv_video_make);
            delBtn = (Button) view.findViewById(R.id.delBtn);
            reInputBtn = (Button) view.findViewById(R.id.reInputBtn);
        }
    }
}
