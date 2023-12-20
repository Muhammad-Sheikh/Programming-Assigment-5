import java.awt.*;
import java.util.Random;

public class pongBall extends Rectangle {

    public int ySpeedFactor = 3, xSpeedFactor = 3;
    public static int ball_size = 20;
    public static boolean wallTouched;


    public pongBall(int x, int y) {
        super(x, y, ball_size, ball_size);
    }

    public void inverseYlinearMove()
    {
        ySpeedFactor = ySpeedFactor*-1;
    }

    public void inverseXlinearMove()
    {
        xSpeedFactor = xSpeedFactor*-1;
    }

    public void moveBall()
    {
        y = y + ySpeedFactor;
        x = x + xSpeedFactor;
    }
    public void paddleCollisionDetected                                                                                                                                                                   ()
    {
        inverseXlinearMove();
        inverseYlinearMove();
    }

    public  void cornerDetection(int width, int height)
    {
        wallTouched = false;
        if(y >= height)
        {
            inverseYlinearMove();
        } else if(y <= 0)
        {
            if(ySpeedFactor < 0)
            {
                ySpeedFactor = ySpeedFactor *-1;
            }
        }

        if(x >= width)
        {
            gamePanel.player1Score = gamePanel.player1Score +1;
            wallTouched = true;
        } else if(x <= 0)
        {
            if(xSpeedFactor < 0)
            {
                gamePanel.player2Score = gamePanel.player2Score +1;
                wallTouched = true;
            }
        }

    }



    public void draw(Graphics component)
    {
        component.setColor(Color.blue);
        component.fillOval(x, y, ball_size, ball_size);
    }

}
