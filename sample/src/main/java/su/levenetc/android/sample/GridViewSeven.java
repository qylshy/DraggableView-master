package su.levenetc.android.sample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by qiuyunlong on 16/4/20.
 */
public class GridViewSeven extends ViewGroup {
    private static final String TAG = GridViewSeven.class.getSimpleName();
    private int itemMeasureWidth = 0;
    private int spacing = 0;
    private int firstItemHeight = 0;

    public GridViewSeven(Context context) {
        this(context, null, 0);
    }

    public GridViewSeven(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GridViewSeven(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        if (attributeSet != null) {
            TypedArray styledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.GridView);
            spacing = styledAttributes.getDimensionPixelSize(R.styleable.GridView_spacing, 0);
            styledAttributes.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GridViewSeven(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, 0);
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int totalWidth = getMeasuredWidth();

        if (itemMeasureWidth == 0) {
            itemMeasureWidth = (totalWidth - 2 * spacing) / 3;
        }

        if (firstItemHeight == 0) {
            firstItemHeight = (totalWidth * 316) / 640;
        }

        int secondRowTop = firstItemHeight + spacing;
        int thirdRowTop = secondRowTop + itemMeasureWidth + spacing;
        int secondLineLeft = itemMeasureWidth + spacing;
        int thirdLineLeft = secondLineLeft * 2;

        for (int i = 0, ll = getChildCount(); i < ll; i++) {
            final View view = getChildAt(i);

            switch (i) {
                case 0: {
                    view.layout(0, 0, totalWidth, firstItemHeight);

                    break;
                }

                case 1: {
                    view.layout(0, secondRowTop, itemMeasureWidth, secondRowTop + itemMeasureWidth);

                    break;
                }

                case 2: {
                    view.layout(secondLineLeft, secondRowTop, secondLineLeft + itemMeasureWidth, secondRowTop + itemMeasureWidth);

                    break;
                }

                case 3: {
                    view.layout(thirdLineLeft, secondRowTop, thirdLineLeft + itemMeasureWidth, secondRowTop + itemMeasureWidth);

                    break;
                }

                case 4: {
                    view.layout(0, thirdRowTop, itemMeasureWidth, thirdRowTop + itemMeasureWidth);
                    break;
                }

                case 5: {
                    view.layout(secondLineLeft, thirdRowTop, secondLineLeft + itemMeasureWidth, thirdRowTop + itemMeasureWidth);
                    break;
                }

                case 6: {
                    view.layout(thirdLineLeft, thirdRowTop, thirdLineLeft + itemMeasureWidth, thirdRowTop + itemMeasureWidth);
                    break;
                }

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (itemMeasureWidth == 0) {
            itemMeasureWidth = (totalWidth - 2 * spacing) / 3;
        }

        if (firstItemHeight == 0) {
            firstItemHeight = (totalWidth * 316) / 640;
        }

        int totalHeight = firstItemHeight + itemMeasureWidth * 2 + 2 * spacing;

        for (int i = 0, l = getChildCount(); i < l; i++) {
            final View view = getChildAt(i);

            if (i == 0) {
                view.measure(totalWidth, firstItemHeight);
                break;
            }
            view.measure(itemMeasureWidth, itemMeasureWidth);
        }

        setMeasuredDimension(totalWidth, totalHeight);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

    }
}
