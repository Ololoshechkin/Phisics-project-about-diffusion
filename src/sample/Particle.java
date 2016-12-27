package sample;

/**
 * Created by Vadim on 14.12.16.
 */
public class Particle {
    private double X, Y;
    private int Type;
    private MathStatistics statistics = new MathStatistics();

    Particle() {
        X = Y = 0.0;
        Type = -1;
    }

    Particle(double x, double y, int type) {
        X = x;
        Y = y;
        Type = type;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getX() {return X;}

    public double getY() {return Y;}

    public void setType(int type) {Type = type;}

    public int getType() {return Type;}

    public void moveX(double mathExpectation, double dispersion, double minX, double maxX) {
        double dX = statistics.getRandWithMathExpectation(mathExpectation, dispersion, maxX);
        X += dX;
        X = Math.min(maxX, X);
        X = Math.max(minX, X);
    }

    public void moveY(double mathExpectation, double dispersion, double minY, double maxY) {
        double dY = statistics.getRandWithMathExpectation(mathExpectation, dispersion, maxY);
        Y += dY;
        Y = Math.min(maxY, Y);
        Y = Math.max(minY, Y);
    }

    public void genY(double minY, double maxY) {
        Y = statistics.nextRandDouble(minY, maxY);
    }

    public void genX(double minX, double maxX) {
        X = statistics.nextRandDouble(minX, maxX);
    }
}
