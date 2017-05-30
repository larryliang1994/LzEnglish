package com.jiubai.lzenglish.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseAgeActivity extends BaseActivity {

    @Bind(R.id.ripple_under4)
    RippleView mUnder4Ripple;

    @Bind(R.id.textView_under4)
    TextView mUnder4TextView;

    @Bind(R.id.ripple_5_9)
    RippleView m5To9Ripple;

    @Bind(R.id.textView_5_9)
    TextView m5To9TextView;

    @Bind(R.id.ripple_up10)
    RippleView mUp10Ripple;

    @Bind(R.id.textView_up10)
    TextView mUp10TextView;

    @Bind(R.id.ripple_others)
    RippleView mOthersRipple;

    @Bind(R.id.textView_others)
    TextView mOthersTextView;

    @Bind(R.id.ripple_next)
    RippleView mNextRipple;

    @Bind(R.id.button_next)
    Button mNextButton;

    private RippleView mPreviousRipple;
    private TextView mPreviousTextView;

    private boolean mNextButtonEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_age);

        StatusBarUtil.StatusBarLightMode(this);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mNextRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (mNextButtonEnable) {
                    Intent intent = new Intent(ChooseAgeActivity.this, ChoosePreferenceVideoActivity.class);
                    UtilBox.startActivity(ChooseAgeActivity.this, intent, true);
                }
            }
        });
    }

    @OnClick({R.id.ripple_under4, R.id.ripple_5_9, R.id.ripple_up10, R.id.ripple_others})
    public void setSelection(View view) {
        mNextButtonEnable = true;
        mNextButton.setBackgroundResource(R.color.lightBlue);

        switch (view.getId()) {
            case R.id.ripple_under4:
                mUnder4Ripple.setBackgroundResource(R.drawable.round_listview_blue);
                mUnder4TextView.setTextColor(ContextCompat.getColor(this, R.color.lightBlue));
                if (mPreviousRipple != null) {
                    mPreviousRipple.setBackgroundResource(R.drawable.round_listview_white);
                    mPreviousTextView.setTextColor(Color.parseColor("#333333"));
                }
                mPreviousRipple = mUnder4Ripple;
                mPreviousTextView = mUnder4TextView;
                break;

            case R.id.ripple_5_9:
                m5To9Ripple.setBackgroundResource(R.drawable.round_listview_blue);
                m5To9TextView.setTextColor(ContextCompat.getColor(this, R.color.lightBlue));
                if (mPreviousRipple != null) {
                    mPreviousRipple.setBackgroundResource(R.drawable.round_listview_white);
                    mPreviousTextView.setTextColor(Color.parseColor("#333333"));
                }
                mPreviousRipple = m5To9Ripple;
                mPreviousTextView = m5To9TextView;
                break;

            case R.id.ripple_up10:
                mUp10Ripple.setBackgroundResource(R.drawable.round_listview_blue);
                mUp10TextView.setTextColor(ContextCompat.getColor(this, R.color.lightBlue));
                if (mPreviousRipple != null) {
                    mPreviousRipple.setBackgroundResource(R.drawable.round_listview_white);
                    mPreviousTextView.setTextColor(Color.parseColor("#333333"));
                }
                mPreviousRipple = mUp10Ripple;
                mPreviousTextView = mUp10TextView;
                break;

            case R.id.ripple_others:
                mOthersRipple.setBackgroundResource(R.drawable.round_listview_blue);
                mOthersTextView.setTextColor(ContextCompat.getColor(this, R.color.lightBlue));
                if (mPreviousRipple != null) {
                    mPreviousRipple.setBackgroundResource(R.drawable.round_listview_white);
                    mPreviousTextView.setTextColor(Color.parseColor("#333333"));
                }
                mPreviousRipple = mOthersRipple;
                mPreviousTextView = mOthersTextView;
                break;

            default:    break;
        }
    }
}
