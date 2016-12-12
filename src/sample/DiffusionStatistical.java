package sample;
import java.awt.*;

/**
 * Created by Vadim on 06.12.16.
 */
public class DiffusionStatistical extends Diffusion {
    private MathStatistics statistics = new MathStatistics();
    private double startTime = 0.0, lastTime = 0.0;
    private Dimension[] particlesFirst, particlesSecond;
    private void createStartParticles() {
        particlesFirst = new Dimension[10 * (int) (super.X0 * (super.Width / 2))];
        particlesSecond = new Dimension[10 * (int) ((super.Width - super.X0) * (super.Width / 2))];
        for (int w = 0; w < super.X0; ++w) {
            for (int h = 0; h < super.Width / 2; ++h) {
                //System.out.println("w = " + w + " ; h = " + h);
                particlesFirst[w * (super.Width / 2) + h] = new Dimension(w, h);
            }
        }
        for (int w = 0; w < super.Width - super.X0; ++w) {
            for (int h = 0; h < super.Width / 2; ++h) {
                particlesSecond[w * (super.Width / 2) + h] = new Dimension(w, h);
            }
        }
    }
    public DiffusionStatistical() throws InterruptedException {
        super();
        createStartParticles();
    }

    @Override
    public void start() {
        super.start();
        startTime = System.currentTimeMillis();
        lastTime = startTime;
        createStartParticles();
    }

    @Override
    public void update() {
        super.update();
        double t = 0.001 * (System.currentTimeMillis() - lastTime);
        double squaFirsteltaX = 2.0 * super.D * t;
        for (int i = 0; i < particlesFirst.length; ++i) {
            Dimension displacement = statistics.getDisplacement(squaFirsteltaX, super.Width, super.Width / 2);
            particlesFirst[i].setSize(particlesFirst[i].getWidth() + displacement.getWidth(), particlesFirst[i].getHeight() + displacement.getHeight());
        }
        for (int i = 0; i < particlesFirst.length; ++i) {
            Dimension displacement = statistics.getDisplacement(squaFirsteltaX, super.Width, super.Width / 2);
            particlesSecond[i].setSize(particlesSecond[i].getWidth() - displacement.getWidth(), particlesSecond[i].getHeight() + displacement.getHeight());
        }
        lastTime = System.currentTimeMillis();
    }


    @Override
    public Color getColor(int x) {
        int cntFirst = 0, cntSecond = 0;
        for (int i = 0; i < particlesFirst.length; ++i) {
            if (particlesFirst[i].getWidth() == x) ++cntFirst;
        }
        for (int i = 0; i < particlesSecond.length; ++i) {
            if (particlesSecond[i].getWidth() == x) ++cntSecond;
        }
        return super.getColorSuperposition(cntFirst / (super.Width / 2), cntSecond / (super.Width / 2));
    }

    @Override
    public double getNFirst(int x) {
        int cntFirst = 0;
        for (int i = 0; i < particlesFirst.length; ++i) {
            if (particlesFirst[i].getWidth() == x) ++cntFirst;
        }
        return (double)cntFirst / ((double)super.Width / 2);
    }

    @Override
    public double getNSecond(int x) {
        int cntSecond = 0;
        for (int i = 0; i < particlesSecond.length; ++i) {
            if (particlesSecond[i].getWidth() == x) ++cntSecond;
        }
        return (double)cntSecond / ((double)super.Width / 2);
    }

    public double getTimeSeconds() {
        return 0.001 * (System.currentTimeMillis() - startTime);
    }
}
