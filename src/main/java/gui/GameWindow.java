package gui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    private final GameLogic logic;
    public GameWindow(String title, GameLogic logic)
    {
        super(title, true, true, true, true);
        this.logic = logic;
        logic.startTimer();
        m_visualizer = new GameVisualizer(logic);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
