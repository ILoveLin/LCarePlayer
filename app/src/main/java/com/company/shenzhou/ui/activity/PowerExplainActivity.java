package com.company.shenzhou.ui.activity;

import android.view.View;

import com.company.shenzhou.R;
import com.company.shenzhou.base.BaseActivity;

/**
 * LoveLin
 * <p>
 * Describe权限说明
 */
public class PowerExplainActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_power_explain;
    }

    @Override
    public void init() {
        setTitleBarVisibility(View.VISIBLE);
        setTitleLeftBtnVisibility(View.VISIBLE);
        setTitleRightBtnVisibility(View.INVISIBLE);
        setTitleName("权限说明");
        setPageStateView();

    }
}
