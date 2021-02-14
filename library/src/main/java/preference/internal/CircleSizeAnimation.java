package preference.internal;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.wear.widget.RoundedDrawable;

/**
 * An animation for android.support.wearable.view.CircledImageView
 * that animates its circle radius to a target value.
 */
class CircleSizeAnimation extends Animation {

    @NonNull private final RoundedDrawable circledView;
    private final float targetRadius;
    private final float startRadius;

    public CircleSizeAnimation(@NonNull RoundedDrawable circledView,
                               float targetRadius) {

        this.circledView = circledView;
        this.targetRadius = targetRadius;
        startRadius = targetRadius;
    }

    @Override protected void applyTransformation(float interpolatedTime, @NonNull Transformation t) {
        float radius = interpolatedTime*targetRadius
                + (1-interpolatedTime)*startRadius;
        circledView.setRadius((int)radius); //todo: was setCircleRadius
    }

    /**
     * Begins animating the circle radius toward the target value
     * If this animation is already in progress, this does nothing.
     */
    /*public void animate() {
       if (circledView.getAnimation() != this) {
            circledView.clearAnimation();
            startRadius = circledView.getCircleRadius();
            circledView.startAnimation(this);
        }
    }*/

}
