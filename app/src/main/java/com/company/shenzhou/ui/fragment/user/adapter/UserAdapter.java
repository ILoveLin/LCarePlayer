package com.company.shenzhou.ui.fragment.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.shenzhou.R;
import com.company.shenzhou.bean.dbbean.UserDBRememberBean;
import com.company.shenzhou.utils.db.UserDBRememberBeanUtils;
import com.company.shenzhou.utils.db.UserDBUtils;
import com.company.shenzhou.view.SwipeMenuLayout;
import com.company.shenzhou.view.SwitchButton;
import com.yun.common.utils.LogUtils;
import com.yun.common.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * LoveLin
 * Describe
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<UserDBRememberBean> mList = new ArrayList<>();
    private Context mContext;
    private String type;  //默认删除

    public UserAdapter(ArrayList<UserDBRememberBean> mList, Context mContext) {
        this.type = "default";
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setListAndNotifyDataSetChanged() {
        List currentList = UserDBUtils.queryAll(UserDBRememberBean.class);
        Log.e("currentList", "currentList====" + currentList.size());
        mList.clear();
        mList.addAll(currentList);
        notifyDataSetChanged();
    }

    //创建新View，被LayoutManager所调用
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_swipemenulayout_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        UserDBRememberBean bean = mList.get(position);
        int userItemType = bean.getUserType();
        int currentUserType = (int) SharePreferenceUtil.get(mContext, SharePreferenceUtil.Current_UserType, 0);
        viewHolder.tv_text.setText("" + bean.getUsername());
        //0普通  1权限  2超级用户
        switch (bean.getUserType()) {
            case 0:
                viewHolder.tv_text_type.setText("(普通用户)");
                break;
            case 1:
                viewHolder.tv_text_type.setText("(权限用户)");
                break;
            case 2:
                viewHolder.tv_text_type.setText("(超级用户)");
                break;
        }

        if (userItemType == 0) {
            //这么写是避免 computing a layout or scrolling  Bug
            mClickCallBack.onItemChecked(bean, viewHolder.slide_switch, false);
        } else {
            mClickCallBack.onItemChecked(bean, viewHolder.slide_switch, true);
        }
        Boolean isChange = (Boolean) SharePreferenceUtil.get(mContext, SharePreferenceUtil.Current_Admin_ChangePassword, false);
        if (userItemType == 2) {
            viewHolder.slide_switch.setVisibility(View.INVISIBLE);
            viewHolder.change_password.setVisibility(View.INVISIBLE);
        } else {
            //删除
            viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //查找数据库update用户表--删除当前用户
                    if (mClickCallBack != null) {
                        mClickCallBack.onItemCallBack(bean, mList, "delete");
                    }
                }
            });
            //修改密码
            viewHolder.change_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickCallBack != null) {
                        mClickCallBack.onItemCallBack(bean, mList, "password");
                    }
                }
            });
        }
        //设置是否是权限用户
        //0普通用户、1权限用户、2超级管理员  默认为0-普通用户
        /**
         * 进来的时候判断当前系统用户类型和当前item类型 然后做相对于的权限判断
         */
        //设置用户权限的判断
        if (currentUserType == 2 && bean.getUserType() == 2) {
            viewHolder.slide_switch.setEnabled(false);  //超级用户对自己不能使用
        } else if (currentUserType == 2 && bean.getUserType() < 2) {
            viewHolder.slide_switch.setEnabled(true);
        } else {
            viewHolder.slide_switch.setEnabled(false);
        }
        LogUtils.e("TAG====adapter==currentUserType===" + currentUserType + "==userItemType==" + userItemType);
        //设置权限
        viewHolder.slide_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean isChecked) {
                //设置是否是权限用户
                //0普通用户、1权限用户、2超级管理员  默认为0-普通用户
                if (currentUserType == 2 && userItemType < 2) {  //只有超级管理员才可以设置权限
                    viewHolder.slide_switch.setEnabled(true);
                    if (isChecked) {
                        bean.setUserType(1);
                    } else {
                        bean.setUserType(0);
                    }
                    UserDBRememberBeanUtils.updateData(bean);
                    setListAndNotifyDataSetChanged();
                }
//                else if (currentUserType == 2 && userItemType == 2) {
//                    if (mClickCallBack != null) {
//                        mClickCallBack.onItemToast("不能对自己操作");
//                    }
////                    Toast.makeText(mContext, "不能对自己操作", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (mClickCallBack != null) {
//                        mClickCallBack.onItemToast("暂无权限");
//                    }
////                    Toast.makeText(mContext, "暂无权限", Toast.LENGTH_SHORT).show();
//                }


            }
        });

        viewHolder.swipeMenuLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickCallBack != null) {
                    mClickCallBack.onItemCallBack(bean, mList, "all");

                }
            }
        });
    }


    //点击事件的回调监听
    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.mClickCallBack = clickCallBack;
    }


    public interface ClickCallBack {
        void onItemCallBack(UserDBRememberBean bean, ArrayList<UserDBRememberBean> mList, String type);

        void onItemChecked(UserDBRememberBean bean, SwitchButton slide_switch, boolean type);

        void onItemToast(String str);
    }

    private ClickCallBack mClickCallBack;

    //解决复用滑动开关bug
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SwipeMenuLayout swipeMenuLay;
        public TextView tv_text;
        public TextView tv_text_type;
        public TextView change_password;
        public Button delBtn;
        public SwitchButton slide_switch;

        public ViewHolder(@NonNull View view) {
            super(view);
            swipeMenuLay = (SwipeMenuLayout) view.findViewById(R.id.swipeMenuLay);
            tv_text = (TextView) view.findViewById(R.id.tv_text);
            tv_text_type = (TextView) view.findViewById(R.id.tv_text_type);
            change_password = (TextView) view.findViewById(R.id.change_password);
            delBtn = (Button) view.findViewById(R.id.delBtn);
            slide_switch = (SwitchButton) view.findViewById(R.id.slide_switch);
        }
    }
}
