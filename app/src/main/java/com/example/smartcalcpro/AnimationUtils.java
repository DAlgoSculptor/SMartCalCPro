package com.example.smartcalcpro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtils {
    public enum AnimationStyle {
        DEFAULT,
        BOUNCE,
        FADE,
        SLIDE
    }

    public static void animateView(View view, AnimationStyle style, float speed) {
        if (view == null) return;

        switch (style) {
            case DEFAULT:
                animateDefault(view, speed);
                break;
            case BOUNCE:
                animateBounce(view, speed);
                break;
            case FADE:
                animateFade(view, speed);
                break;
            case SLIDE:
                animateSlide(view, speed);
                break;
        }
    }

    private static void animateDefault(View view, float speed) {
        view.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration((long) (100 / speed))
            .withEndAction(() -> view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration((long) (100 / speed))
                .start())
            .start();
    }

    private static void animateBounce(View view, float speed) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce);
        animation.setDuration((long) (1000 / speed));
        animation.setInterpolator(new BounceInterpolator());
        view.startAnimation(animation);
    }

    private static void animateFade(View view, float speed) {
        view.animate()
            .alpha(0f)
            .setDuration((long) (300 / speed))
            .withEndAction(() -> view.animate()
                .alpha(1f)
                .setDuration((long) (300 / speed))
                .start())
            .start();
    }

    private static void animateSlide(View view, float speed) {
        TranslateAnimation animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, -0.1f,
            Animation.RELATIVE_TO_SELF, 0.1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        );
        animation.setDuration((long) (200 / speed));
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }

    public static void animateButtonPress(View view, float speed) {
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration((long) (100 / speed))
            .withEndAction(() -> view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration((long) (100 / speed))
                .start())
            .start();
    }

    public static void animateCardElevation(View view, float speed) {
        if (view == null) return;

        ValueAnimator elevationAnimator = ValueAnimator.ofFloat(4f, 8f, 4f);
        elevationAnimator.setDuration((long) (300 / speed));
        elevationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        elevationAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            view.setElevation(value);
        });
        
        elevationAnimator.start();
    }
} 