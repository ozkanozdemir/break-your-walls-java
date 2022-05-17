package BreakYourWalls;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapGenerator {
    // tuğlaların verilerini tutacak 2 boyutlu dizi - satır ve sütun şeklinde
    public int[][] map;

    // tuğla genişlik ve yükseklik değişkenleri
    public static int brickWidth;
    public static int brickHeight;

    // tuğla renkleri
    private List<Color> brickColors = new ArrayList<Color>();

    // private final Color textColor = new Color(220, 220, 220);

    // Sınıfın ana fonksiyonu
    public MapGenerator(int row, int col) {
        // olması gereken tuğla satır ve sütun sayısı kadar map dizisini doldur
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }

        // Frame'in genişlik ve yüksekliğine ve tuğlaların sayısına göre 1 adet tuğlanın genişlik ve yüksekliğini ayarla
        brickWidth = (int) (Main.DIM_WIDTH * 0.8f) / col;
        brickHeight = (int) (Main.DIM_HEIGHT * 0.25f) / row;

//        brickWidth = 540 / col;
//        brickHeight = 150 / row;

        // Her satırın rengini ayarla
        for (int i = 0; i < map.length; i++) {
            brickColors.add(MyColor.randomBrickColor());
        }
    }

    // Tuğlaların çizimi
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            // Her satır için rastgele tuğla rengi belirle

            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // position
                    int xPosition = j * brickWidth + ((int) (Main.DIM_WIDTH * 0.2f) / 2);
                    int yPosition = i * brickHeight + 50;

                    // Tuğlanın rengi
                    g.setColor(brickColors.get(i));
                    g.fillRect(xPosition, yPosition, brickWidth, brickHeight);

                    // Tuğlanın etrafında 3 px'lik arka plan rengi veriyoruz
                    // Bu sayede aralarında boşluk varmış gibi görünecek
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Gameplay.bgColor);
                    g.drawRect(xPosition, yPosition, brickWidth, brickHeight);

//                    if (tabus.length > (i * j) + j) {
//                        g.setFont(new Font("serif", Font.BOLD, 12));
//                        g.setColor(textColor);
//                        g.drawString(tabus[(i * j) + j], xPosition + 8, yPosition + (brickHeight / 2) + 4);
//                    }
                }
            }
        }
    }

    // Gameplay sınıfından bir tuğlayı kaldırabilmek için kullanıyoruz
    // Kendisine gelen değeri (tuğlayı kaldırmak için 0 gelecek) map dizisindeki konumunu bulup ona o değeri atıyoruz
    public void setBrickValue(int value, int row, int col, boolean playSound) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (value == 0 && playSound) {
            MyAudio.PlayAudio("src/BreakYourWalls/audio.wav");
        }

        map[row][col] = value;
    }
}
