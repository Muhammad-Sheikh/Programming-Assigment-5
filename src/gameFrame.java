import javax.swing.*;
import java.awt.*;

public class gameFrame extends JFrame {
    gamePanel panel;

    public gameFrame()
    {
        panel = new gamePanel();
        this.add(panel);
        this.setTitle("Java Pong By Muhammed Sheikh"); //set title for frame
        this.setResizable(false); //frame can't change size
        this.setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //X button will stop program execution
        this.pack();//makes components fit in window - don't need to set JFrame size, as it will adjust accordingly
        this.setVisible(true); //makes window visible to user
        this.setLocationRelativeTo(null);//set window in middle of screen
    }

}
