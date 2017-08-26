package com.jiubai.lzenglish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.presenter.ActivationPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IActivationView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivationActivity extends BaseActivity implements IActivationView {

    @Bind(R.id.editText)
    EditText mEditText;

    @Bind(R.id.button_confirm)
    Button mConformButton;

    @Bind(R.id.layout_toolbar)
    LinearLayout mToolbarLayout;

    @Bind(R.id.view_cover)
    View mCoverView;

    private int videoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        StatusBarUtil.StatusBarLightMode(this);

        videoId = getIntent().getIntExtra("videoId", 0);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mToolbarLayout.getLayoutParams();
        params.height = Config.AppbarHeight + Config.StatusbarHeight;
        mToolbarLayout.setLayoutParams(params);
        mToolbarLayout.setPadding(0, Config.StatusbarHeight, 0, 0);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEditText.getText().toString().length() != 0) {
                    mCoverView.setVisibility(View.GONE);
                } else {
                    mCoverView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.button_confirm, R.id.textView_find_code, R.id.imageView_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_confirm:
                if (mEditText.getText().toString().length() != 0) {
                    UtilBox.showLoading(this, true);
                    new ActivationPresenterImpl(this).activate(mEditText.getText().toString());
                }
                break;

            case R.id.textView_find_code:
                UtilBox.colorfulAlert(this, "激活码，在您购买的卡片套装内哦", "知道了");
                break;

            case R.id.imageView_back:
                UtilBox.returnActivity(this);
                break;
        }
    }

    @Override
    public void onActivateResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading(false);

        if (result) {
            Toast.makeText(this, "激活成功", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, PlayVideoActivity.class);
            intent.putExtra("videoId", videoId);
            intent.putExtra("fromQRScan", true);
            UtilBox.startActivity(this, intent, true);
        } else {
            UtilBox.alert(this, info, "关闭", null);
        }
    }
}
