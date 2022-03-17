package BreakYourWalls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    // Constants
    final int defaultTotalBricks = 45;
    final int defaultPlayerX = 280;
    final int defaultPlayerY = 550;
    final int defaultBallPositionX = 120;
    final int defaultBallPositionY = 310;
    final int defaultBallXDirection = -1;
    final int defaultBallYDirection= -2;
    final int ballWidth = 15;
    final int ballHeight = 15;
    final int paddleWidth = 100;
    final int paddleHeight = 8;

    final int moveStep = 30 ;
    final Timer timer;
    final int delay = 7;


    private boolean play = false;
    private int score = 0;

    private int totalBricks = defaultTotalBricks;

    private int playerX = defaultPlayerX;

    private int ballPositionX = defaultBallPositionX;
    private int ballPositionY = defaultBallPositionY;
    private int ballXDirection = defaultBallXDirection;
    private int ballYDirection = defaultBallYDirection;

    private Color confidenceTextColor = new Color(200, 200, 200, 60);

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(5, 9);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1,  692, 592);

        // confidence
        g.setColor(confidenceTextColor);
        g.setFont(new Font("serif", Font.BOLD, 50));
        g.drawString("özgüven (confidence)", 110, 160);

        // drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, defaultPlayerY, paddleWidth, paddleHeight);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballPositionX, ballPositionY, ballWidth, ballHeight);

        // bricks finished
        if (totalBricks <= 0) {
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!!! The Score : " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);

            confidenceTextColor = new Color(255, 255, 255, 255);
        }

        // to start text
        if (!play) {
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Start", 230, 350);
        }

        // the ball fell
        if (ballPositionY > 570) {
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over! The Score : " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, ballWidth, ballHeight);
            Rectangle playersRect = new Rectangle(playerX, defaultPlayerY, 100, 8);
            if (ballRect.intersects(playersRect)) {
                ballYDirection = -ballYDirection;
                ballXDirection = (int) (Math.abs(ballRect.x - playersRect.x) - (paddleWidth / 2)) / 20;
            }

            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width) {
                                ballXDirection = -ballXDirection;
                            } else {
                                ballYDirection = -ballYDirection;
                            }

                            break;
                        }
                    }
                }
            }

            ballPositionX += ballXDirection;
            ballPositionY += ballYDirection;

            if (ballPositionX < 0) {
                ballXDirection = -ballXDirection;
            }

            if (ballPositionY < 0) {
                ballYDirection = -ballYDirection;
            }

            if (ballPositionX > 670) {
                ballXDirection = -ballXDirection;
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // on press right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight();
            if (playerX > 591) {
                playerX = 591;
            }
        }

        // on press left
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft();
            if (playerX < 3) {
                playerX = 3;
            }
        }

        // restart the game
        if (!play && e.getKeyCode() == KeyEvent.VK_ENTER) {
            play = true;
            ballPositionX = defaultBallPositionX;
            ballPositionY = defaultBallPositionY;
            ballXDirection = defaultBallXDirection;
            ballYDirection = defaultBallYDirection;
            playerX = defaultPlayerX;
            score = 0;
            totalBricks = defaultTotalBricks;
            confidenceTextColor = new Color(200, 200, 200, 60);

            map = new MapGenerator(5, 9);

            repaint();
        }
    }

    public void moveRight() {
        play = true;
        playerX += moveStep;
    }

    public void moveLeft() {
        play = true;
        playerX -= moveStep;
    }
}
