import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

class SnakeGamePanel extends JPanel {

    public final int SCREEN_HEIGHT = 600;
    public final int SCREEN_WIDTH = 600;
    public int UNIT_SIZE;
    public int GAME_UNITS;
    public int SPEED;
    public int[] x;
    public int[] y;
    int bodyParts = 3;
    int applesEaten;
    int appleX;
    int appleY;
    static char direction = 'R';
    Timer timer;
    Random random;

    SnakeGamePanel(int speed, int unitSize) {
        UNIT_SIZE = unitSize;
        SPEED = speed;
        GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];
        random = new Random();

        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != 'R') direction = 'L';
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != 'L') direction = 'R';
                    }
                    case KeyEvent.VK_UP -> {
                        if (direction != 'D') direction = 'U';
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != 'U') direction = 'D';
                    }
                }
            }
        });
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        startGame();
    }

    public final void startGame() {
        newApple();
        timer = new Timer(SPEED, e -> {
            setScore(applesEaten);
            move();
            checkApple();
            checkCollisions();
            repaint(); // This method calls the paintComponent() method
        });
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // This method is called every time the timer ticks (i.e. every SPEED milliseconds).
        // Draw grid
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        // Draw apple
        g.drawImage(new ImageIcon("icon/apple.png").getImage(), appleX, appleY, UNIT_SIZE, UNIT_SIZE, null);

        // Draw snake
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(new Color(0, 255, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        setScore(applesEaten);
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();

            // Add Sound
            EatSound eatSound = new EatSound();
            eatSound.start();
        }
    }

    private void setScore(int applesEaten) {
        // Set the score in the label
         SnakeGameFrame.score.setText("Score: " + applesEaten);
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                timer.stop();
                gameOver();
            }
        }

        // Allowing snake to go through walls
        if (x[0] < 0) x[0] = SCREEN_WIDTH - UNIT_SIZE;
        if (x[0] >= SCREEN_WIDTH) x[0] = 0;
        if (y[0] < 0) y[0] = SCREEN_HEIGHT - UNIT_SIZE;
        if (y[0] >= SCREEN_HEIGHT) y[0] = 0;
    }

    public void gameOver() {
        int dialogResponse = JOptionPane.showConfirmDialog(null, "You lost!\nScore: " + applesEaten + "\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (dialogResponse == 0) {
            reset();
        } else {
            System.exit(0);
        }
    }

    public void reset() {
        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';

        // Reset snake position
        for (int i = 0; i < bodyParts; i++) {
            x[i] = UNIT_SIZE * (bodyParts - i);
            y[i] = 0;
        }
        timer.stop(); // Stop the previous timer before starting a new one to avoid multiple timers running at the same time
        startGame();
    }

    public int getSpeed() {
        // Get the speed from the speed JComboBox
        return SPEED;
    }
}