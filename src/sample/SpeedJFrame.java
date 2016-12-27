package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Vadim on 20.12.16.
 */
public class SpeedJFrame extends JFrame implements Runnable {
    private JSlider slider;
    private KeyListener keyListener;
    private boolean paused = false;
    private boolean oneMore = false;
    public SpeedJFrame(int x0, int y0, int width, int height) {
        setBounds(x0, y0, width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        slider = new JSlider(0, 200, 20);
        slider.setVisible(true);
        add(slider);
        setAlwaysOnTop(true);
        setVisible(true);
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    paused = !paused;
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    oneMore = !oneMore;
                }
            }
        };
    }

    public double getIterations() {
        return 250.0 * slider.getValue();
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isWannaOneMore() {return oneMore;}

    public void dontWannaOneMore() {
        oneMore = false;
    }

    public void dontWannaPaused() {
        paused = false;
    }

    public void wannaPaused() {
        paused = true;
    }

    public void wannaOneMore() {oneMore = true;}

    @Override
    public void run() {

    }
}
