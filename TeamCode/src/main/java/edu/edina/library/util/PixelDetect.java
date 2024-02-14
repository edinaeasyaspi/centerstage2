package edu.edina.library.util;

import org.opencv.core.Mat;

import edu.edina.library.util.drivecontrol.LinearFunc;
import edu.edina.library.util.drivecontrol.LinearFuncFitter;

public class PixelDetect {
    private final int[][][] sample;
    private final boolean[][] mask;

    public PixelDetect(int[][][] sample, boolean[][] mask) {
        this.sample = sample;
        this.mask = mask;
    }

    public void detect() {
        int rows = sample.length, cols = sample[0].length;

        LinearFuncFitter fitter = new LinearFuncFitter(rows * cols);
        for (int row = 1; row + 1 < rows; row++) {
            for (int col = 1; col + 1 < cols; col++) {
                if (detect(row, col)) {
                    fitter.sample(col, row);
                    mask[row][col] = true;
                } else {
                    mask[row][col] = false;
                }
            }
        }

        LinearFunc f = fitter.fit();
    }

    private boolean detect(int row, int col) {
        int w = 0, p = 0, o = 0, g = 0;

        int[] pix = new int[3];

        pix = sample[row - 1][col - 1];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        pix = sample[row][col - 1];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        pix = sample[row + 1][col - 1];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        pix = sample[row + 1][col];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        pix = sample[row + 1][col + 1];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        pix = sample[row][col + 1];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        pix = sample[row - 1][col + 1];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        pix = sample[row - 1][col];
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        return w > 2 || p > 2 || o > 2 || g > 2;
    }

    public static int isWhite(int[] hsv) {
        return hsv[1] < 50 && hsv[2] > 150 ? 1 : 0;
    }

    public static int isPurple(int[] hsv) {
        return nearHue(hsv, 185) && hsv[1] + hsv[2] > 180 ? 1 : 0;
    }

    public static int isOrange(int[] hsv) {
        return nearHue(hsv, 30) && hsv[1] + hsv[2] > 250 ? 1 : 0;
    }

    public static int isGreen(int[] hsv) {
        return nearHue(hsv, 70) && hsv[1] + hsv[2] > 200 ? 1 : 0;
    }

    private static boolean nearHue(int[] hsv, int hue) {
        return Math.abs(hsv[0] - hue) < 30;
    }
}
