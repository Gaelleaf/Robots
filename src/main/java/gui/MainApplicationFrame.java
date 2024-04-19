package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import log.Logger;


public class MainApplicationFrame extends JFrame
{
    private final ResourceBundle bundle = ResourceBundle.getBundle("resources", Locale.getDefault());
    private final GameLogic logic = new GameLogic();
    private final JDesktopPane desktopPane = new JDesktopPane();
    protected static final String PREF_NODE = "MyAppPrefs";
    protected static final String PREF_KEY_WINDOW_COUNT = "windowCount";
    protected static final String PREF_KEY_WINDOW_X = "windowX_";
    protected static final String PREF_KEY_WINDOW_Y = "windowY_";
    protected static final String PREF_KEY_WINDOW_WIDTH = "windowWidth_";
    protected static final String PREF_KEY_WINDOW_HEIGHT = "windowHeight_";
    protected static final String PREF_KEY_WINDOW_MAXIMIZED = "windowMaximized_";
    protected static final String PREF_KEY_WINDOW_ICONIFIED = "windowIconified_";
    
    
    public MainApplicationFrame() throws PropertyVetoException {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        CoordinatesWindow coordinatesWindow = new CoordinatesWindow(logic);
        addWindow(coordinatesWindow, 300, 100);
        coordinatesWindow.setVisible(true);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow, 300, 800);

        GameWindow gameWindow = new GameWindow(bundle.getString("gameWindow.title"), logic);
        addWindow(gameWindow, 300, 300);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateLookAndFeelMenu());
        menuBar.add(generateTestMenu());
        menuBar.add(generateExitMenu());
        setJMenuBar(menuBar);
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
            prefs.putBoolean(PREF_KEY_WINDOW_MAXIMIZED + i, frame.isMaximum());
            prefs.putBoolean(PREF_KEY_WINDOW_ICONIFIED + i, frame.isIcon());
        }
    }

    private void restoreState() throws PropertyVetoException {
        Preferences prefs = Preferences.userRoot().node(PREF_NODE);
        int windowCount = prefs.getInt(PREF_KEY_WINDOW_COUNT, 0);
        for (int i = 0; i < windowCount; i++) {
            int x = prefs.getInt(PREF_KEY_WINDOW_X + i, 0);
            int y = prefs.getInt(PREF_KEY_WINDOW_Y + i, 0);
            int width = prefs.getInt(PREF_KEY_WINDOW_WIDTH + i, 0);
            int height = prefs.getInt(PREF_KEY_WINDOW_HEIGHT + i, 0);
            JInternalFrame frame = desktopPane.getAllFrames()[i];
            frame.setBounds(x, y, width, height);
            frame.setMaximum(prefs.getBoolean(PREF_KEY_WINDOW_MAXIMIZED + i, false));
            frame.setIcon(prefs.getBoolean(PREF_KEY_WINDOW_ICONIFIED + i, false));
        }
    }
    protected void addWindow(JInternalFrame frame, int width,  int height) 
    {
        desktopPane.add(frame);
        frame.setVisible(true);
        frame.setSize(width, height);
        frame.setName(String.valueOf(desktopPane.getAllFrames().length));
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
    private JMenu generateLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        lookAndFeelMenu.add(createMenuItem("Универсальная схема", KeyEvent.VK_S, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK),
                (event) -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    invalidate();
                }));
        return lookAndFeelMenu;
    }

    private JMenu generateTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        testMenu.add(createMenuItem("Сообщение в лог", KeyEvent.VK_S, null, (event) -> {
            Logger.debug("Новая строка");
        }));
        return testMenu;
    }

    private JMenu generateExitMenu() {
        JMenu exitMenu = new JMenu("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_X);
        JMenuItem exitItem = new JMenuItem("Закрыть программу", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.SHIFT_DOWN_MASK));

        exitItem.addActionListener(event -> {
                int option = JOptionPane.showConfirmDialog(null, "Подтверждение выхода", "Выход", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            // Show a confirmation dialog to the user
                            int option = JOptionPane.showConfirmDialog(null,
                                    "Хотите сохранить состояние окон перед выходом?",
                                    "Выход",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null);
                            if (option == JOptionPane.NO_OPTION) {
                                dispose();
                            } else {
                                saveState();
                                dispose();
                            }
                        }
                    });
                    WindowEvent eventExit  = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(eventExit);
                }
        });
        exitMenu.add(exitItem);
        return exitMenu;
    }
    private JMenuItem createMenuItem(String text, int mnemonic, KeyStroke accelerator, ActionListener action) {
        JMenuItem menuItem = new JMenuItem(text, mnemonic);
        if (accelerator != null) {
            menuItem.setAccelerator(accelerator);
        }
        menuItem.addActionListener(action);
        return menuItem;
    }
}
