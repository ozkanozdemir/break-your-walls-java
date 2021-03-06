package BreakYourWalls;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    // Constants
    // Genişlik ve yükseklikler px cinsindendir
    final int brickRowCount = 5; // tuğla satır sayısı
    final int brickColumnCount = 8; // tuğla sütun sayısı
    final int defaultTotalBricks = brickRowCount * brickColumnCount; // varsayılan toplam tuğla sayısı

    final int paddleWidth = 120; // Oyuncunun (küreğin) varsayılan genişliği
    final int paddleHeight = 8; // Oyuncu (kürek) yüksekliği
    final int defaultPlayerX = (Main.DIM_WIDTH / 2) - (paddleWidth / 2); // oyuncunun varsayılan başlangıç pozisyonu - X : soldan px olarak uzaklığı
    final int defaultPlayerY = Main.DIM_HEIGHT - 64; // oyuncunun varsayılan başlangıç pozisyonu - Y : yukarıdan px olarak uzaklığı

    final int defaultBallPositionX = 140; // Topun başlangıç pozisyonu - X : soldan px olarak uzaklığı
    final int defaultBallPositionY = Main.DIM_HEIGHT - 200; // Topun başlangıç pozisyonu - Y : yukarıdan px olarak uzaklığı
    final int defaultBallXDirection = -1; // Topun başlangıç yatay yönü
    final int defaultBallYDirection= -2; // Topun başlangıç dikey yönü
    final int ballWidth = 12; // Topun genişliği
    final int ballHeight = 12; // Topun yüksekliği

    final int borderWidth = 4; // Pencerenin kenarlıklarının kalınlığı

    final int scoreStep = 5; // skor artış miktarı

    // Variables
    private boolean play = false; // oyunun başlama durumu
    private boolean paused = false; // oyunun duraklama durumu
    private int score = 0; // skor

    private boolean ballXDirectionUpdating = false; // topun x yönünün güncellenme durumu
    private boolean ballYDirectionUpdating = false; // topun x yönünün güncellenme durumu

    private int totalBricks = defaultTotalBricks; // toplam tuğla sayısı

    private int playerX = defaultPlayerX; // Oyuncunun (küreğin) pozisyonu - X : soldan px olarak uzaklığı

    private int ballPositionX = defaultBallPositionX; // topun pozisyonu - X : soldan px olarak uzaklığı
    private int ballPositionY = defaultBallPositionY; // topun pozisyonu - Y : yukarıdan px olarak uzaklığı
    private int ballXDirection = defaultBallXDirection; // topun yatay yönü
    private int ballYDirection = defaultBallYDirection; // topun dikey yönü

    final int moveStep = 32 ; // sağ sol ok tuşlarına basınca kaç px hareket edeceği
    private final Timer timer; // hareket sağlamak için zamanlayıcı
    private int delay = 7; // Gecikme süresi

    // colors
    private Color confidenceTextColor = new Color(70, 68, 68, 60); // özgüven yazısının rengi
    public static Color bgColor = new Color(54, 52, 52); // arka plan rengi
    private final Color ballColor = new Color(220, 206, 206); // top rengi
    private final Color borderColor = new Color(0, 0, 0); // pencere kenarlığı rengi
    private final Color scoreColor = new Color(147, 145, 145); // skor rengi
    private final Color levelColor = new Color(147, 145, 145); // level rengi
    private final Color infoTextColor = new Color(100, 197, 0); // bilgilendirme yazıları rengi
    private final Color paddleColor = new Color(0, 197, 139); // bilgilendirme yazıları rengi
    private final Color pauseTextColor = new Color(58, 182, 229); // bilgilendirme yazıları rengi

    // inputs
