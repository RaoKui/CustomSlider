package com.raokui.customslider;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final ViewPager viewPager = findViewById(R.id.viewpager);
//        viewPager.setAdapter(new VPagerAdapter());
//        viewPager.setOffscreenPageLimit(4);
//        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View page, float position) {
//                if (position < -1) {
//                    page.setScaleY(0.9f);
//                } else if (position <= 1) {
//                    float scale = Math.max(0.9f, 1 - Math.abs(position));
//                    page.setScaleY(scale);
//                } else {
//                    page.setScaleY(0.9f);
//                }
//
//            }
//        });
//        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            private float reduceX = 0.0f;
//            private float itemWidth = 0;
//            private float offsetPosition = 0f;
//            private int mCoverWidth;
//            private float mScaleMax = 1.0f;
//            private float mScaleMin = 0.9f;
////            private ViewPager mViewPager;
////            public CoverModeTransformer(ViewPager pager){
////                mViewPager = pager;
////            }
//
//            @Override
//            public void transformPage(View view, float position) {
//                if (offsetPosition == 0f) {
//                    float paddingLeft = viewPager.getPaddingLeft();
//                    float paddingRight = viewPager.getPaddingRight();
//                    float width = viewPager.getMeasuredWidth();
//                    offsetPosition = paddingLeft / (width - paddingLeft - paddingRight);
//                }
//                float currentPos = position - offsetPosition;
//                if (itemWidth == 0) {
//                    itemWidth = view.getWidth();
//                    //由于左右边的缩小而减小的x的大小的一半
//                    reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f;
//                }
//                if (currentPos <= -1.0f) {
//                    view.setTranslationX(reduceX + mCoverWidth);
//                    view.setScaleX(mScaleMin);
//                    view.setScaleY(mScaleMin);
//                } else if (currentPos <= 1.0) {
//                    float scale = (mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos));
//                    float translationX = currentPos * -reduceX;
//                    if (currentPos <= -0.5) {//两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
//                        view.setTranslationX(translationX + mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
//                    } else if (currentPos <= 0.0f) {
//                        view.setTranslationX(translationX);
//                    } else if (currentPos >= 0.5) {//两个view中间的临界，这时两个view在同一层
//                        view.setTranslationX(translationX - mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
//                    } else {
//                        view.setTranslationX(translationX);
//                    }
//                    view.setScaleX(scale + mScaleMin);
//                    view.setScaleY(scale + mScaleMin);
//                } else {
//                    view.setScaleX(mScaleMin);
//                    view.setScaleY(mScaleMin);
//                    view.setTranslationX(-reduceX - mCoverWidth);
//                }
//
//            }
//        });

        for (int i = 0; i < 3; i++) {
            titles.add(i + "");
        }

        CustomSliderView<String> customSliderView = findViewById(R.id.csv);
        customSliderView.setPages(titles, new HolderCreator() {
            @Override
            public CustomHolder createHolder() {
                return new Holder();
            }
        });
    }

    private static class Holder implements CustomHolder<String> {
        private ImageView mImageView;

        private int imageViews[] = {R.mipmap.one, R.mipmap.two, R.mipmap.three};

        @Override
        public View create(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, null);
            mImageView = view.findViewById(R.id.iv);
            return view;
        }

        @Override
        public void bindData(Context context, int position, String data) {
            mImageView.setImageResource(imageViews[position]);
        }
    }

    private ArrayList titles = new ArrayList();

    private SparseArray<View> childIndex = new SparseArray<View>();



}
