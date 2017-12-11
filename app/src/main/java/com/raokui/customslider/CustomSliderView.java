package com.raokui.customslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 饶魁 on 2017/12/11.
 */

public class CustomSliderView<T> extends RelativeLayout {

    private static final String TAG = "CustomSliderView";

    private List<T> mDataList;
    private CustomSliderAdapter mAdapter;

    public CustomSliderView(Context context) {
        super(context);
        init();
    }

    public CustomSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        readAttrs(context, attrs);
    }

    public CustomSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        readAttrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomSliderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        readAttrs(context, attrs);
    }

    private ViewPager mvpBanner;

    private void init() {
        // 初始化ViewPager
        View view = LayoutInflater.from(getContext()).inflate(R.layout.banner_layout, this, true);
        mvpBanner = view.findViewById(R.id.vp_banner);
        initViewPagerScroll();
        mvpBanner.setOffscreenPageLimit(4);// 设置可预览的页面，当前页面的左右都可预加载3个页面
        mvpBanner.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position < -1) {
                    page.setScaleY(0.9f);
                } else if (position <= 1) {
                    float scale = Math.max(0.9f, 1 - Math.abs(position));
                    page.setScaleY(scale);
                } else {
                    page.setScaleY(0.9f);
                }

            }
        });

    }

    private float mSide_scale;

    private float mSlider_space;

    private boolean mIsCanLoop;


    private static final float DEFAULT_SIDE_SCALE = 1.0F;

    private static final float DEFAULT_SLIDER_SPACE = 0.0F;


    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSliderView);
        // banner间距,左右缩放倍数
        mSide_scale = typedArray.getFloat(R.styleable.CustomSliderView_side_scale, DEFAULT_SIDE_SCALE);
        mSlider_space = typedArray.getFloat(R.styleable.CustomSliderView_slider_space, DEFAULT_SLIDER_SPACE);
        mIsCanLoop = typedArray.getBoolean(R.styleable.CustomSliderView_can_loop, false);
    }

    private int mCurrent_item;

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mCurrent_item = mvpBanner.getCurrentItem();
            mCurrent_item++;
            if (mCurrent_item == mAdapter.getCount()) {
                mCurrent_item = 0;
                mvpBanner.setCurrentItem(mCurrent_item, false);
                mHandler.postDelayed(this, 3000);
            } else {
                mvpBanner.setCurrentItem(mCurrent_item);
                mHandler.postDelayed(this, 3000);
            }
        }
    };

    private Handler mHandler = new Handler();

    public void setPages(List<T> datas, HolderCreator holderCreator) {
        if (datas == null || holderCreator == null) {
            return;
        }

        mDataList = datas;

        mAdapter = new CustomSliderAdapter(mDataList, holderCreator);
        mvpBanner.setAdapter(mAdapter);
        mAdapter.setDatas(mDataList);
        mHandler.postDelayed(mRunnable, 3000);

    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mViewPagerScroller = new ViewPagerScroller(
                    mvpBanner.getContext());
            mScroller.set(mvpBanner, mViewPagerScroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private ViewPagerScroller mViewPagerScroller;

    /**
     * ＊由于ViewPager 默认的切换速度有点快，因此用一个Scroller 来控制切换的速度
     * <p>而实际上ViewPager 切换本来就是用的Scroller来做的，因此我们可以通过反射来</p>
     * <p>获取取到ViewPager 的 mScroller 属性，然后替换成我们自己的Scroller</p>
     */
    public static class ViewPagerScroller extends Scroller {
        private int mDuration = 800;// ViewPager默认的最大Duration 为600,我们默认稍微大一点。值越大越慢。
        private boolean mIsUseDefaultDuration = false;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mIsUseDefaultDuration ? duration : mDuration);
        }

        public void setUseDefaultDuration(boolean useDefaultDuration) {
            mIsUseDefaultDuration = useDefaultDuration;
        }

        public boolean isUseDefaultDuration() {
            return mIsUseDefaultDuration;
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }


        public int getScrollDuration() {
            return mDuration;
        }
    }


    private static class CustomSliderAdapter<T> extends PagerAdapter {

        private HolderCreator mHolderCreator;

        private List<T> mDataList = new ArrayList<>();

        public CustomSliderAdapter(List<T> mDataList, HolderCreator mHolderCreator) {
            this.mHolderCreator = mHolderCreator;
            this.mDataList = mDataList;
        }

        public void setDatas(List<T> dataList) {
            this.mDataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getView(container, position);
            container.addView(view);
            return view;
        }

        private View getView(ViewGroup container, int position) {
            CustomHolder holder = mHolderCreator.createHolder();
            if (holder == null) {
                throw new RuntimeException("can not return a null holder");
            }

            View view = holder.create(container.getContext());

            if (mDataList != null && mDataList.size() > 0) {
                holder.bindData(container.getContext(), position, mDataList.get(position));
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