//    public static JButton saveButton = new JButton("Kaydet");
//    public static JTextArea tabu1 = new JTextArea("Tabunu yaz...");
//    public static JTextArea tabu2 = new JTextArea("Tabunu yaz...");
//    public static JTextArea tabu3 = new JTextArea("Tabunu yaz...");
//    public static JTextArea tabu4 = new JTextArea("Tabunu yaz...");

    // tuğla oluşturucu sınıfı
    private MapGenerator map;

    // Sınıfın ana fonksiyonu
    public Gameplay() {
        // MapGenerator sınıfından obje oluştur
        map = new MapGenerator(brickRowCount, brickColumnCount);

        // klavyeye basma olaylarını dinle
        addKeyListener(this);

        // application'a focus ol
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Timer sınıfından zamanlayıcı objesini oluştur ve başlat
        timer = new Timer(delay, this);
        timer.start();
    }

    // Komponentin çizimleri
    protected void paintComponent(Graphics g) {
        // background
        g.setColor(bgColor);
        g.fillRect(1, 1,  Main.DIM_WIDTH - 8, Main.DIM_HEIGHT - 8);

        // confidence text
        g.setColor(confidenceTextColor);
        g.setFont(new Font("serif", Font.BOLD, 50));
        g.drawString("özgüven (confidence)", (Main.DIM_WIDTH / 2 - (220)), 160);

        // drawing map - tuğlaların çizimini oluştur
        map.draw((Graphics2D) g);

        // borders - pencere kenarlıkları
        g.setColor(borderColor);
        g.fillRect(0, 0, borderWidth, Main.DIM_HEIGHT - 8);
        g.fillRect(0, 0, Main.DIM_WIDTH - 8, borderWidth);
        g.fillRect(Main.DIM_WIDTH - 8, 0, borderWidth, Main.DIM_HEIGHT - 8);

        // scores
        g.setColor(scoreColor);
        g.setFont(new Font("serif", Font.BOLD, 42));
        g.drawString("" + score, Main.DIM_WIDTH - 70, 50);

        // the paddle - kürek
        g.setColor(paddleColor);
        g.fillRect(playerX, defaultPlayerY, paddleWidth, paddleHeight);

        // the ball
        g.setColor(ballColor);
        g.fillOval(ballPositionX, ballPositionY, ballWidth, ballHeight);

        // bricks finished - tuğlaların hepsi yok edildi
        if (totalBricks <= 0) {
            // oyun bitiş değikenini ayarla
            play = false;

            // Oyunu kazandın yazısı
            g.setColor(infoTextColor);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won \uD83D\uDE80 \uD83D\uDE80 \uD83D\uDE80", (Main.DIM_WIDTH / 2 - (120)), (Main.DIM_HEIGHT / 2) + 30);
            g.drawString("The Score : " + score, (Main.DIM_WIDTH / 2 - (105)), (Main.DIM_HEIGHT / 2) + 70);

            // Oyuna yeniden başla yazısı
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", (Main.DIM_WIDTH / 2 - (100)), (Main.DIM_HEIGHT / 2) + 110);

            // özgüven yazısının rengini değiştir
            confidenceTextColor = new Color(222, 222, 222, 222);
        }

        // oyun henüz hiç başlamadıysa başlama yazısını göster
        if (!play && score == 0) {
            g.setColor(infoTextColor);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Use left / right arrows", (Main.DIM_WIDTH / 2 - (130)), (Main.DIM_HEIGHT / 2));
            g.drawString("Press esc for pause", (Main.DIM_WIDTH / 2 - (124)), (Main.DIM_HEIGHT / 2 + 40));
            g.drawString("Press Enter to Start", (Main.DIM_WIDTH / 2 - (120)), (Main.DIM_HEIGHT / 2) + 80);
        }

        // oyunduraklatıldı yazısı
        if (play && paused) {
            g.setColor(pauseTextColor);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("paused", (Main.DIM_WIDTH / 2) - 40, (Main.DIM_HEIGHT / 2) + 40);
            g.drawString("Press \"R\" for Restart Game", (Main.DIM_WIDTH / 2) - 180, (Main.DIM_HEIGHT / 2) + 90);
        }

        // top aşağı düştü - oyun bitti
        if (ballPositionY > defaultPlayerY + paddleHeight + borderWidth) {
            // oyun bitiş değikenini ayarla
            play = false;

            // oyun bitti yazısı
            g.setColor(infoTextColor);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over! The Score : " + score, (Main.DIM_WIDTH / 2 - (180)), (Main.DIM_HEIGHT / 2) + 30);

            // oyuna yeniden başlama yazısı
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", (Main.DIM_WIDTH / 2 - (100)), (Main.DIM_HEIGHT / 2) + 80);
        }
    }

    // Sahnede herhangi bir eylem olması durumu
    // Değişkenlerin güncellenmesinin dinlenmesi ve değişkenlerin durumlarına göre işlem yapılması burada olur
    @Override // üst sınıftaki methodun üzerine yazılıyor
    public void actionPerformed(ActionEvent e) {
        // zamanlayıcıyı başlat
        timer.start();

        // oyun başladıysa ve durdurulmadıysa
        if (play && !paused) {
            // topun fiziksel alanı
            Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, ballWidth, ballHeight);
            // oyuncunun (küreğin) fiziksel alanı
            Rectangle playerRect = new Rectangle(playerX, defaultPlayerY, paddleWidth, paddleHeight);

            // top ve oyuncu (kürek) birbirine temas ettiyse topun yönünü güncelle
            if (ballRect.intersects(playerRect)) {
                onBallTouchedPaddle(ballRect, playerRect);
            }

            // Her tuğlanın fiziksel kapladığı alanı oluştur
            // TODO: sürekli bu çizim yapıldığı için işlemciyi çok yoruyor. Performans arttılması sağlanacak.
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * MapGenerator.brickWidth + ((int) (Main.DIM_WIDTH * 0.2f) / 2);
                        int brickY = i * MapGenerator.brickHeight + 50;
                        int brickWidth = MapGenerator.brickWidth;
                        int brickHeight = MapGenerator.brickHeight;

                        // Tuğlanın fiziksel alanı
                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                        // top bir tuğlaya temas etti
                        if (ballRect.intersects(brickRect)) {
                            // tuğlanın çizimini kaldır
                            try {
                                map.setBrickValue(0, i, j, true);
                            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                                ex.printStackTrace();
                            }

                            // toplam tuğla sayısını azalt
                            totalBricks--;

                            // scoru arttır
                            score += scoreStep;

                            // Top tuğlaya temas ettiğinde, topun yönü güncelleniyor
                            if (ballPositionX + (ballWidth - 1) <= brickRect.x
                                    || ballPositionX + 1 >= brickRect.x + brickRect.width) {
                                // Aynı anda 2 tuğlaya birden değdiğinde 2 adet güncelleme yapılıyor
                                // 2 yön değişikliği olduğu için top aynı yönde gitmeye devam ediyor
                                // Bu yüzden 33 ms'lik bir gecikme ekledik
                                if (!ballXDirectionUpdating) {
                                    ballXDirectionUpdating = true;
                                    ballXDirection = -ballXDirection;
                                    Helper.setTimeout(() -> ballXDirectionUpdating = false, 33);
                                }
                            } else {
                                if (!ballYDirectionUpdating) {
                                    ballYDirectionUpdating = true;
                                    ballYDirection = -ballYDirection;
                                    Helper.setTimeout(() -> ballYDirectionUpdating = false, 33);
                                }
                            }

                            // top bu tuğlaya çarptığı için döngüden çık
                            break;
                        }
                    }
                }
            }

            // Bu fonksiyon her çalıştığında topun X ve Y konumu değiştiriliyor
            ballPositionX += ballXDirection;
            ballPositionY += ballYDirection;

            // top soldaki duvara çarptı - topun yatay yönünü tersine çevir
            if (ballPositionX < borderWidth) {
                ballXDirection = -ballXDirection;
            }

            // top yukarudaki duvara çarptı - topun dikey yönünü tersine çevir
            if (ballPositionY < borderWidth) {
                ballYDirection = -ballYDirection;
            }

            // top sağdaki duvara çarptı - topun yatay yönünü tersine çevir
            if (ballPositionX > Main.DIM_WIDTH - (30 - borderWidth)) {
                ballXDirection = -ballXDirection;
            }
        }

        // Yeniden boya
        repaint();
    }

    @Override // üst sınıftaki methodun üzerine yazılıyor
    public void keyTyped(KeyEvent e) {}

    @Override // üst sınıftaki methodun üzerine yazılıyor
    public void keyReleased(KeyEvent e) {}

    // klavyede herhangi bir tuşa basıldığında çalışacak fonksiyon
    @Override // üst sınıftaki methodun üzerine yazılıyor
    public void keyPressed(KeyEvent e) {
        // on press right - sağ ok a basıldı veya "D" tuşuna basıldı
        // oyuncuyu (küreği) sağa kaydır
        if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && !paused && play) {
            moveRight();
            int maxPlayerX = (Main.DIM_WIDTH - paddleWidth - (2 * borderWidth));
            if (playerX > maxPlayerX) {
                playerX = maxPlayerX;
            }
        }

        // on press left - sol ok a veya "A" tuşuna basıldı
        // oyuncuyu (küreği) sola kaydır
        if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && !paused && play) {
            moveLeft();
            if (playerX < borderWidth) {
                playerX = borderWidth;
            }
        }

        // on press esc or p - esc veya p tuşuna basıldı
        // oyunu duraklat
        if (play && (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P)) {
            paused = !paused;
        }

        // restart the game - enter a basıldı
        // oyun başlamadıysa oyunu başlat ve başlangıç varsayılan değerlerini ayarla
        if (!play && e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame();
        }

        // on press r - oyunu yeniden başlat - oyun durakladığında çalışır
        if (e.getKeyCode() == KeyEvent.VK_R && paused) {
            restartGame();
        }

        // on press b - Hile :) - oyunu bitir
        if (e.getKeyCode() == KeyEvent.VK_B && play) {
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        // tuğlanın çizimini kaldır
                        try {
                            map.setBrickValue(0, i, j, false);
                        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            play = false;
            totalBricks = 0;
            score = 999;
        }
    }

    // oyuncuyu (küreği) sağa hareket ettirme
    public void moveRight() {
        play = true;
        paused = false;
        playerX += moveStep;
    }

    // oyuncuyu (küreği) sola hareket ettirme
    public void moveLeft() {
        play = true;
        paused = false;
        playerX -= moveStep;
    }

    // top oyuncuya (küreğe) temas ettiğinde topun yönünü güncellemek için çalışacak fonksiyon
    private void onBallTouchedPaddle(Rectangle ballRect, Rectangle playerRect) {
        // TODO: topun yönünün güncellenmesini geliştirilecek
        ballYDirection = -ballYDirection;

        // -3 ... +3
        ballXDirection = (int) Math.ceil(Math.abs(ballRect.x - playerRect.x) / (paddleWidth / 7.0f)) - 4;

        // topun yönüne ve oyuncuya (küreğe) değdiği noktaya göre gitmesi gereken yeni yönü ayarla
//        if (touchedIndex == 0) {
//            ballXDirection = -ballXDirection;
//        } else if (touchedIndex < 0) {
//            ballXDirection = ballXDirection < 0 ? touchedIndex - ballXDirection : ballXDirection + touchedIndex;
//        } else {
//            ballXDirection = ballXDirection > 0 ? touchedIndex - ballXDirection : ballXDirection + touchedIndex;
//        }

        // topun yönünü kendi belirlediğimiz sınırlar içinde tutalım
        ballXDirection = Math.min(ballXDirection, 3);
        ballXDirection = Math.max(ballXDirection, -3);

        // topun yönüne göre delayi (hızı) güncelle
        switch (ballXDirection) {
            case 0:
                delay = 5;
                break;
            case -1:
            case 1:
                delay = 7;
                break;
            case -2:
            case 2:
                delay = 10;
                break;
            case -3:
            case 3:
                delay = 12;
                break;
            default:
                delay = 6;
                break;
        }
        timer.setDelay(delay);
    }

    // oyunu (yeniden) başlatma fonksiyonu
    private void restartGame() {
        // tuğlaları oluştur
        if (score > 0) {
            map = new MapGenerator(brickRowCount, brickColumnCount);
        }

        play = true;
        paused = false;
        ballPositionX = defaultBallPositionX;
        ballPositionY = defaultBallPositionY;
        ballXDirection = defaultBallXDirection;
        ballYDirection = defaultBallYDirection;
        playerX = defaultPlayerX;
        score = 0;
        totalBricks = defaultTotalBricks;
        confidenceTextColor = new Color(200, 200, 200, 60);
        delay = 7;
        timer.setDelay(delay);

        // yeniden boya
        repaint();
    }
}
