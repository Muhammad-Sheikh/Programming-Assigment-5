import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddles extends Rectangle {
    public int ySpeedFactor;
    public char upChar, downChar;
    Color color;
    public static int paddleHeight = 100, paddleWidth = 20;

    public Paddles(int x, int y, Color color,char upChar, char downChar) {
        super(x, y, paddleWidth, paddleHeight);
        this.color = color;
        this.upChar = upChar;
        this.downChar = downChar;
    }


    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == downChar) {
            ySpeedFactor = 0;
        }

        if (e.getKeyChar() == upChar) {
            ySpeedFactor = 0;
        }
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyChar() == upChar){
            inverseYLinearMove();
        }

        if(e.getKeyChar() == downChar){
            ySpeedFactor = 5;
        }

    }

    public void inverseYLinearMove()
    {
        ySpeedFactor = 5;
        ySpeedFactor = ySpeedFactor*-1;
    }


    public void movePaddle()
    {
        y = y + ySpeedFactor;
    }



    public void draw(Graphics component)
    {
        component.setColor(color);
        component.fillRect(x, y, paddleWidth, paddleHeight);
    }



}

