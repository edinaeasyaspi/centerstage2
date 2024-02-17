package edu.edina.library.util;

import org.opencv.core.Mat;

import edu.edina.library.util.drivecontrol.LinearFunc;
import edu.edina.library.util.drivecontrol.LinearFuncFitter;

public class PixelDetect {
    private final double xOrigin, yOrigin;
    private final int[][][] sample;
    private final boolean[][] mask;

    public PixelDetect(int[][][] sample, boolean[][] mask, double xOrigin, double yOrigin) {
        this.sample = sample;
        this.mask = mask;
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
    }

    public Result detect() {
        int rows = sample.length, cols = sample[0].length;
        double xSum = 0;
        int xCount = 0;

        LinearFuncFitter fitter = new LinearFuncFitter(rows * cols);
        for (int row = 1; row + 1 < rows; row++) {
            for (int col = 1; col + 1 < cols; col++) {
                double x = col - xOrigin;
                double y = row - yOrigin;

                if (detect(row, col)) {
                    fitter.sample(x, y);
                    mask[row][col] = true;
                    xSum = xSum + x;
                    xCount = xCount + 1;
                } else {
                    mask[row][col] = false;
                }
            }
        }

        LinearFunc f = fitter.fit();

        double x0 = xSum / xCount;
        double y0 = f.eval(x0);
        double angle = Math.tan(f.beta);
        double q = Math.cos(angle) * f.alpha;
        double s = f.alpha * Math.sin(angle) + f.beta * x0 / Math.sin(angle);

        return new Result(x0, y0, Math.toDegrees(angle), s, q, f, xCount);
    }

    public static class Result {
        public final double x0, y0, angleDeg, s, d;
        public final LinearFunc f;
        public final int count;

        public Result(double x0, double y0, double angleDeg, double s, double d, LinearFunc f, int count) {
            this.x0 = x0;
            this.y0 = y0;
            this.angleDeg = angleDeg;
            this.s = s;
            this.d = d;
            this.f = f;
            this.count = count;
        }
    }

    private boolean detect(int row, int col) {
        int w = 0, p = 0, o = 0, g = 0;

        int[] pix;

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
        return hsv[1] < 50 && hsv[2] > 200 ? 1 : 0;
    }

    public static int isPurple(int[] hsv) {
        return nearHue(hsv, 185) && hsv[1] + hsv[2] > 180 ? 1 : 0;
    }

    public static int isOrange(int[] hsv) {
        return nearHue(hsv, 43) && hsv[1] + hsv[2] > 250 ? 1 : 0;
    }

    public static int isGreen(int[] hsv) {
        return nearHue(hsv, 70) && hsv[1] + hsv[2] > 200 ? 1 : 0;
    }

    private static boolean nearHue(int[] hsv, int hue) {
        return Math.abs(hsv[0] - hue) < 30;
    }
}
