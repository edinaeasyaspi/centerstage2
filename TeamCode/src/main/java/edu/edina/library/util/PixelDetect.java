package edu.edina.library.util;

import org.opencv.core.Mat;

import edu.edina.library.util.drivecontrol.LinearFunc;
import edu.edina.library.util.drivecontrol.LinearFuncFitter;

public class PixelDetect {
    private final Mat mat;

    public PixelDetect(Mat mat) {
        this.mat = mat;
    }

    public void detect() {
        LinearFuncFitter fitter = new LinearFuncFitter(mat.rows() * mat.cols());
        for (int row = 1; row + 1 < mat.rows(); row++) {
            for (int col = 1; col + 1 < mat.cols(); col++) {
                if (detect(row, col)) {
                    fitter.sample(col, row);
                }
            }
        }

        LinearFunc f = fitter.fit();
    }

    private boolean detect(int row, int col) {
        int w = 0, p = 0, o = 0, g = 0;

        int[] pix = new int[3];

        mat.get(row - 1, col - 1, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        mat.get(row, col - 1, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        mat.get(row + 1, col - 1, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        mat.get(row + 1, col, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        mat.get(row + 1, col + 1, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        mat.get(row, col + 1, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        mat.get(row - 1, col + 1, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        mat.get(row - 1, col, pix);
        w += isWhite(pix);
        p += isPurple(pix);
        o += isOrange(pix);
        g += isGreen(pix);

        return w > 2 || p > 2 || o > 2 || g > 2;
    }

    private static int isWhite(int[] hsv) {
        return hsv[1] < 50 && hsv[2] > 200 ? 1 : 0;
    }

    private static int isPurple(int[] hsv) {
        return nearHue(hsv, 185) && hsv[1] + hsv[2] > 255 ? 1 : 0;
    }

    private static int isOrange(int[] hsv) {
        return nearHue(hsv, 30) && hsv[1] + hsv[2] > 350 ? 1 : 0;
    }

    private static int isGreen(int[] hsv) {
        return nearHue(hsv, 70) && hsv[1] + hsv[2] > 300 ? 1 : 0;
    }

    private static boolean nearHue(int[] hsv, int hue) {
        return Math.abs(hsv[0] - hue) < 30;
    }
}
