import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 48;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private int highScore = 0;
    private final BufferedImage backgroundImage;
    private MapGenerator map;
    private ImageIcon paddleIcon;
    private ImageIcon ballIcon;

    public Gameplay() throws IOException {
        map = new MapGenerator(2,2);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        paddleIcon = new ImageIcon("/SchoolWork/softwareDesign/BrickBreakerGame/src/paddle.jpg");
        ballIcon = new ImageIcon("/SchoolWork/softwareDesign/BrickBreakerGame/src/ball.png");
        backgroundImage = ImageIO.read(new File("/SchoolWork/softwareDesign/BrickBreakerGame/src/background.jpg")); // Change "background.jpg" to your image file path

    }

    public void paintComponent(Graphics g){
        //background
        super.paintComponent(g);
        if(backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        //drawing map
        map.draw((Graphics2D)g);

        //borders
        g.setColor(Color.blue);
        g.fillRect(0, 0 ,3 ,592);
        g.fillRect(0, 0 ,692 ,3);
        g.fillRect(691, 0 ,3 ,592);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("comic sans", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        g.setColor(Color.white);
        g.setFont(new Font("comic sans", Font.BOLD, 25));
        g.drawString("High Score: " + highScore, 30, 590);



        //the paddle
       Image paddleImage =  paddleIcon.getImage();
       g.drawImage(paddleImage, playerX, 500,80,80, this);

        //the ball
      Image ballImage = ballIcon.getImage();
      g.drawImage(ballImage, ballposX, ballposY, 10, 10, this);

        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            if(score > highScore){
                highScore = score;
            }
            g.setColor(Color.blue);
            g.setFont(new Font("comic sans", Font.BOLD, 30));
            g.drawString("You won! Score: " + score, 190, 300);

            g.setFont(new Font("comic sans", Font.BOLD, 20));
            g.drawString("High Score: " + highScore, 280, 400);

            g.setFont(new Font("comic sans", Font.BOLD, 20));
            g.drawString("Press enter to Restart", 230, 350);
        }

        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            if(score > highScore){
                highScore = score;
            }
            g.setColor(Color.blue);
            g.setFont(new Font("comic sans", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("comic sans", Font.BOLD, 20));
            g.drawString("High Score: " + highScore, 280, 400);

            g.setFont(new Font("comic sans", Font.BOLD, 20));
            g.drawString("Press enter to Restart", 250, 350);
        }
        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    timer.start();
    if(play){
        if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8))){
            ballYdir = -ballYdir;
            ballXdir = -2;
        }
        else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8))){
            ballYdir = -ballYdir;
            ballXdir = ballXdir + 1;
        }
        else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8)))
        {
            ballYdir = -ballYdir;
        }

        A: for(int i = 0; i < map.map.length; i++){
            for(int j = 0; j < map.map[0].length; j++){
                if(map.map[i][j] > 0){
                    int bricksX = j * map.brickWidth + 80;
                    int bricksY = i * map.brickHeight + 50;
                    int brickWidth = map.brickWidth;
                    int brickHeight = map.brickHeight;

                    Rectangle rect = new Rectangle(bricksX, bricksY, brickWidth, brickHeight);
                    Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                    if(ballRect.intersects(rect)){
                        map.setBricksValue(0,i,j);
                        score += 5;
                        totalBricks--;

                        if(ballposX + 19 <= rect.x || ballposX + 1 >= rect.x + rect.width){
                            ballXdir = -ballXdir;
                        }else{
                            ballYdir = -ballYdir;
                        }
                        break A;
                    }
                }
            }
        }
        ballposX += ballXdir;
        ballposY += ballYdir;
        if(ballposX < 0){
            ballXdir = -ballXdir;
        }
        if(ballposY < 0){
            ballYdir = -ballYdir;
        }
        if(ballposX > 670){
            ballXdir = -ballXdir;
        }
    }
    repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600 ){
                playerX = 600;
            } else{
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);

                repaint();
            }
        }
    }
    public void moveRight(){
        play = true;
        playerX+=20;
    }
    public void moveLeft(){
        play = true;
        playerX-=20;
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

}
