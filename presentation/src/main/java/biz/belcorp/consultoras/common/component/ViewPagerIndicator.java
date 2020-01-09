package biz.belcorp.consultoras.common.component;

/**
 * Created by andres.escobar on 21/08/2017.
 * Copyright ©2010 All rights reserved.
 **/

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;

public class ViewPagerIndicator extends LinearLayoutCompat {
    private static final float SCALE = 1.6F;
    private static final int NO_SCALE = 1;
    private static final int DEF_VALUE = 10;
    private int mPageCount;
    private int mSelectedIndex;
    private int mItemSize;
    private int mDelimiterSize;
    private int mImageResource;
    @NonNull
    private final List<ImageView> mIndexImages;
    @Nullable
    private android.support.v4.view.ViewPager.OnPageChangeListener mListener;

    public ViewPagerIndicator(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public ViewPagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mItemSize = 10;
        this.mDelimiterSize = 10;
        this.mIndexImages = new ArrayList();
        this.setOrientation(0);
        this.mImageResource = R.drawable.ic_circle_indicator;

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, 0, 0);

        try {
            this.mItemSize = attributes.getDimensionPixelSize(R.styleable.ViewPagerIndicator_itemSize, 10);
            this.mDelimiterSize = attributes.getDimensionPixelSize(R.styleable.ViewPagerIndicator_delimiterSize, 10);
            this.mImageResource = attributes.getResourceId(R.styleable.ViewPagerIndicator_imageResource, R.drawable.ic_circle_indicator);

        } finally {
            attributes.recycle();
        }

        if (this.isInEditMode()) {
            this.createEditModeLayout();
        }

    }

    private void createEditModeLayout() {
        for (int i = 0; i < 5; ++i) {
            FrameLayout boxedItem = this.createBoxedItem(i);
            this.addView(boxedItem);
            if (i == 1) {
                View item = boxedItem.getChildAt(0);
                ViewGroup.LayoutParams layoutParams = item.getLayoutParams();
                layoutParams.height = (int) ((float) layoutParams.height * 1.6F);
                layoutParams.width = (int) ((float) layoutParams.width * 1.6F);
                item.setLayoutParams(layoutParams);
            }
        }

    }

    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        this.setPageCount(pagerAdapter != null ? pagerAdapter.getCount() : 0);
        viewPager.addOnPageChangeListener(new ViewPagerIndicator.OnPageChangeListener());
    }

    public void addOnPageChangeListener(android.support.v4.view.ViewPager.OnPageChangeListener listener) {
        this.mListener = listener;
    }

    private void setSelectedIndex(int selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex <= this.mPageCount - 1) {
            ImageView unselectedView = this.mIndexImages.get(this.mSelectedIndex);
            unselectedView.animate().scaleX(1.0F).scaleY(1.0F).setDuration(300L).start();
            ImageView selectedView = this.mIndexImages.get(selectedIndex);
            selectedView.animate().scaleX(1.6F).scaleY(1.6F).setDuration(300L).start();
            this.mSelectedIndex = selectedIndex;
        }
    }

    private void setPageCount(int pageCount) {
        this.mPageCount = pageCount;
        this.mSelectedIndex = 0;
        this.removeAllViews();
        this.mIndexImages.clear();

        for (int i = 0; i < pageCount; ++i) {
            this.addView(this.createBoxedItem(i));
        }

        this.setSelectedIndex(this.mSelectedIndex);
    }

    @NonNull
    private FrameLayout createBoxedItem(int position) {
        FrameLayout box = new FrameLayout(this.getContext());
        ImageView item = this.createItem();
        box.addView(item);
        this.mIndexImages.add(item);
        android.support.v7.widget.LinearLayoutCompat.LayoutParams boxParams = new android.support.v7.widget.LinearLayoutCompat.LayoutParams((int) ((float) this.mItemSize * 1.6F), (int) ((float) this.mItemSize * 1.6F));
        if (position > 0) {
            boxParams.setMargins(this.mDelimiterSize, 0, 0, 0);
        }

        box.setLayoutParams(boxParams);
        return box;
    }

    @NonNull
    private ImageView createItem() {
        ImageView index = new ImageView(this.getContext());
        android.widget.FrameLayout.LayoutParams indexParams = new android.widget.FrameLayout.LayoutParams(this.mItemSize, this.mItemSize);
        indexParams.gravity = 17;
        index.setLayoutParams(indexParams);
        index.setImageResource(mImageResource);
        index.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return index;
    }

    private class OnPageChangeListener implements android.support.v4.view.ViewPager.OnPageChangeListener {
        private OnPageChangeListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (ViewPagerIndicator.this.mListener != null) {
                ViewPagerIndicator.this.mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

        }

        public void onPageSelected(int position) {
            ViewPagerIndicator.this.setSelectedIndex(position);
            if (ViewPagerIndicator.this.mListener != null) {
                ViewPagerIndicator.this.mListener.onPageSelected(position);
            }

        }

        public void onPageScrollStateChanged(int state) {
            if (ViewPagerIndicator.this.mListener != null) {
                ViewPagerIndicator.this.mListener.onPageScrollStateChanged(state);
            }

        }
    }
}
