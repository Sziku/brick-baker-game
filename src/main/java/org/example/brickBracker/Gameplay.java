package org.example.brickBracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;

    private  int titalBricks = 21;
    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -5;

    private MapGenerator mapGenerator;


    public Gameplay(){
        mapGenerator = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }


    public void paint(Graphics g){
        //background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        //drawing map
        mapGenerator.draw((Graphics2D)g);


        //borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        //score
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,590, 30);

        //the padle
        g.setColor(Color.green);
        g.fillRect(playerX, 550,100,8);


        //the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY,20,20);

        if(titalBricks <=0){
            play = false;
            ballposX = 0;
            ballposY = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("You won, Scores: "+score,260, 300);

            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press Enter to Restart",230, 350);
        }

        if(ballposY > 570){
            play = false;
            ballposX = 0;
            ballposY = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over, Scores: "+score,190, 300);

            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Press Enter to Restart",230, 300);
        }

        g.dispose();

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play){
            if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX, 550 , 100 ,8))){
                ballYdir  = -ballYdir;
            }
            A: for (int i = 0; i < mapGenerator.map.length; i++) {
                for (int j = 0; j < mapGenerator.map[0].length; j++) {
                    if(mapGenerator.map[i][j]>0){
                        int brickX = j * mapGenerator.brickWidth + 80;
                        int brickY = i * mapGenerator.brickHeight + 80;
                        int brickWidth = mapGenerator.brickWidth;
                        int brickHeight = mapGenerator.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20,20 );
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            mapGenerator.setBrickValue(0, i,j);
                            titalBricks--;
                            score+=5;

                            if (ballposX + 19 < brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            }else {
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
            if(playerX >= 600){
                playerX = 600;
            }else {
                moveRight();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }else {
                moveLeft();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
               play = true;
               ballposY =  350;
               ballposX  = 120;
               ballYdir = - 2;
               ballXdir = -1;
               playerX = 310;
               score = 0;
               titalBricks = 21;
               mapGenerator = new MapGenerator(3,7);
               repaint();
            }
        }
    }

    private void moveLeft() {
        play = true;
        playerX-=20;
    }

    private void moveRight() {
        play = true;
        playerX+=20;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
