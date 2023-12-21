import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class gamePanel extends JPanel implements Runnable, KeyListener, LineListener {
    public static final int gameWidth = 640;
    public static final int gameHeight = 640;
    public static int player1score, player2score, ballSpeed = 2, winningScore = 10;
    private boolean ballCollidedOnce, gameEnded = false, playCompleted;
    public Thread gameTick, musicTick;
    public Image ballImage, paddle1Image, paddle2Image;

    public Graphics graphics;
    public pongBall ball;
    private Random rand = new Random();
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

        musicTick = new Thread(this::selectTrack);
        musicTick.start();

        try {
            File gameScore = new File("currentScore.txt");
            if (gameScore.createNewFile()) {
                System.out.println("New Save File Made!");
            } else {
                System.out.println("Save File already exists!");
            }

            Scanner scoreReader = new Scanner(gameScore);
            while (scoreReader.hasNextLine()) {
                String data = scoreReader.nextLine();
                player1score = Integer.parseInt(data.substring(0, data.indexOf(' ')));
                player2score = Integer.parseInt(data.substring(data.indexOf(' ') + 1));
            }
        } catch (IOException e) {
            System.out.println("Error with save file!.");
            e.printStackTrace();
        }


    }

    public void writeScores() {
        try {
            FileWriter scoreUpdater = new FileWriter("currentScore.txt");
            if (player1score <= winningScore || player2score <= winningScore) {
                scoreUpdater.write(player1score + " " + player2score);
                scoreUpdater.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void playMusic(String filePath) {
        File soundTrack = new File(filePath);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundTrack);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.addLineListener(this);

            audioClip.open(audioStream);

            audioClip.start();

            while (!playCompleted) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            audioClip.close();

        } catch (UnsupportedAudioFileException ex) {
        } catch (LineUnavailableException ex) {
        } catch (IOException x) {
        }

    }

    public void touchedWall() {
        ball.x = gameWidth / 2;
        ball.y = gameHeight / 2;

        ball.y = ball.y + rand.nextInt(-200, 100);
        ball.x = ball.x + rand.nextInt(-100, 100);

        ballSpeed = 2;
        ball.updateSpeed();
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
        int middleX = getWidth() / 2;

        ball.draw(g);
        paddle1.draw(g);
        paddle2.draw(g);
        g.setColor(Color.black);


        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString(String.valueOf(player1score), gameWidth / 2 - 45, 40);
        g.drawString(String.valueOf(player2score), gameWidth / 2 + 20, 40);

        Graphics2D twoDimensionGraphic = (Graphics2D) g;

        float[] dashSpaces = {10, 10};
        BasicStroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashSpaces, 0);

        twoDimensionGraphic.setStroke(dashed);

        twoDimensionGraphic.drawLine(middleX, 0, middleX, getHeight());

        if (gameEnded) {
            if (player1score == 2) {
                graphics.setFont(new Font("TimesRoman", Font.PLAIN, 40));
                graphics.drawString("You win! ", gameWidth / 2 - 180, gameHeight / 2);
                graphics.drawString("You Lose! ", gameWidth / 2 + 35, gameHeight / 2);
            } else {
                graphics.setFont(new Font("TimesRoman", Font.PLAIN, 40));
                graphics.drawString("You Lose! ", gameWidth / 2 - 180, gameHeight / 2);
                graphics.drawString("You Win! ", gameWidth / 2 + 35, gameHeight / 2);
            }
        }

    }

    private void selectTrack() {
        int c;
        c = rand.nextInt(5);
        switch (c) {
            case 0:
                playMusic(System.getProperty("user.dir") + "\\track1.wav");
            case 1:
                playMusic(System.getProperty("user.dir") + "\\track2.wav");
            case 2:
                playMusic(System.getProperty("user.dir") + "\\track3.wav");
            case 3:
                playMusic(System.getProperty("user.dir") + "\\track4.wav");
            case 4:
                playMusic(System.getProperty("user.dir") + "\\track5.wav");
        }
    }

    public void setPaddleBound(Paddles e) {
        if (e.y <= 0) {
            e.y = 0;
        }
        if (e.y >= gameHeight - paddle1.height) {
            e.y = gameHeight - paddle1.height;
        }
    }

    public void gameEnd() {
        while (true) {
            gameEnded = true;
            ballSpeed = 0;
            ball.x = 700;
            ball.y = 700;
            draw(graphics);
            try {
                FileWriter scoreUpdater = new FileWriter("currentScore.txt");
                scoreUpdater.write("");
                scoreUpdater.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public void checkCollision() {
        setPaddleBound(paddle1);
        setPaddleBound(paddle2);

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


        if (ball.intersects(paddle2)) {
            ball.inverseXlinearMove();
            ballCollidedOnce = true;
            increaseSpeed();
        } else if (ball.intersects(paddle1)) {
            ball.xSpeedFactor = ballSpeed;
            ballCollidedOnce = true;
            increaseSpeed();
        }

    }


    public void increaseSpeed() {
        if (ballCollidedOnce) {
            ballSpeed = ballSpeed + 1;
            if (ballSpeed > 7) {
                ballSpeed = 7;
            }
            ball.updateSpeed();
            return;
        }
        ballCollidedOnce = true;
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
                if (pongBall.wallTouched) {
                    touchedWall();
                }
                checkCollision();
                writeScores();
                repaint();
                if (player1score == winningScore || player2score == winningScore) {
                    gameEnd();
                }
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

    @Override
    public void update(LineEvent event) {
        LineEvent.Type eventType = event.getType();
        if (eventType == LineEvent.Type.STOP) {
            playCompleted = true;
            selectTrack();
        }
    }
}
