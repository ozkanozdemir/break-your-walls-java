package BreakYourWalls;

public class MyColor {
    // Tuğla rengi
    private static final int minBrickColor = 70;
    private static final int maxBrickColor = 255;

    // Rastgele renk oluşturma
    public static java.awt.Color randomBrickColor() {
        return new java.awt.Color(
                minBrickColor + (int) (Math.random() * (maxBrickColor - minBrickColor)),
                minBrickColor + (int) (Math.random() * (maxBrickColor - minBrickColor)),
                minBrickColor + (int) (Math.random() * (maxBrickColor - minBrickColor))
        );
    }
}
