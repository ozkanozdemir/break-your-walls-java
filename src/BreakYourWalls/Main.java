package BreakYourWalls;

import javax.swing.*;
import java.awt.*;

public class Main {
    // Pencerenin genişliği ve yüksekliği
    public static final int DIM_WIDTH = 900;
    public static final int DIM_HEIGHT = 750;

    // Sınıfın ana fonksiyonu
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Frame
                JFrame frame = new JFrame();

                // Gameplay sınınfından obje oluştur
                Gameplay gameplay = new Gameplay();

                // Frame'in genişlik ve yükseklik değerlerini ayarla
                frame.setBounds(10, 10, DIM_WIDTH, DIM_HEIGHT);

                // Frame'in başlığını ayarla
                frame.setTitle("Break Your Walls");

                // Frame yeniden boyutlandırılamaycak
                frame.setResizable(false);

                // Frame görünür olacak
                frame.setVisible(true);

                // Frame'in kapanması durumunda frame den çıkış yapılacak - kodlar çalışmayı bırakacak
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Frame'in içine gameplay objemizi ekliyoruz
                frame.add(gameplay);
            }
        });
    }

}
