import java.awt.*;
import java.util.Random;

public class pongBall extends Rectangle {

    public int ySpeedFactor = 3, xSpeedFactor = 3, newSpeed;
    public static int ball_size = 20;

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

    public void randomSkew()
    {
        Random random = new Random();
        x = x + random.nextInt(5);
        y = y + random.nextInt(5);
    }
    public void moveBall()
    {
        y = y + ySpeedFactor + newSpeed;
        x = x + xSpeedFactor + newSpeed;
    }
    public void paddleCollsionDetetced()
    {
        inverseXlinearMove();
        inverseYlinearMove();
    }

    public  void cornerDetection(int width, int height)
    {
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
            inverseXlinearMove();
        } else if(x <= 0)
        {
            if(xSpeedFactor < 0)
            {
                xSpeedFactor = ySpeedFactor *-1;
            }
        }

    }



    public void draw(Graphics component)
    {
        component.setColor(Color.blue);
        component.fillOval(x, y, ball_size, ball_size);
    }

}
