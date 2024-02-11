package edu.edina.opmodes.teleop.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import edu.edina.library.util.projection.Point;
import edu.edina.library.util.projection.Projector;
import edu.edina.library.util.projection.Vec;

public class BackProjectionVisionProcessor implements org.firstinspires.ftc.vision.VisionProcessor {
    private Mat hsvMat;
    private double fovDiag;
    private String diagString;
    private Point[] points;
    private ArrayList<Integer> xList, yList;
    private Projector proj;

    public BackProjectionVisionProcessor() {
        hsvMat = new Mat();
        points = new Point[4];
    }

    public String getDiagString() {
        return diagString;
    }

    public Point getPoint(int i) {
        return points[0];
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        try {
            if (width != 640)
                throw new RuntimeException("width != 640");
            if (height != 480)
                throw new RuntimeException("height != 480");
            xList = new ArrayList<>();
            yList = new ArrayList<>();
            proj = new Projector(0.49976, 640, 480);
        } catch (Exception e) {
            diagString = String.format("error during init: %s", toString(e));
        }
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        try {
            Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV_FULL);

            byte[] pix = new byte[3];
            hsvMat.get(320, 280, pix);
            diagString = String.format("hsv=%d %d %d", pix[0], pix[1], pix[2]);

            points[0] = hueMedian(hsvMat, 60);
            points[1] = hueMedian(hsvMat, 120);
            points[2] = hueMedian(hsvMat, 180);
            points[3] = hueMedian(hsvMat, 300);

            for (Point p : points)
                if (p == null)
                    return null;

            Vec[] v = proj.backProjectSquare(points);

            return points;
        } catch (Exception e) {
            diagString = String.format("error processing frame: %s", toString(e));
            return null;
        }
    }

    private static String toString(Exception x) {
        String s = x.toString();
        for (StackTraceElement e : x.getStackTrace())
            s += "\n" + e;
        return s;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(scaleCanvasDensity * 4);

        int halfWidth = 5;

        for (Point p : points) {
            if (p == null)
                continue;

            float x = (float) (p.x * scaleBmpPxToCanvasPx);
            float y = (float) (p.y * scaleBmpPxToCanvasPx);
            canvas.drawLine(x, y - halfWidth, x, y + halfWidth, paint);
            canvas.drawLine(x - halfWidth, y, x + halfWidth, y, paint);
        }
    }

    private Point hueMedian(Mat frame, int hue360) {
        int hueMin = (hue360 - 30)*360/256;
        int hueMax = (hue360 + 30)*360/256;

        xList.clear();
        yList.clear();

        byte[] pix = new byte[3];
        for (int i = 0; i < frame.height(); i++) {
            for (int j = 0; j < frame.width(); j++) {
                int hue = (int) pix[0];
                if (hue < 0)
                    hue += 360;

                frame.get(i, j, pix);
                if (hueMin <= hue && hue <= hueMax) {
                    xList.add(j);
                    yList.add(i);
                }
            }
        }

        if (xList.size() == 0)
            return null;

        return new Point(median(xList), median(yList));
    }

    private static int median(ArrayList<Integer> list) {
        Collections.sort(list);
        return list.get(list.size() / 2);
    }
}
