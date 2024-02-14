package edu.edina.opmodes.teleop.test;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import edu.edina.library.util.PixelDetect;
import edu.edina.library.util.projection.Point;
import edu.edina.library.util.projection.Projector;
import edu.edina.library.util.projection.Vec;

public class PixelDetectProcessor implements org.firstinspires.ftc.vision.VisionProcessor {
    private static final int SAMPLE_COLS = 16;
    private static final int SAMPLE_ROWS = 16;
    private final Vec[] vectors;
    private Point[][] points;
    private boolean[][] mask;
    private String diagString;
    private Projector proj;
    private Mat hsvMat;
    private int[][][] hsvSample;
    private PixelDetect pixDet;

    public PixelDetectProcessor(Vec[] vectors) {
        this.vectors = vectors;
    }

    public String getDiagString() {
        return diagString;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        try {
            if (width != 640) throw new RuntimeException("width != 640");
            if (height != 480) throw new RuntimeException("height != 480");
            proj = new Projector(0.49976, 640, 480);
            hsvMat = new Mat();
            hsvSample = new int[SAMPLE_ROWS][][];
            points = new Point[SAMPLE_ROWS][];
            mask = new boolean[SAMPLE_ROWS][];

            pixDet = new PixelDetect(hsvSample, mask);

            Vec a = vectors[0];
            Vec b = vectors[3].sub(vectors[0]);
            Vec c = vectors[1].sub(vectors[0]);

            for (int i = 0; i < SAMPLE_ROWS; i++) {
                points[i] = new Point[SAMPLE_COLS];
                mask[i] = new boolean[SAMPLE_COLS];
                hsvSample[i] = new int[SAMPLE_COLS][];
                for (int j = 0; j < SAMPLE_COLS; j++) {
                    double dx = (double) j / (SAMPLE_COLS - 1);
                    double dy = (double) i / (SAMPLE_ROWS - 1);

                    dx = 2 * dx - 0.84;
                    dy = 2 * dy + 0.15;

                    Vec v = b.mul(dy)
                            .add(c.mul(dx))
                            .add(a);
                    points[i][j] = proj.project(v);
                }
            }

        } catch (Exception e) {
            diagString = String.format("error during init: %s", toString(e));
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        try {
            Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV_FULL);

            for (int i = 0; i < SAMPLE_ROWS; i++) {
                for (int j = 0; j < SAMPLE_COLS; j++) {
                    Point p = points[i][j];
                    double[] pix = hsvMat.get((int) p.y, (int) p.x);
                    hsvSample[i][j] = new int[]{(int) pix[0], (int) pix[1], (int) pix[2]};
                }
            }

            pixDet.detect();

            int[] iPix = hsvSample[SAMPLE_ROWS/2][SAMPLE_COLS/2];
            diagString = String.format("[0][0] = %d,%d,%d %s %s %s %s",
                    iPix[0], iPix[1], iPix[2],
                    PixelDetect.isWhite(iPix) > 0 ? "w" : " ",
                    PixelDetect.isPurple(iPix) > 0 ? "p" : " ",
                    PixelDetect.isOrange(iPix) > 0 ? "o" : " ",
                    PixelDetect.isGreen(iPix) > 0 ? "g" : " ");

            return null;
        } catch (Exception e) {
            diagString = String.format("error during init: %s", toString(e));
            return diagString;
        }
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint selPaint = new Paint();
        selPaint.setColor(Color.CYAN);
        selPaint.setStyle(Paint.Style.STROKE);
        selPaint.setStrokeWidth(scaleCanvasDensity * 4);

        Paint desPaint = new Paint();
        desPaint.setColor(Color.RED);
        desPaint.setStyle(Paint.Style.STROKE);
        desPaint.setStrokeWidth(scaleCanvasDensity * 4);

        for (int i = 0; i < SAMPLE_ROWS; i++) {
            for (int j = 0; j < SAMPLE_COLS; j++) {
                Point p = points[i][j];
                int x = (int) (p.x * scaleBmpPxToCanvasPx);
                int y = (int) (p.y * scaleBmpPxToCanvasPx);
                Paint pt = mask[i][j] ? selPaint : desPaint;
                canvas.drawRect(new Rect(x, y, x + 1, y + 1), pt);
            }
        }
    }

    private static String toString(Exception x) {
        String s = x.toString();
        for (StackTraceElement e : x.getStackTrace())
            s += "\n" + e;
        return s;
    }
}
