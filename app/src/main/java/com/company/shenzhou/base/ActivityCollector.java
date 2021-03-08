package com.company.shenzhou.base;


import androidx.fragment.app.FragmentActivity;

import java.util.LinkedList;

/**
 * 界面控制管理类，活动管理器
 * 解释文档，郭霖 第一行代码 第2版 2.6.2
 * package_name com.lzyc.ybtappcal.app
 *
 * @author yang
 */
public class ActivityCollector {

    private static ActivityCollector instance;
    private static LinkedList<FragmentActivity> activityList;
    private static LinkedList<FragmentActivity> activityListRegister;//用户注册相关
    public static LinkedList<FragmentActivity> activityListOrder;//检查订单界面相关
    public static LinkedList<FragmentActivity> activityListOrderAbout;//所有购买订单相关


    public ActivityCollector() {
        if (activityList == null) {
            activityList = new LinkedList<FragmentActivity>();
        }
        if (activityListRegister == null) {
            activityListRegister = new LinkedList<FragmentActivity>();
        }
        if (activityListOrder == null) {
            activityListOrder = new LinkedList<FragmentActivity>();
        }
        if (activityListOrderAbout == null) {
            activityListOrderAbout = new LinkedList<FragmentActivity>();
        }
    }

    public static ActivityCollector getInstance() {
        if (instance == null) {
            instance = new ActivityCollector();
        }
        return instance;
    }

    public void addActivity(FragmentActivity acy) {
        activityList.add(acy);
    }

    public void removeActivityRegister(FragmentActivity acy) {
        if (activityListRegister == null) {
            return;
        }
        if (activityListRegister.contains(acy)) {
            activityListRegister.remove(acy);
        }
    }

    public void removeActivityOrder(FragmentActivity acy) {
        if (activityListOrder == null) {
            return;
        }
        if (activityListOrder.contains(acy)) {
            activityListOrder.remove(acy);
        }
    }

    public void removeActivityOrderAbout(FragmentActivity acy) {
        if (activityListOrderAbout == null) {
            return;
        }
        if (activityListOrderAbout.contains(acy)) {
            activityListOrderAbout.remove(acy);
        }
    }

    public void removeActivity(FragmentActivity acy) {
        if (activityList == null) {
            return;
        }
        if (activityList.contains(acy)) {
            activityList.remove(acy);
        }
    }

    public void addActivityOrder(FragmentActivity acy) {
        activityListOrder.add(acy);
    }

    public void addActivityRegiser(FragmentActivity acy) {
        activityListRegister.add(acy);
    }

    public void activityOrderAbout(FragmentActivity acy) {
        activityListOrderAbout.add(acy);
    }


    /**
     * 销毁所有
     */
    public static void removeAll() {
        FragmentActivity acy;
        while (activityList.size() != 0) {
            //poll(),方法检索并移除此列表的头元素(第一个元素)
            acy = activityList.poll();
            //isFinishing(),判断当前activity是否被干掉;
            //判断当前activity是否被干掉,不然会出现null
            if (acy != null && !acy.isFinishing()) {
                acy.finish();
            }
        }
        removeOrderAll();
        removeActivityOrderAbout();
        removeRegisterAll();

    }

    /**
     * 销毁注册相关界面
     */
    public static void removeRegisterAll() {
        if (activityListRegister == null) {
            return;
        }
        FragmentActivity acy;
        while (activityListRegister.size() != 0) {
            acy = activityListRegister.poll();
            if (acy != null && !acy.isFinishing()) {
                acy.finish();
            }
        }
    }

    /**
     * 销毁检查订单界面
     */
    public static void removeOrderAll() {
        if (activityListOrder == null) {
            return;
        }
        FragmentActivity acy;
        while (activityListOrder.size() != 0) {
            acy = activityListOrder.poll();
            if (acy != null && !acy.isFinishing()) {
                acy.finish();
            }
        }
    }

    /**
     * 清空购买相关界面，返回首页
     */
    public static void removeActivityOrderAbout() {
        if (activityListOrderAbout == null) {
            return;
        }
        FragmentActivity acy;
        while (activityListOrderAbout.size() != 0) {
            acy = activityListOrderAbout.poll();
            if (acy != null && !acy.isFinishing()) {
                acy.finish();
            }
        }
    }
}
