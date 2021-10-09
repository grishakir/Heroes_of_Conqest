package com.thekingames.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

public abstract class Utils {
    public static float toPixels(float dp, Context context) {
        return dp * context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static float toDp(float pixels, Context context) {
        return pixels / context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static Animator translationYAnimator(final float start, int end, final View view, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, end);
        animator.setDuration(duration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        return animator;
    }
}
