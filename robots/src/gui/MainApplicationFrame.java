package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final ResourceBundle bundle = ResourceBundle.getBundle("resources", Locale.getDefault());
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow, 300, 800);

        GameWindow gameWindow = new GameWindow(bundle.getString("gameWindow.title"));
        addWindow(gameWindow, 400, 400);

        setJMenuBar(new MenuGenerator(this));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(bundle.getString("logWindow.title"), Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("Logger.debug.strMessage.status"));
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame, int width,  int height) 
    {
        desktopPane.add(frame);
        frame.setVisible(true);
        frame.setSize(width, height);
    }
    
    protected void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
