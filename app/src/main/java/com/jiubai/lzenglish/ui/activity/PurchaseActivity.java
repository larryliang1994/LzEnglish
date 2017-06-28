package com.jiubai.lzenglish.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.CouponAdapter;
import com.jiubai.lzenglish.bean.PurchaseInfo;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.presenter.PurchasePresenterImpl;
import com.jiubai.lzenglish.ui.iview.IPurchaseView;
import com.kyleduo.switchbutton.SwitchButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseActivity extends BaseActivity implements IPurchaseView {

    @Bind(R.id.textView_price)
    TextView mPriceTextView;

    @Bind(R.id.textView_total_price)
    TextView mTotalPriceTextView;

    @Bind(R.id.textView_title)
    TextView mTitleTextView;

    @Bind(R.id.imageView)
    ImageView mImageView;

    @Bind(R.id.textView_coupon)
    TextView mCouponTextView;

    @Bind(R.id.textView_discount)
    TextView mDiscountTextView;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.switchButton)
    SwitchButton mSwitchButton;

    @Bind(R.id.view_cover)
    View mCoverView;

    private ProgressDialog progressDialog;

    private CouponAdapter mAdapter;
    private PurchaseInfo mPurchaseInfo;

    private float currentPrice = 0;
    private int seasonId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        StatusBarUtil.StatusBarLightMode(this);

        ButterKnife.bind(this);

        UtilBox.showLoading(this, false);

        initView();

        seasonId = getIntent().getIntExtra("seasonId", -1);

        new PurchasePresenterImpl(this).getPurchaseInfo(seasonId);
    }

    private void initView() {
        mCoverView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mAdapter.currentIndex != -1) {
                        mDiscountTextView.setText("￥" + String.format("%.2f",
                                mPurchaseInfo.getCoupons().get(mAdapter.currentIndex).getPrice()));

                        currentPrice = mPurchaseInfo.getPrice() -
                                mPurchaseInfo.getCoupons().get(mAdapter.currentIndex).getPrice();
                        if (currentPrice < 0) {
                            currentPrice = 0;
                        }

                        mTotalPriceTextView.setText("￥" + String.format("%.2f", currentPrice));
                    }
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mDiscountTextView.setText("￥0.00");

                    currentPrice = mPurchaseInfo.getPrice();

                    mTotalPriceTextView.setText("￥" + String.format("%.2f", currentPrice));
                }
            }
        });
    }

    @OnClick({R.id.button_submit, R.id.imageView_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                progressDialog.show();

                if (mAdapter.currentIndex != -1) {
                    new PurchasePresenterImpl(this).didPurchase(
                            seasonId,
                            mPurchaseInfo.getCoupons().get(mAdapter.currentIndex).getId()
                    );
                } else {
                    new PurchasePresenterImpl(this).didPurchase(seasonId, -1);
                }

                break;

            case R.id.imageView_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        UtilBox.returnActivity(this);
    }

    @Override
    public void onGetPurchaseInfoResult(boolean result, String info, Object extras) {
        if (result) {
            mPurchaseInfo = (PurchaseInfo) extras;

            ImageLoader.getInstance().displayImage(Config.ResourceUrl + mPurchaseInfo.getImage(), mImageView);

            mTitleTextView.setText("购买 " + mPurchaseInfo.getName());
            mPriceTextView.setText("￥" + String.format("%.2f", mPurchaseInfo.getPrice()));

            currentPrice = mPurchaseInfo.getPrice();

            mCouponTextView.setText("剩余" + mPurchaseInfo.getCoupons().size() + "张");

            mTotalPriceTextView.setText("￥" + String.format("%.2f", mPurchaseInfo.getPrice()));

            mAdapter = new CouponAdapter(this, mPurchaseInfo.getCoupons());

            mAdapter.setItemClickListener(new CouponAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mDiscountTextView.setText("￥" + String.format("%.2f", mPurchaseInfo.getCoupons().get(position).getPrice()));

                    currentPrice = mPurchaseInfo.getPrice() - mPurchaseInfo.getCoupons().get(position).getPrice();
                    if (currentPrice < 0) {
                        currentPrice = 0;
                    }

                    mTotalPriceTextView.setText("￥" + String.format("%.2f", currentPrice));
                }
            });

            mRecyclerView.setAdapter(mAdapter);

            if (mPurchaseInfo.getCoupons().size() == 0) {
                mSwitchButton.setEnabled(false);
            } else {
                mSwitchButton.setEnabled(true);
            }

            mCoverView.setVisibility(View.GONE);

            UtilBox.dismissLoading(false);
        } else {
            UtilBox.dismissLoading(false);

            UtilBox.alert(this, info,
                    "重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new PurchasePresenterImpl(PurchaseActivity.this).getPurchaseInfo(seasonId);
                        }
                    },
                    "返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilBox.returnActivity(PurchaseActivity.this);
                        }
                    });
        }
    }

    @Override
    public void onDoPurchaseResult(boolean result, String info, Object extras) {
        progressDialog.dismiss();

        if (result) {

        } else {
            UtilBox.dismissLoading(false);

            UtilBox.alert(this, info,
                    "重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.show();

                            if (mAdapter.currentIndex != -1) {
                                new PurchasePresenterImpl(PurchaseActivity.this).didPurchase(
                                        seasonId,
                                        mPurchaseInfo.getCoupons().get(mAdapter.currentIndex).getId()
                                );
                            } else {
                                new PurchasePresenterImpl(PurchaseActivity.this).didPurchase(seasonId, -1);
                            }
                        }
                    },
                    "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
    }
}
