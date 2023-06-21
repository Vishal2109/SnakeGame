package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int[] snakexlength = new int[750];
    private int[] snakeylength = new int[750];
    private int lengthOfSnake = 3;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private int moves = 0;
    private Timer timer;
    private int delay = 100;
    private int[] appleXpos = {25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    private int[] appleYpos = {75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};
    private Random random = new Random();
    private int applex,appley;
    private int applesEaten =0;
    private boolean gameOver = false;


    public GamePanel() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        timer = new Timer(delay,this);
        timer.start();
        newApple();
    }


    ImageIcon snaketitle = new ImageIcon(getClass().getResource("snaketitle.jpg"));
    ImageIcon leftmouth = new ImageIcon(getClass().getResource("leftmouth.png"));
    ImageIcon rightmouth = new ImageIcon(getClass().getResource("rightmouth.png"));
    ImageIcon upmouth = new ImageIcon(getClass().getResource("upmouth.png"));
    ImageIcon downmouth = new ImageIcon(getClass().getResource("downmouth.png"));
    ImageIcon snakebody = new ImageIcon(getClass().getResource("snakeimage.png"));
    ImageIcon apple = new ImageIcon(getClass().getResource("enemy.png"));


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.drawRect(24,10,851,55);
        g.drawRect(24, 74, 851, 576);
        snaketitle.paintIcon(this,g,25,11);
        g.setColor(Color.BLACK);
        g.fillRect(25,75,850,575);


//        gives coordinate for initial body of snake
        if(moves==0){
            snakexlength[0] = 100;
            snakexlength[1] = 75;
            snakexlength[2] = 50;
            snakeylength[0] = 100;
            snakeylength[1] = 100;
            snakeylength[2] = 100;
        }

//        shows prompts to let user choose Difficulty level
        if(moves==0){
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.PLAIN,25));
            g.drawString("Press H for Hard",340,300);
            g.drawString("Press M for Medium",340,325);
            g.drawString("Press E for Easy",340,350);
        }

//        paints mouth icon according to direction of snake
        if(left==true){
            leftmouth.paintIcon(this,g, snakexlength[0], snakeylength[0]);
        }
        else if (right==true) {
            rightmouth.paintIcon(this,g, snakexlength[0], snakeylength[0]);
        }
        else if (up==true) {
            upmouth.paintIcon(this,g, snakexlength[0], snakeylength[0]);
        }
        else if(down==true){
            downmouth.paintIcon(this,g, snakexlength[0], snakeylength[0]);
        }

//        paints body of snake
        for(int i=1; i<lengthOfSnake; i++){
            snakebody.paintIcon(this,g, snakexlength[i], snakeylength[i]);
        }

        apple.paintIcon(this,g,applex,appley);

//        prompts on GameOver
        if(gameOver==true){
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.BOLD,50));
            g.drawString("Game Over",300,300);
            g.setFont(new Font("Arial",Font.PLAIN,20));
            g.drawString("Score : "+ getScore(applesEaten),395,350);
            g.setFont(new Font("Arial",Font.PLAIN,20));
            g.drawString("Press SPACE to Restart",325,400);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN,14));
        g.drawString("Score : "+ getScore(applesEaten),750,30);
        g.drawString("Length : "+lengthOfSnake,750,50);
        g.drawString("Difficulty : "+getDifficulty(timer.getDelay()),75,40);
        g.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

//        paints body of snake as snake moves
        for(int i=lengthOfSnake-1; i>0; i--){
            snakexlength[i] = snakexlength[i-1];
            snakeylength[i] = snakeylength[i-1];
        }

//        changes position of head of snake
        if(left){
            snakexlength[0] = snakexlength[0] - 25;
        }
        if(right){
            snakexlength[0] = snakexlength[0] + 25;
        }
        if(up){
            snakeylength[0] = snakeylength[0] - 25;
        }
        if(down){
            snakeylength[0] = snakeylength[0] + 25;
        }

//        enables snake to come out of opposite wall if its head goes into a wall
        if(snakexlength[0]>850){
            snakexlength[0]=25;
        }
        if(snakexlength[0]<25){
            snakexlength[0]=850;
        }
        if(snakeylength[0]>625){
            snakeylength[0]=75;
        }
        if(snakeylength[0]<75){
            snakeylength[0]=625;
        }

        SnakeEatsApple();
        collidesWithItself();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_H && moves<=0){
            timer.setDelay(75);
            moves++;
        }
        else if(e.getKeyCode()==KeyEvent.VK_M && moves<=0){
            timer.setDelay(100);
            moves++;
        }
        else if(e.getKeyCode()==KeyEvent.VK_E && moves<=0){
            timer.setDelay(125);
            moves++;
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE && gameOver==true){
            restart();
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT && !right){
            left=true;
            right=false;
            up=false;
            down=false;
            moves++;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT && !left){
            left=false;
            right=true;
            up=false;
            down=false;
            moves++;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP && !down){
            left=false;
            right=false;
            up=true;
            down=false;
            moves++;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN && !up){
            left=false;
            right=false;
            up=false;
            down=true;
            moves++;
        }
    }

    private void collidesWithItself() {
        for(int i=lengthOfSnake-1; i>0; i--){
            if(snakexlength[i]==snakexlength[0] && snakeylength[i]==snakeylength[0]){
                timer.stop();
                gameOver=true;
            }
        }
    }

    private void SnakeEatsApple() {
        if(snakexlength[0]==applex && snakeylength[0]==appley){
            newApple();
            lengthOfSnake++;
            applesEaten++;

        }
    }

    private void newApple() {
        applex = appleXpos[random.nextInt(34)];
        appley = appleYpos[random.nextInt(23)];
        for(int i=lengthOfSnake-1; i>=0; i--){
            if(snakexlength[i]==applex && snakeylength[i]==appley){
                newApple();
            }
        }
    }

    private void restart() {
        gameOver=false;
        moves=0;
        applesEaten =0;
        lengthOfSnake=3;
        left=false;
        right=true;
        up=false;
        down=false;
        timer.start();
        newApple();
        repaint();
    }

    private String getDifficulty(int timeDelay){
        if(timeDelay ==75){
            return "Hard";
        }
        else if(timeDelay ==100){
            return "Medium";
        }
        else if(timeDelay ==125){
            return "Easy";
        }
        else{
            return "";
        }
    }

    private int getScore(int applesEaten){
        int score = 0;
        if(timer.getDelay()==125){
            score = applesEaten;
        }
        else if(timer.getDelay()==100){
            score = applesEaten*2;
        }
        else if(timer.getDelay()==75){
            score = applesEaten*3;
        }
        return score;
    }



    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
