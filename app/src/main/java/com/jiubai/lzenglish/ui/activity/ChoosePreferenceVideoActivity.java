package com.jiubai.lzenglish.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChoosePreferenceVideoActivity extends BaseActivity {

    @Bind(R.id.ripple_1)
    RippleView mRipple1;

    @Bind(R.id.ripple_2)
    RippleView mRipple2;

    @Bind(R.id.ripple_3)
    RippleView mRipple3;

    @Bind(R.id.ripple_4)
    RippleView mRipple4;

    @Bind(R.id.ripple_5)
    RippleView mRipple5;

    @Bind(R.id.ripple_6)
    RippleView mRipple6;

    @Bind(R.id.ripple_7)
    RippleView mRipple7;

    @Bind(R.id.ripple_8)
    RippleView mRipple8;

    @Bind(R.id.ripple_9)
    RippleView mRipple9;

    @Bind(R.id.ripple_next)
    RippleView mNextRipple;

    private int[] mRipples = {R.id.ripple_1, R.id.ripple_2, R.id.ripple_3, R.id.ripple_4,
            R.id.ripple_5, R.id.ripple_6, R.id.ripple_7, R.id.ripple_8, R.id.ripple_9};
    private int[] mTextViews = {R.id.textView_1, R.id.textView_2, R.id.textView_3, R.id.textView_4,
            R.id.textView_5, R.id.textView_6, R.id.textView_7, R.id.textView_8, R.id.textView_9};
    private boolean[] mSelected = {false, false, false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_preference_video);

        StatusBarUtil.StatusBarLightMode(this);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mNextRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(ChoosePreferenceVideoActivity.this, HomeActivity.class);
                UtilBox.startActivity(ChoosePreferenceVideoActivity.this, intent, true);
            }
        });
    }

    @OnClick({R.id.ripple_1, R.id.ripple_2, R.id.ripple_3, R.id.ripple_4,
            R.id.ripple_5, R.id.ripple_6, R.id.ripple_7, R.id.ripple_8, R.id.ripple_9})
    public void onClick(View rippleView) {
        if (rippleView.getId() != R.id.ripple_next) {
            int index = getRippleIndex(rippleView.getId());
            if (mSelected[index]) {
                rippleView.setBackgroundResource(R.drawable.round_listview_white);
                ((TextView)findViewById(mTextViews[index])).setTextColor(ContextCompat.getColor(this, R.color.primaryText));
            } else {
                rippleView.setBackgroundResource(R.drawable.round_listview_blue);
                ((TextView)findViewById(mTextViews[index])).setTextColor(ContextCompat.getColor(this, R.color.lightBlue));
            }

            mSelected[index] = !mSelected[index];
        }
    }

    private int getRippleIndex(int id) {
        for (int i = 0; i < mRipples.length; i++) {
            if (mRipples[i] == id) {
                return i;
            }
        }

        return 0;
    }
}
