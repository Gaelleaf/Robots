package gui;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import log.Logger;

public class MenuGenerator extends JMenuBar{
    private final MainApplicationFrame applicationFrame;
    
    public MenuGenerator(MainApplicationFrame applicationFrame){
        this.applicationFrame = applicationFrame;
        add(generateLookAndFeelMenu());
        add(generateTestMenu());
        add(generateExitMenu());
    }
    private JMenu generateLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        lookAndFeelMenu.add(createMenuItem("Универсальная схема", KeyEvent.VK_S, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK),
                (event) -> {
                    applicationFrame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
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
                    WindowEvent eventExit  = new WindowEvent(applicationFrame, WindowEvent.WINDOW_CLOSING);
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