package ru.vsu.cs.task5;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ru.vsu.cs.util.DrawUtils;
import ru.vsu.cs.util.JTableUtils;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JTable tableGameField;
    private JLabel labelStatus;
    private JButton startButton;
    private JLabel resultLabel;
    private JButton cleanButton;
    private JComboBox typeMethods;
    private JCheckBox teleportBox;
    private JCheckBox checkBoxTypeTeleports;

    private static final int DEFAULT_COL_COUNT = 10;
    private static final int DEFAULT_ROW_COUNT = 6;

    private static final int DEFAULT_GAP = 8;
    private static final int DEFAULT_CELL_SIZE = 70;
    private final GameParams params = new GameParams(DEFAULT_ROW_COUNT, DEFAULT_COL_COUNT);
    private final Game game = new Game();

    /* Демонстрация работы с таймером (удалить, если не нужно в вашей игре) */
    private int time = 0;
    private final Timer timer = new Timer(1000, e -> {
        time++;
        this.labelStatus.setText("Прошло времени (секунд): " + time);
    });

    private final ParamsDialog dialogParams;


    public MainForm() {
        this.setTitle("Поиск пути");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        setJMenuBar(createMenuBar());
        this.pack();

        SwingUtils.setShowMessageDefaultErrorHandler();

        tableGameField.setRowHeight(DEFAULT_CELL_SIZE);
        JTableUtils.initJTableForArray(tableGameField, DEFAULT_CELL_SIZE, false, false, false, false);
        tableGameField.setIntercellSpacing(new Dimension(0, 0));
        tableGameField.setEnabled(false);

        tableGameField.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            final class DrawComponent extends Component {
                private int row = 0, column = 0;

                @Override
                public void paint(Graphics gr) {
                    Graphics2D g2d = (Graphics2D) gr;
                    int width = getWidth() - 2;
                    int height = getHeight() - 2;
                    paintCell(row, column, g2d, width, height);
                }
            }


            DrawComponent comp = new DrawComponent();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                comp.row = row;
                comp.column = column;
                return comp;
            }
        });

        newGame();

        updateWindowSize();
        updateView();

        dialogParams = new ParamsDialog(params, tableGameField, e -> newGame());

        tableGameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = tableGameField.rowAtPoint(e.getPoint());
                int col = tableGameField.columnAtPoint(e.getPoint());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (teleportBox.isSelected()) {
                        game.leftMouseClickTeleport(row, col);
                    } else {
                        game.leftMouseClick(row, col);
                    }
                    updateView();
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    game.rightMouseClick(row, col);
                    updateView();
                }
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    game.middleMouseClick(row, col);
                    updateView();

                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    game.StartClick(typeMethods.getSelectedIndex(), checkBoxTypeTeleports.isSelected());
                    resultLabel.setText("Минимальное количество ходов до точки: " + game.getResult());
                    updateView();
                } catch (Exception e) {
                    SwingUtils.showErrorMessageBox(e);
                }
            }
        });
        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.cleanField();
                resultLabel.setText("Минимальное количество ходов до точки: " + 0);
                updateView();
            }
        });


        /*
            обработка событий нажатия клавиш (если в вашей программе не нужно, удалить код ниже)
            сделано так, а не через addKeyListener, так в последнем случае события будет получать компонент с фокусом,
            т.е. если на форме есть, например, кнопка или поле ввода, то все события уйдут этому компоненту
         */
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    System.out.printf("globalKeyPressed: %s, %s, %s%n",
                            e.getKeyChar(), e.getKeyCode(), e.getExtendedKeyCode());
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    System.out.printf("globalKeyReleased: %s, %s, %s%n",
                            e.getKeyChar(), e.getKeyCode(), e.getExtendedKeyCode());
                } else if (e.getID() == KeyEvent.KEY_TYPED) {
                    System.out.printf("globalKeyTyped: %s, %s, %s%n",
                            e.getKeyChar(), e.getKeyCode(), e.getExtendedKeyCode());
                }

                return false;
            }
        });
    }

    private JMenuItem createMenuItem(String text, String shortcut, Character mnemonic, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        if (shortcut != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut.replace('+', ' ')));
        }
        if (mnemonic != null) {
            menuItem.setMnemonic(mnemonic);
        }
        return menuItem;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBarMain = new JMenuBar();

        JMenu menuGame = new JMenu("Приложение");
        menuBarMain.add(menuGame);
        menuGame.add(createMenuItem("Новое поле", "ctrl+N", null, e -> {
            newGame();
        }));
        menuGame.add(createMenuItem("Параметры", "ctrl+P", null, e -> {
            dialogParams.updateView();
            dialogParams.setVisible(true);
        }));
        menuGame.addSeparator();
        menuGame.add(createMenuItem("Выход", "ctrl+X", null, e -> {
            System.exit(0);
        }));

        JMenu menuView = new JMenu("Вид");
        menuBarMain.add(menuView);
        menuView.add(createMenuItem("Подогнать размер окна", null, null, e -> {
            updateWindowSize();
        }));
        menuView.addSeparator();
        SwingUtils.initLookAndFeelMenu(menuView);

        JMenu menuHelp = new JMenu("Справка");
        menuBarMain.add(menuHelp);
        menuHelp.add(createMenuItem("Инструкция", "ctrl+R", null, e -> {
            SwingUtils.showInfoMessageBox(
                    "ПКМ по пустому полю -> START \n" +
                            "ПКМ по занятой ячейке -> удалить \n" +
                            "ЛКМ -> создать преграду \n" +
                            "Средняя КM -> END \n" +
                            "Телепорт, ЛКМ -> установить телепорт \n"
                    , "FAQ");
        }));
        menuHelp.add(createMenuItem("О программе", "ctrl+A", null, e -> {
            SwingUtils.showInfoMessageBox(
                    "Task 5 - СИАКОД 2024" +
                            "\n\nАвтор: Одинаев И.Д. \n" +
                            "О программе"
            );
        }));

        return menuBarMain;
    }

    private void updateWindowSize() {
        int menuSize = this.getJMenuBar() != null ? this.getJMenuBar().getHeight() : 0;
        SwingUtils.setFixedSize(
                this,
                tableGameField.getWidth() + 2 * DEFAULT_GAP + 60,
                tableGameField.getHeight() + panelMain.getY() + labelStatus.getHeight() +
                        menuSize + 1 * DEFAULT_GAP + 2 * DEFAULT_GAP + 60
        );
        this.setMaximumSize(null);
        this.setMinimumSize(null);
    }

    private void updateView() {
        tableGameField.repaint();
    }


    private Font font = null;

    private Font getFont(int size) {
        if (font == null || font.getSize() != size) {
            font = new Font("Comic Sans MS", Font.BOLD, size);
        }
        return font;
    }

    private void paintCell(int row, int column, Graphics2D g2d, int cellWidth, int cellHeight) { //раскрашивание клеток
        Game.Cell cellValue = game.getCell(row, column);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = Math.min(cellWidth, cellHeight);
        int bound = (int) Math.round(size * 0.1);
        Color color;
        String text = null;
        switch (cellValue.state) {
            case OPENED -> {
                return;
            }
            case START -> color = Color.RED;
            case CLOSED -> color = Color.BLACK;
            case END -> color = Color.GREEN;
            case RESULT -> {
                color = Color.YELLOW;
                text = String.valueOf(cellValue.value);
            }
            case TELEPORT -> {
                color = new Color(128, 0, 128); // Фиолетовый
                text = "*".concat(String.valueOf(cellValue.teleportNumber));
            }
            case CRAWL -> {
                color = Color.WHITE;
                text = String.valueOf(cellValue.value);
            }
            default -> color = Color.WHITE;

        }

        g2d.setColor(color);
        g2d.fillRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, bound * 3, bound * 3);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, bound * 3, bound * 3);

        if (text != null) {
            g2d.setFont(getFont(size - 6 * bound));
            g2d.setColor(DrawUtils.getContrastColor(color));
            DrawUtils.drawStringInCenter(g2d, font, text, 0, 0, cellWidth, (int) Math.round(cellHeight * 0.95));
        }
    }

    private void newGame() {
        game.newGame(params.getRowCount(), params.getColCount());
        JTableUtils.resizeJTable(tableGameField,
                game.getRowCount(), game.getColCount(),
                tableGameField.getRowHeight(), tableGameField.getRowHeight()
        );
        time = 0;
        timer.start();
        updateView();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(4, 3, new Insets(10, 10, 10, 10), -1, 10));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableGameField = new JTable();
        scrollPane1.setViewportView(tableGameField);
        startButton = new JButton();
        startButton.setText("Start");
        panelMain.add(startButton, new GridConstraints(2, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelMain.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        resultLabel = new JLabel();
        resultLabel.setText("Ответ");
        panelMain.add(resultLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelStatus = new JLabel();
        labelStatus.setText("Label");
        panelMain.add(labelStatus, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(64, 17), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
