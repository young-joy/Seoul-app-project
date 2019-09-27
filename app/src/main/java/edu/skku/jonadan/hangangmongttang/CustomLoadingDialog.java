package edu.skku.jonadan.hangangmongttang;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class CustomLoadingDialog extends Dialog {

    private Context context;
    private ImageView loadingImg;
    private AnimationDrawable anim;

    public CustomLoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        this.context = context;
    }

    @Override
    public void show() {
        super.show();
        anim.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        anim.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        loadingImg = findViewById(R.id.loading_dialog_img_view);
        loadingImg.setBackgroundResource(R.drawable.loading_img);
        anim = (AnimationDrawable) loadingImg.getBackground();
    }
}
