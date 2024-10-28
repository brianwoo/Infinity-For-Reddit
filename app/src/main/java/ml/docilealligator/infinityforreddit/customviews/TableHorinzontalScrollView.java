package ml.docilealligator.infinityforreddit.customviews;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;

import androidx.viewpager2.widget.ViewPager2;

import ml.docilealligator.infinityforreddit.customviews.slidr.widget.SliderPanel;

public class TableHorinzontalScrollView extends HorizontalScrollView {
    private CustomToroContainer toroContainer;
    private ViewPager2 viewPager2;
    private SliderPanel sliderPanel;

    private float lastX = 0.0f;
    private float lastY = 0.0f;

    public TableHorinzontalScrollView(Context context) {
        super(context);
        init();
    }

    public TableHorinzontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableHorinzontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TableHorinzontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        new Handler(Looper.getMainLooper()).post(() -> {
            ViewParent parent = getParent();
            while (parent != null) {
                if (parent instanceof CustomToroContainer) {
                    toroContainer = (CustomToroContainer) parent;
                } else if (parent instanceof ViewPager2) {
                    viewPager2 = (ViewPager2) parent;
                } else if (parent instanceof SliderPanel) {
                    sliderPanel = (SliderPanel) parent;
                }

                parent = parent.getParent();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean allowScroll = true;

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();

                if (toroContainer != null) {
                    toroContainer.requestDisallowInterceptTouchEvent(true);
                }
                if (viewPager2 != null) {
                    viewPager2.setUserInputEnabled(false);
                }
                if (sliderPanel != null) {
                    sliderPanel.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (toroContainer != null) {
                    toroContainer.requestDisallowInterceptTouchEvent(false);
                }
                if (viewPager2 != null) {
                    viewPager2.setUserInputEnabled(true);
                }
                if (sliderPanel != null) {
                    sliderPanel.requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = ev.getX();
                float currentY = ev.getY();
                float dx = Math.abs(currentX - lastX);
                float dy = Math.abs(currentY - lastY);

                allowScroll = dy < dx;

                if (toroContainer != null) {
                    toroContainer.requestDisallowInterceptTouchEvent(allowScroll);
                }
                if (viewPager2 != null) {
                    viewPager2.setUserInputEnabled(false);
                }
                if (sliderPanel != null) {
                    sliderPanel.requestDisallowInterceptTouchEvent(true);
                }
                break;
        }

        return allowScroll && super.onInterceptTouchEvent(ev);
    }
}
