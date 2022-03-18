package BreakYourWalls;

import java.awt.*;

public class MapGenerator {
    // tuğlaların verilerini tutacak 2 boyutlu dizi - satır ve sütun şeklinde
    public int[][] map;

    // tuğla genişlik ve yükseklik değişkenleri
    public static int brickWidth;
    public static int brickHeight;

    // tuğla rengi
    private final Color brickColor = new Color(147, 39, 1, 255);

    // Sınıfın ana fonksiyonu
    public MapGenerator(int row, int col) {
        // olması gereken tuğla satır ve sütun sayısı kadar map dizisini doldur
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }

        // Frame'in genişlik ve yüksekliğine ve tuğlaların sayısına göre 1 adet tuğlanın genilik ve yüksekliğini ayarla
        brickWidth = (int) (Main.DIM_WIDTH * 0.8f) / col;
        brickHeight = (int) (Main.DIM_HEIGHT * 0.25f) / row;

//        brickWidth = 540 / col;
//        brickHeight = 150 / row;
    }

    // Tuğlaların çizimi
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Tuğlanın rengi
                    g.setColor(brickColor);
                    g.fillRect(j * brickWidth + ((int) (Main.DIM_WIDTH * 0.2f) / 2), i * brickHeight + 50, brickWidth, brickHeight);

                    // Tuğlanın etrafında 3 px'lik arka plan rengi veriyoruz
                    // Bu sayede aralarında boşluk varmış gibi görünecek
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Gameplay.bgColor);
                    g.drawRect(j * brickWidth + ((int) (Main.DIM_WIDTH * 0.2f) / 2), i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    // Gameplay sınıfından bir tuğlayı kaldırabilmek için kullanıyoruz
    // Kendisine geken değeri (tuğlayı kaldırmak için 0 gelecek) map dizisindeki konumunu bulup ona o değeri atıyoruz
    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
