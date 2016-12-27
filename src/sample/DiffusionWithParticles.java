package sample;

import java.awt.*;

/**
 * Created by Vadim on 14.12.16.
 */
public class DiffusionWithParticles extends Diffusion {
    private int particleCount = 5000;
    private double dispersion = 20.0;
    private Particle[] particles;
    private double startTime = 0.0;
    private int height = 500;

    DiffusionWithParticles() throws InterruptedException {
    }

    public void setParticleCount(int cnt) {particleCount = cnt;}

    public void setHeight(int h) {height = h;}

    @Override
    public void start() {
        super.start();
        particles = new Particle[particleCount];
        for (int i = 0; i < particleCount; ++i) {
            particles[i] = new Particle();
            particles[i].genY(0.0, (double) height);
            particles[i].genX(0.0, (double) super.Width);
            if (particles[i].getX() < super.X0) {
                particles[i].setType(0);
            } else  {
                particles[i].setType(1);
            }
        }
        startTime = System.currentTimeMillis();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        height = width / 2;
    }

    @Override
    public void update() {
        super.update();
        double displacementX = (double) (System.currentTimeMillis() - startTime);
        for (int i = 0; i < super.Width; ++i) {
            particles[i].moveX((particles[i].getType() == 0 ? displacementX : -displacementX), dispersion, 0.0, 1.0 * super.Width);
            particles[i].moveY(0.0, dispersion, 0.0, 1.0 * height);
        }
    }

    @Override
    public double getNFirst(int x) {
        double dx = 0.5;
        double nFirstX = 0.0;
        double allCntInX = 0.0;
        for (int i = 0; i < particleCount; ++i) {
            if (x - dx <= particles[i].getX() && particles[i].getX() <= x + dx) {
                if (particles[i].getType() == 0) nFirstX += 1.0;
                allCntInX += 1.0;
            }
        }
        if (allCntInX == .0) return 0.0;
        nFirstX /= allCntInX;
        return nFirstX;
    }

    public double getNSecond(int x) {
        double dx = 0.5;
        double nSecondX = 0.0;
        double allCntInX = 0.0;
        for (int i = 0; i < particleCount; ++i) {
            if (x - dx <= particles[i].getX() && particles[i].getX() <= x + dx) {
                if (particles[i].getType() == 1) nSecondX += 1.0;
                allCntInX += 1.0;
            }
        }
        if (allCntInX == .0) return 0.0;
        nSecondX /= allCntInX;
        return nSecondX;
    }

    public Color getColor(int x) {
        return super.getColorSuperposition(getNFirst(x), getNSecond(x));
    }

}
