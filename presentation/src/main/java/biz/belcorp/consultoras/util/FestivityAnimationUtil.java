package biz.belcorp.consultoras.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import biz.belcorp.consultoras.R;

/**
 *
 */
public final class FestivityAnimationUtil {

    private FestivityAnimationUtil() {
        throw new UnsupportedOperationException();
    }

    public static void getCommonConfetti(int color1, int color2, Resources resources, ViewGroup container) {
        int size = resources.getDimensionPixelSize(R.dimen.default_confetti_size);

        ConfettiSource confettiSource = new ConfettiSource(-size, -size);
        CommonConfetti commonConfetti =
            CommonConfetti.rainingConfetti(container, confettiSource, new int[]{
                color1, color2
            });

        int velocitySlow = resources.getDimensionPixelOffset(R.dimen.default_velocity_slow);
        int velocityNormal = resources.getDimensionPixelOffset(R.dimen.default_velocity_normal);
        int velocityFast = resources.getDimensionPixelOffset(R.dimen.default_velocity_fast);

        commonConfetti.getConfettiManager()
            .setVelocityX(velocityFast, velocityNormal)
            .setAccelerationX(-velocityNormal, velocitySlow)
            .setTargetVelocityX(0, velocitySlow / 2f)
            .setVelocityY(velocityNormal, velocitySlow);

        commonConfetti.stream(3000);
    }

    public static void imageConfetti(int src, Resources resources, ViewGroup container) {

        int size = resources.getDimensionPixelSize(R.dimen.big_confetti_size);
        int velocitySlow = resources.getDimensionPixelOffset(R.dimen.default_velocity_slow);
        int velocityNormal = resources.getDimensionPixelOffset(R.dimen.default_velocity_normal);

        final Bitmap bitmap = Bitmap
            .createScaledBitmap(BitmapFactory.decodeResource(resources, src),
                size, size, false);

        ConfettiSource source = new ConfettiSource(0, -size, container.getWidth(), -size);

        ConfettoGenerator confettoGenerator = random -> new BitmapConfetto(bitmap);

        ConfettiManager confettiManager = new ConfettiManager(container.getContext(), confettoGenerator, source, container)
            .setVelocityX(0, velocitySlow)
            .setVelocityY(velocityNormal, velocitySlow)
            .setRotationalVelocity(180, 90)
            .setTouchEnabled(true);

        confettiManager.setNumInitialCount(0)
            .setEmissionDuration(3000)
            .setEmissionRate(7)
            .animate();
    }

    public static void imageConfetti(Resources resources, ViewGroup container, List<Integer> images) {

        int size = resources.getDimensionPixelSize(R.dimen.medium_confetti_size);
        int velocitySlow = resources.getDimensionPixelOffset(R.dimen.default_velocity_slow);
        int velocityNormal = resources.getDimensionPixelOffset(R.dimen.default_velocity_normal);

        final List<Bitmap> allPossibleConfetti  = new ArrayList<>();
        for(int image : images){
            allPossibleConfetti.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, image), size, size, false));
        }

        if(!allPossibleConfetti.isEmpty()){
            ConfettiSource source = new ConfettiSource(0, -size, container.getWidth(), -size);
            ConfettoGenerator confettoGenerator = random -> {
                final Bitmap bitmap = allPossibleConfetti.get(random.nextInt(allPossibleConfetti.size()));
                return new BitmapConfetto(bitmap);
            };

            ConfettiManager confettiManager = new ConfettiManager(container.getContext(), confettoGenerator, source, container)
                .setVelocityX(0, velocitySlow)
                .setVelocityY(velocityNormal, velocitySlow)
                .setRotationalVelocity(180, 90)
                .setTouchEnabled(true);

            confettiManager.setNumInitialCount(0)
                .setEmissionDuration(3000)
                .setEmissionRate(15)
                .animate();
        }
    }
}
