package su.levenetc.android.sample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import su.levenetc.android.draggableview.DragController;
import su.levenetc.android.draggableview.DraggableView;
import su.levenetc.android.draggableview.SkewView;
import su.levenetc.android.draggableview.utils.Utils;

/**
 * Created by qiuyunlong on 16/4/20.
 */
public class SevenGridContainer extends GridViewSeven implements DragController.IDragViewGroup {

    private DragController<SevenGridContainer> dragController = new DragController<>(this);
    private View selectedView;
    private static final int MAX_COLUMNS = 3;

    private int from = -1;
    private int to = -1;
    private int tmpPos = -1;
    private int lastTo = -1;

    private float lastFingerX = -1;
    private float lastFingerY = -1;

    private static final int[] imageArray = {
            R.drawable.alium,
            R.drawable.chemichal_brothers_exit_planet_dust,
            R.drawable.chemichal_brothers_further,
            R.drawable.chemichal_brothers_hanna,
            R.drawable.city_sunset,
            R.drawable.die_antwoords_donker_mag,
            R.drawable.die_antwoords_tension
    };

    private static List list = new ArrayList();

    static {
        list.add(R.drawable.alium);
        list.add(R.drawable.chemichal_brothers_exit_planet_dust);
        list.add(R.drawable.chemichal_brothers_further);
        list.add(R.drawable.chemichal_brothers_hanna);
        list.add(R.drawable.city_sunset);
        list.add(R.drawable.die_antwoords_donker_mag);
        list.add(R.drawable.die_antwoords_tension);
    }

    private boolean onDragEnd = false;


    public SevenGridContainer(Context context) {
        super(context);
    }

    public SevenGridContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return dragController.onTouchEvent(event);
    }


    @Override
    public View onDownEvent(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (Utils.isViewContains(child, x, y, false)) {
                selectedView = child;
                from = i;
                return child;
            }
        }
        return null;
    }

    @Override
    public ViewGroup getContainerForDraggableView() {
        Activity context = (Activity) getContext();
        ViewGroup rootView = (ViewGroup) context.getWindow().getDecorView().getRootView();
        return (ViewGroup) ((ViewGroup) rootView.getChildAt(0)).getChildAt(1);
    }

    @Override
    public void onDragStart() {
        AlphaAnimation alphaAnim = new AlphaAnimation(1, 0.5f);
        alphaAnim.setDuration(500);
        alphaAnim.setFillAfter(true);
        startAnimation(alphaAnim);
        selectedView.setVisibility(View.INVISIBLE);
        onDragEnd = false;
    }

    @Override
    public void onDragEnd() {

        swap(from , to);

        onDragEnd = true;
        tmpPos = -1;
        from = -1;
        to = -1;
        lastTo = -1;


        clearAnimation();//

        System.out.println("qqqqqqqq===onDragEnd==from=" + from + ",to=" + to +",tmpPos="+tmpPos);

        DraggableView draggableView = dragController.getDraggableView();

        AnimatorSet translateSet = new AnimatorSet();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0.5f, 1f);
        ObjectAnimator transX = ObjectAnimator.ofFloat(
                draggableView,
                "translationX",
                draggableView.getX(),
                selectedView.getX() - draggableView.getXTranslation()
        );

        ObjectAnimator transY = ObjectAnimator.ofFloat(
                draggableView,
                "translationY",
                draggableView.getY(),
                selectedView.getY() - draggableView.getYTranslation()
        );

        transX.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dragController.finishDrag();
                selectedView.setVisibility(View.VISIBLE);
                // swap(from, to);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        translateSet.playTogether(transX, transY, alpha);
        translateSet.setInterpolator(new FastOutSlowInInterpolator());
        translateSet.setDuration(300);
        //暂时屏蔽结束动画
        //translateSet.start();
        dragController.finishDrag();
        selectedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMoveEvent(float x, float y) {

        /*for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (Utils.isViewContains(child, (int) x, (int) y, false)) {
                if (to != i) {
                    to = i;
                    //selectedView = child;
                    System.out.println("qqqqqqq====" + i + ",x=" + x + ",y=" + y);
                    //return child;
                    swap(from, to);
                }

            }
        }*/
        //return null;

    }

    @Override
    public void onMoveEvent(float x, float y, float fingerX, float fingerY) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (Utils.isViewContains(child, (int) fingerX, (int) fingerY, false)) {
                if (lastTo != i){
                    lastTo = to;
                    to = i;
                    swapFake(from, to);
                }
                return;
            }
        }
        lastTo = to;
        to = -1;
        swapFake(from, from);
    }

    private void swapFake(int from, int to){
        if (to < getChildCount()) {
            if (lastTo != -1){
                ((ImageView) getChildAt(lastTo)).setImageResource((int) list.get(lastTo));
            }
            ((ImageView) getChildAt(from)).setImageResource((int) list.get(to));
            ((ImageView) getChildAt(to)).setImageResource((int) list.get(from));

            if (from == to){
                getChildAt(from).setVisibility(INVISIBLE);
            }else {
                getChildAt(from).setVisibility(VISIBLE);
            }



            /*if (from == to) {
                if (tmpPos != -1){
                   // Collections.swap(list, tmpPos, to);
                    ((ImageView) getChildAt(tmpPos)).setImageResource((int) list.get(tmpPos));
                    ((ImageView) getChildAt(to)).setImageResource((int) list.get(to));
                }
                getChildAt(from).setVisibility(INVISIBLE);
            }else {
                if (tmpPos != -1 && tmpPos!= to){
                    ((ImageView) getChildAt(tmpPos)).setImageResource((int) list.get(tmpPos));
                }
            }*/
           // Collections.swap(list, from, to);
        }
        System.out.println("qqqqFake===swap==from=" + from + ",to=" + to + ",tmp="+ tmpPos +",lastTo="+ lastTo);
    }

    private void swap(int from, int to) {
//        if (to < getChildCount()) {
//            ((ImageView) getChildAt(from)).setImageResource((int) list.get(to));
//            ((ImageView) getChildAt(to)).setImageResource((int) list.get(from));
//            getChildAt(from).setVisibility(VISIBLE);
//            if (from == to) {
//                if (tmpPos != -1){
//                    Collections.swap(list, tmpPos, to);
//                    ((ImageView) getChildAt(tmpPos)).setImageResource((int) list.get(tmpPos));
//                    ((ImageView) getChildAt(to)).setImageResource((int) list.get(to));
//                }
//                getChildAt(from).setVisibility(INVISIBLE);
//            }
//            Collections.swap(list, from, to);
//        }
        if (to == -1){
            return;
        }
        Collections.swap(list, from, to);
        System.out.println("qqqq===swap==from=" + from + ",to=" + to + ",tmp="+ tmpPos);
    }

    @Override
    public DraggableView createDraggableView(
            Bitmap bitmap,
            VelocityTracker velocityTracker,
            PointF selectedViewPoint,
            PointF downEventPoint) {


        return new SkewView(
                getContext(),
                bitmap,
                velocityTracker,
                selectedViewPoint,
                downEventPoint,
                this
        );
    }
}
