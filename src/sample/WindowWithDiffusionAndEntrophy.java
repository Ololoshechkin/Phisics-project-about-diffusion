package sample;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Vadim on 27.12.16.
 */
public class WindowWithDiffusionAndEntrophy extends Thread {
    //свыфв
    private DiffusionDifferencial diffusion;
    private double D;
    private double borderAlpha;
    private MyJFrame window;
    private int Width, Height;
    private Color colorLeft = Color.RED, colorRight = Color.BLUE;
    private double correctionSpeed = 1.0;
    private double startTime = 0;
    private double curTime = 0;
    private ArrayList<Double> N1 = new ArrayList<Double>(), N2 = new ArrayList<Double>();

    WindowWithDiffusionAndEntrophy(double d, double borderalpha) throws InterruptedException {
        D = d;
        if (Math.abs(DiffusionDifferencial.getAlphaBy(D)) > 0.5) {
            //x(t) = sqrt(2Dt)
            // D' -> D / a => x'(t) -> x(t) / sqrt(a)
            // cD' = 0.5 => D' = 0.5 / c
            // correctionSpeed = 1/sqrt(0.5 / c   /    last / c) = sqrt(last / 0.5)
            correctionSpeed = Math.sqrt(Math.abs(DiffusionDifferencial.getAlphaBy(D)) / 0.5);
            D /= correctionSpeed * correctionSpeed;
        }
        borderAlpha = borderalpha;
        Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        window = new MyJFrame(screenSize.width, screenSize.height);
        Width = (screenSize.width - 250) / 2;
        Height = (screenSize.height - 250) / 2;

        window.setBoxPlace(0, 50 , 50, Width, Height);
        window.setGraphPlace(0, 50, 100 + Height, Width, Height);
        for (int i = 1; i <= 2; ++i) {
            window.setGraphPlace(i, 100 + Width, 50 + (50 + Height) * (i - 1), Width, Height);
        }
        diffusion = new DiffusionDifferencial();
        diffusion.setWidth(Width);
        diffusion.setBorder(borderAlpha);
        diffusion.setD(D);
        diffusion.start();

        window.startGui();
        window.wannaPaused();
        //speedWindow = new SpeedJFrame((int) screenSize.getWidth() - 200, (int) screenSize.getHeight() - 60,200, 60);
    }

    public void setBorderAlpha(double alpha) {
        borderAlpha = alpha;
    }

    public void setD(double d) {
        D = d;
    }

    private Color getColorSuperposition(double nFirst, double nSecond) {
        double nSum = nFirst + nSecond;
        nFirst /= nSum;
        nSecond /= nSum;
        return new Color((int)(nFirst * colorLeft.getRed() + nSecond * colorRight.getRed()),
                (int)(nFirst * colorLeft.getGreen() + nSecond * colorRight.getGreen()),
                (int)(nFirst * colorLeft.getBlue() + nSecond * colorRight.getBlue()));
    }

    private void updateEntrophy(int iterations) {
        double n1 = 0.0, n2 = 0.0;
        double s1 = 0.0, s2 = 0.0;
        for (int x = 0; x < Width; ++x) {
            n1 += diffusion.getNFirst(x) * x;
            n2 += diffusion.getNSecond(x) * x;
            s1 += diffusion.getNFirst(x);
            s2 += diffusion.getNSecond(x);
        }
        n1 /= s1;
        n2 /= s2;
        n1 /= Width;
        n2 /= Width;
        if (N1.size() < 3 || Math.abs(N1.get(N1.size() - 1) - n1) > 0.01) N1.add(n1);
        if (N2.size() < 3 || Math.abs(N2.get(N2.size() - 1) - n1) > 0.01) N2.add(n2);
    }

    private void updateModels(int iteration, boolean delayedPause) {
        for (int x = 0; x < Width; ++x) {
            window.drawLine(0, x, diffusion.getColor(x));
        }
        if (iteration % 2 == 0 || delayedPause) {
            window.clearGraph(0);
            window.setStartPlotPoint(0, 0, diffusion.getNFirst(0), colorLeft);
            for (int x = 0; x < Width; x += 2) {
                window.drawPlotPoint(0, x, diffusion.getNFirst(x));
            }
            window.drawPlotPoint(0, Width - 1, diffusion.getNFirst(Width - 1));

            window.setStartPlotPoint(0, Width - 1, diffusion.getNSecond(Width - 1), colorRight);
            for (int x = Width - 1; x >= 0; x -= 2) {
                window.drawPlotPoint(0, x, diffusion.getNSecond(x));
            }
            window.drawPlotPoint(0, 0, diffusion.getNSecond(0));

            double n1 = 0.0, n2 = 0.0;
            double s1 = 0.0, s2 = 0.0;
            for (int x = 0; x < Width; ++x) {
                n1 += diffusion.getNFirst(x) * x;
                n2 += diffusion.getNSecond(x) * x;
                s1 += diffusion.getNFirst(x);
                s2 += diffusion.getNSecond(x);
            }
            n1 /= s1;
            n2 /= s2;
            n1 /= Width;
            n2 /= Width;
            N1.add(n1);
            N2.add(n2);
            if (iteration % 5 == 0 || delayedPause) {
                window.clearGraph(1);
                window.setStartPlotPoint(1, 0, N1.get(0), colorLeft);
                for (int x = 0; x < N1.size(); ++x) {
                    int X = x * Width / N1.size();
                    window.drawPlotPoint(1, X, N1.get(x));
                }
                window.drawPlotPoint(1, Width - 1, N1.get(N1.size() - 1));
                window.setStartPlotPoint(1, 0, N2.get(0), colorRight);
                for (int x = 0; x < N2.size(); ++x) {
                    int X = x * Width / N2.size();
                    window.drawPlotPoint(1, X, N2.get(x));
                }
                window.drawPlotPoint(1, Width - 1, N2.get(N2.size() - 1));
            }
        }

        diffusion.multipleUpdate(5000, window.getSpeed());
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int iteration = 0;
        diffusion.setColorFirst(colorLeft);
        diffusion.setColorSecond(colorRight);
        updateModels(-1, true);
        boolean delayedPause = false;
        window.dontWannaPaused();
        window.wannaOneMore();
        int x = 0;
        while (true) {
            if (!delayedPause && window.isWannaOneMore()) {
                window.dontWannaOneMore();
                window.dontWannaPaused();
                delayedPause = true;
                try {
                    diffusion = new DiffusionDifferencial();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                diffusion.setWidth(Width);
                diffusion.setBorder(borderAlpha);
                diffusion.setD(D);
                diffusion.setColorFirst(colorLeft);
                diffusion.setColorSecond(colorRight);
                diffusion.start();
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!delayedPause && window.isPaused()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            updateModels(iteration, delayedPause);
            if (delayedPause) {
                window.wannaPaused();
                window.dontWannaOneMore();
                delayedPause = false;
            }
            ++iteration;
        }
    }
    public void setColorFirst(Color colorFirst) {
        colorLeft = colorFirst;
    }

    public void setColorFirstByRGB(int R, int G, int B) {colorLeft = new Color(R, G, B);}

    public void setColorSecond(Color colorSecond) {
        colorRight = colorSecond;
    }

    public void setColorSecondByRGB(int R, int G, int B) {colorRight = new Color(R, G, B);}
}
