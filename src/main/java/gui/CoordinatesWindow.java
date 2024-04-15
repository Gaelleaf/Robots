package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.*;

public class CoordinatesWindow extends JInternalFrame {
    private final JLabel label;
    private final Timer timer;

    public CoordinatesWindow() {
        super("Robot Coordinates", true, true, true, true);
        setSize(300, 300);
        setLocation(10, 10);

        label = new JLabel("Robot coordinates: (0, 0)", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        setContentPane(panel);

        timer = new Timer(1, e -> {
            double x = GameVisualizer.getRobotX();
            double y = GameVisualizer.getRobotY();
            label.setText("Robot coordinates: (" + x + ", " + y + ")");
        });
        timer.start();

        setVisible(true);
    }

    @Override
    public void dispose() {
        timer.stop();
        super.dispose();
    }
}