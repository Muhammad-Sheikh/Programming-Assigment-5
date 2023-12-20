import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class gamePanel extends JPanel implements Runnable, KeyListener {
    public static final int gameWidth = 640;
    public static final int gameHeight = 640;

    public Thread gameTick;
    public Image ballImage, paddle1Image, paddle2Image;
    public Graphics graphics;
    public pongBall ball;
    public int userMouseY = 0;
    private Random rand = new Random();


    public static int player1Score, player2Score;
    public Paddles paddle1;
    public Paddles paddle2;

    public gamePanel() {

        this.addKeyListener(this);

        ball = new pongBall(gameWidth / 2, gameHeight / 2);
        this.setFocusable(true);

        paddle1 = new Paddles(gameWidth - 640, gameHeight / 2, Color.green, 'w', 's');
        paddle2 = new Paddles(gameWidth - 20, gameHeight / 2, Color.red, 'i', 'k');

        this.setPreferredSize(new Dimension(gameWidth, gameHeight));

        gameTick = new Thread(this);
        gameTick.start();
    }

    public void touchedWall()
    {
        ball.x = gameWidth /2;
        ball.y = gameHeight /2;

        ball.y = ball.y + rand.nextInt(-200, 100);
        ball.x = ball.x + rand.nextInt(-100, 100);
    }

    public void paint(Graphics g) {

        ballImage = createImage(gameWidth, gameHeight);
        paddle1Image = createImage(gameWidth, gameHeight);
        paddle2Image = createImage(gameWidth, gameHeight);

        graphics = paddle1Image.getGraphics();
        draw(graphics);
        g.drawImage(paddle1Image, 0, 0, this);

        graphics = paddle2Image.getGraphics();
        draw(graphics);
        g.drawImage(paddle2Image, 640, 0, this);

        graphics = ballImage.getGraphics();
        draw(graphics);

        g.drawImage(ballImage, 0, 0, this);


    }

    public void draw(Graphics g) {
        ball.draw(g);
        paddle1.draw(g);
        paddle2.draw(g);
        g.setColor(Color.black);
        g.drawString("Player 1 score: " + player1Score + " Player 2 Score: " + player2Score,gameWidth/2, 40 );
    }

    public void checkCollision() {
        if (ball.y <= 0) {
            ball.y = 0;
        }
        if (ball.y >= gameHeight - pongBall.ball_size) {
            ball.y = gameHeight - pongBall.ball_size;
        }
        if (ball.x <= 0) {
            ball.x = 0;
        }
        if (ball.x + pongBall.ball_size >= gameWidth) {
            ball.x = gameHeight - pongBall.ball_size;
        }


        if(ball.intersects(paddle2))
        {
            ball.inverseYlinearMove();
            ball.inverseXlinearMove();
        } else if(ball.intersects(paddle1))
        {
            ball.ySpeedFactor = 3;
            ball.xSpeedFactor = 3;
        }

    }




    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;

        while (true) { //this is the infinite game loop
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            //only move objects around and update screen if enough time has passed
            if (delta >= 1) {
                paddle1.movePaddle();
                paddle2.movePaddle();
                ball.moveBall();
                ball.cornerDetection(620, 620);
                if(pongBall.wallTouched) { touchedWall(); }
                checkCollision();
                repaint();
                delta--;
            }
        }


    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        paddle1.keyPressed(e);
        paddle2.keyPressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        paddle1.keyReleased(e);
        paddle2.keyReleased(e);
    }
}
