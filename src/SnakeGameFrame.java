import javax.swing.*;
import java.awt.*;

public class SnakeGameFrame extends JFrame {

    public static JLabel score = new JLabel();

    SnakeGameFrame(int SPEED, int UNIT_SIZE) {
        this.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon("icon/Designer.png").getImage());
        SnakeGamePanel snakeGamePanel = new SnakeGamePanel(SPEED, UNIT_SIZE);

        JPanel scoreBoard = new JPanel();
        scoreBoard.setBackground(Color.BLACK);
        scoreBoard.setPreferredSize(new Dimension(600, 50));
        scoreBoard.setLayout(new GridLayout(1, 5));

        score.setForeground(Color.WHITE);
        score.setHorizontalAlignment(SwingConstants.CENTER);

        JComboBox<String> speed = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        int speedIndex = switch (SPEED) {
            case 150 -> 0;
            case 100 -> 1;
            case 50 -> 2;
            default -> 0;
        };
        speed.setSelectedIndex(speedIndex);
        speed.setFocusable(false);
        speed.addActionListener(e -> {
            switch (speed.getSelectedIndex()) {
                case 0 -> snakeGamePanel.timer.setDelay(150);
                case 1 -> snakeGamePanel.timer.setDelay(100);
                case 2 -> snakeGamePanel.timer.setDelay(50);
            }
            snakeGamePanel.SPEED = snakeGamePanel.timer.getDelay();
            snakeGamePanel.reset();
        });

        JComboBox<String> unitSize = new JComboBox<>(new String[]{"Small", "Medium", "Large"});
        int index = switch (UNIT_SIZE) {
            case 75 -> 0;
            case 50 -> 1;
            case 25 -> 2;
            default -> 1;
        };
        unitSize.setSelectedIndex(index);
        unitSize.setFocusable(false);
        unitSize.addActionListener(e -> {
            switch (unitSize.getSelectedIndex()) {
                case 0 -> snakeGamePanel.UNIT_SIZE = 75;
                case 1 -> snakeGamePanel.UNIT_SIZE = 50;
                case 2 -> snakeGamePanel.UNIT_SIZE = 25;
            }
            snakeGamePanel.GAME_UNITS = (snakeGamePanel.SCREEN_WIDTH * snakeGamePanel.SCREEN_HEIGHT) / snakeGamePanel.UNIT_SIZE;
            snakeGamePanel.x = new int[snakeGamePanel.GAME_UNITS];
            snakeGamePanel.y = new int[snakeGamePanel.GAME_UNITS];
            snakeGamePanel.reset();
        });

        JLabel speedLabel = new JLabel("Speed:");
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel unitSizeLabel = new JLabel("Size:");
        unitSizeLabel.setForeground(Color.WHITE);
        unitSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scoreBoard.add(speedLabel);
        scoreBoard.add(speed);
        scoreBoard.add(score);
        scoreBoard.add(unitSizeLabel);
        scoreBoard.add(unitSize);
        this.add(scoreBoard, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(600, 50));
        panel.setLayout(new GridLayout(1, 2));

        JButton pauseButton = new JButton("Pause");
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> {
            if (snakeGamePanel.timer.isRunning()) {
                snakeGamePanel.timer.stop();
                pauseButton.setText("Resume");
            } else {
                snakeGamePanel.timer.start();
                pauseButton.setText("Pause");
            }
        });

        JButton restartButton = new JButton("Restart");
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> {
            snakeGamePanel.reset();
        });

        panel.add(pauseButton);
        panel.add(restartButton);

        this.add(panel, BorderLayout.SOUTH);
        this.add(snakeGamePanel, BorderLayout.CENTER);

        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
}
