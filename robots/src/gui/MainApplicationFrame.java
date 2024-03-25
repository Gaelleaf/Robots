package gui;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
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
    protected static final String PREF_NODE = "MyAppPrefs";
    protected static final String PREF_KEY_WINDOW_COUNT = "windowCount";
    protected static final String PREF_KEY_WINDOW_X = "windowX_";
    protected static final String PREF_KEY_WINDOW_Y = "windowY_";
    protected static final String PREF_KEY_WINDOW_WIDTH = "windowWidth_";
    protected static final String PREF_KEY_WINDOW_HEIGHT = "windowHeight_";
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        // int inset = 50;        
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // setBounds(inset, inset,
        //     screenSize.width  - inset*2,
        //     screenSize.height - inset*2);

        // setContentPane(desktopPane);
        
        
        // LogWindow logWindow = createLogWindow();
        // addWindow(logWindow, 300, 800);

        // GameWindow gameWindow = new GameWindow(bundle.getString("gameWindow.title"));
        // addWindow(gameWindow, 400, 400);

        // setJMenuBar(new MenuGenerator(this));
        restoreState();
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
    protected void saveState() {
        Preferences prefs = Preferences.userRoot().node(PREF_NODE);
        prefs.putInt(PREF_KEY_WINDOW_COUNT, desktopPane.getAllFrames().length);
        for (int i = 0; i < desktopPane.getAllFrames().length; i++) {
            JInternalFrame frame = desktopPane.getAllFrames()[i];
            prefs.putInt(PREF_KEY_WINDOW_X + i, frame.getX());
            prefs.putInt(PREF_KEY_WINDOW_Y + i, frame.getY());
            prefs.putInt(PREF_KEY_WINDOW_WIDTH + i, frame.getWidth());
            prefs.putInt(PREF_KEY_WINDOW_HEIGHT + i, frame.getHeight());
        }
    }

    private void restoreState() {
        Preferences prefs = Preferences.userRoot().node(PREF_NODE);
        int windowCount = prefs.getInt(PREF_KEY_WINDOW_COUNT, 0);
        for (int i = 0; i < windowCount; i++) {
            int x = prefs.getInt(PREF_KEY_WINDOW_X + i, 0);
            int y = prefs.getInt(PREF_KEY_WINDOW_Y + i, 0);
            int width = prefs.getInt(PREF_KEY_WINDOW_WIDTH + i, 0);
            int height = prefs.getInt(PREF_KEY_WINDOW_HEIGHT + i, 0);
            JInternalFrame frame = new GameWindow(bundle.getString("gameWindow.title"));
            frame.setBounds(x, y, width, height);
            addWindow(frame, width, height);
        }
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
