package com.tc.frescousedemo;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView urlImg, circleImg, roundImg, diffRoundImg, layRoundImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        //从网络下载图片显示
        Uri imgUrl = Uri.parse("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
        urlImg.setImageURI(imgUrl);

        //显示圆形图片
        circleImg.setImageURI(imgUrl);

        //代码中设置圆角
//        RoundingParams bitmapOnlyParams = RoundingParams.fromCornersRadius(40)
//                .setRoundingMethod(RoundingParams.RoundingMethod.BITMAP_ONLY);
//        roundImg.getHierarchy().setRoundingParams(bitmapOnlyParams);


        //设置不同大小的圆角，只能在代码中设置
        RoundingParams diffParams = RoundingParams.fromCornersRadii(50, 100, 150, 200);
        diffRoundImg.getHierarchy().setRoundingParams(diffParams);

        //使用overlayColor方式设置圆角
        RoundingParams overlayColor = RoundingParams.fromCornersRadius(80)
                .setRoundingMethod(RoundingParams.RoundingMethod.OVERLAY_COLOR)
                .setOverlayColor(Color.WHITE);//设置覆盖层颜色，一般设置为背景色，这里为了区别，设置为白色
        layRoundImg.getHierarchy().setRoundingParams(overlayColor);
    }

    private void initViews() {
        urlImg = (SimpleDraweeView) findViewById(R.id.sdv_image_url);
        circleImg = (SimpleDraweeView) findViewById(R.id.sdv_image_circle);
        roundImg = (SimpleDraweeView) findViewById(R.id.sdv_image_round);
        diffRoundImg = (SimpleDraweeView) findViewById(R.id.sdv_image_diff_round);
        layRoundImg = (SimpleDraweeView) findViewById(R.id.sdv_image_lay);
    }
}
