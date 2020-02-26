package TratamientoDeImagen;

public class Pixel {
    private double[] valorPixel;
    private int x;
    private int y;

    public Pixel(double[] valorPixel, int x, int y) {
        this.valorPixel = valorPixel;
        this.x = x;
        this.y = y;
    }

    public double[] getValorPixel() {
        return valorPixel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
