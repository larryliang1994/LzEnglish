package com.jiubai.lzenglish.widget.recorder.manager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiubai.lzenglish.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogManager {

    /**
     * 以下为dialog的初始化控件，包括其中的布局文件
     */

    private Dialog mDialog;

    @Bind(R.id.imageView_recording)
    ImageView mRecordingImageView;

    @Bind(R.id.imageView_volume)
    ImageView mVolumeImageView;

    @Bind(R.id.imageView_cancel)
    ImageView mCancelImageView;

    @Bind(R.id.textView_cancel)
    TextView mCancelTextView;

    @Bind(R.id.textView_content)
    TextView mContentTextView;

    private Context mContext;

    public DialogManager(Context context) {
        mContext = context;

        mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
        // 用layoutinflater来引用布局
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_manager, null);
        mDialog.setContentView(view);

        ButterKnife.bind(this, view);
    }

    public void showRecordingDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
            // 用layoutinflater来引用布局
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.dialog_manager, null);
            mDialog.setContentView(view);

            ButterKnife.bind(this, view);
        }

        mDialog.show();
    }

    /**
     * 设置正在录音时的dialog界面
     */
    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mRecordingImageView.setVisibility(View.VISIBLE);
            mVolumeImageView.setVisibility(View.VISIBLE);
            mCancelImageView.setVisibility(View.GONE);
            mCancelTextView.setBackgroundColor(Color.TRANSPARENT);
            mCancelTextView.setText("手指上滑，取消录音");
        }
    }

    /**
     * 取消界面
     */
    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mRecordingImageView.setVisibility(View.GONE);
            mVolumeImageView.setVisibility(View.GONE);
            mCancelImageView.setImageResource(R.drawable.cancel_record);
            mCancelImageView.setVisibility(View.VISIBLE);
            mCancelTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_searchbox_red));
            mCancelTextView.setText("松开手指，取消录音");
        }

    }

    // 时间过短
    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mRecordingImageView.setVisibility(View.GONE);
            mVolumeImageView.setVisibility(View.GONE);
            mCancelImageView.setImageResource(R.drawable.danger);
            mCancelImageView.setVisibility(View.VISIBLE);
            mCancelTextView.setBackgroundColor(Color.TRANSPARENT);
            mCancelTextView.setText("录音时间太短");
        }

    }

    // 隐藏dialog
    public void dimissDialog() {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }

    public void updateVoiceLevel(int level) {
        if (level > 10) {
            level = 9;
        }

        if (mDialog != null && mDialog.isShowing()) {

            //通过level来找到图片的id，也可以用switch来寻址，但是代码可能会比较长
            int resId = mContext.getResources().getIdentifier("volume_" + level,
                    "drawable", mContext.getPackageName());
            mVolumeImageView.setImageResource(resId);
        }
    }

    public TextView getCancelTextView() {
        return mCancelTextView;
    }

    public void setCancelTextView(TextView mCancelTextView) {
        this.mCancelTextView = mCancelTextView;
    }

    public void setContentTextView(String content) {
        this.mContentTextView.setText(content);
    }
}
