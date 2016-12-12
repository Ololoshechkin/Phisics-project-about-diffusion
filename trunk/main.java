import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.stage.Screen;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import javax.swing.*;
import javax.swing.text.html.HTMLWriter;

import static java.awt.Color.*;

public class main {

    public static JTextField borderTextField = new JTextField(47);
    public static JTextField DTextField = new JTextField(40);

    public static void main(String[] args) throws InterruptedException {

        JFrame menu = new JFrame("menu");
        menu.setSize(800, 500);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setLayout(new FlowLayout());
        borderTextField.setBackground(Color.white);
        DTextField.setBackground(Color.white);
        borderTextField.setText("введите пропорцию [0;1]");
        DTextField.setText("введите коэффицент диффузии");
        JButton button = new JButton("start");
        ActionListener actionListener = new MyActionListener();
        button.addActionListener(actionListener);
        menu.add(borderTextField);
        menu.add(DTextField);
        menu.add(button);
        menu.setVisible(true);
    }

    public static class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double alpha = Double.parseDouble(borderTextField.getText());
            double D = Double.parseDouble(DTextField.getText());
            WindowWithDiffusions wnd = null;
            try {
                wnd = new WindowWithDiffusions(D, alpha);
            } catch (InterruptedException e1) {
                //e1.printStackTrace();
            }
            wnd.start();
        }
    }

}
