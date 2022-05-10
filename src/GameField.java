import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    public GameField() {
        setBackground(Color.yellow);
        loadImages();
        initGame();
        addKeyListener(new KeyField()); // обработчик событий
        setFocusable(true); // чтобы взаимодействие было с моим игр.полем
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE; // 48 потому что кратно 16(размер дотс)
            y[i] = 48;
        }
        timer = new Timer(250, this); // this - именно этот клас будет отвечать за обработку таймера + имплементить интерейс
        timer.start();
        createApple();
    }
    public void createApple() {
        appleX = new Random().nextInt(20)*DOT_SIZE;// 20 шт 16ти пиксельных квадрата может поместиться на мое поле
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    @Override // переопределение метода для перерисовки игрового поля
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) { //отрисовываемся когда мы в игре
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String GameOver = "Game Over";
            // Font f = new Font("Arial", 14, Font.BOLD);
            g.setColor(Color.red);
            // g.setFont(f);
            g.drawString(GameOver, 125, SIZE/2);
        }

    }

    public void move() { //логическая перерисовка точек в том массиве x y
        for (int i = dots; i > 0 ; i--) {
            x[i] = x[i - 1]; // т.о. сдвигаем, 2я точка становится на позицию 3ей и т.п.
            y[i] = y[i - 1]; //т.е. все точки, которые не голова мы переместили на предыдущие позиции
        }
        if (left) { // а вот и голова
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            createApple();
        }
    }
    public void checkCollisions() {  // не врезается ли в бордюры
        for (int i = dots; i > 0 ; i--) { //проверяю не столкнулся ли я сам с собой
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override // метод который обрабатывает действие каждые 250 м/с
    public void actionPerformed(ActionEvent e) {
        if (inGame){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    class KeyField extends KeyAdapter { // обработчик нажатия клавиш
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }

        }
    }
}
