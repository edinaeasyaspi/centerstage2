package edu.edina.opmodes.teleop.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;

import edu.edina.library.util.projection.Point;
import edu.edina.library.util.projection.Projector;
import edu.edina.library.util.projection.Vec;

public class VisionCalProcessor implements org.firstinspires.ftc.vision.VisionProcessor {
    private String diagString;
    private Point[] points;
    private Vec[] vectors;
    private Projector proj;

    private final AprilTagProcessor aprilTagProc;

    public VisionCalProcessor(AprilTagProcessor aprilTagProc) {
        this.aprilTagProc = aprilTagProc;
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
            proj = new Projector(0.49976, 640, 480);
        } catch (Exception e) {
            diagString = String.format("error during init: %s", toString(e));
        }
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        try {
            points = new Point[4];
            vectors = null;

            String s = "";

            for (AprilTagDetection det : aprilTagProc.getDetections()) {
                for (int i = 0; i < 4; i++) {
                    points[i] = new Point(det.corners[i].x, det.corners[i].y);
                    if (!s.isEmpty())
                        s += ",";
                    s += points[i];
                }

                break;
            }

            if (points[0] == null)
                return null;

            vectors = proj.backProjectSquare(points);
            for (Vec v : vectors) {
                s += "\n" + v;
            }

            diagString = s;

            return vectors;
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
        int[] colors = new int[]{
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.CYAN
        };

        int halfWidth = 5;

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (p == null)
                continue;

            Paint paint = new Paint();
            paint.setColor(colors[i]);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(scaleCanvasDensity * 4);

            float x = (float) (p.x * scaleBmpPxToCanvasPx);
            float y = (float) (p.y * scaleBmpPxToCanvasPx);
            canvas.drawLine(x, y - halfWidth, x, y + halfWidth, paint);
            canvas.drawLine(x - halfWidth, y, x + halfWidth, y, paint);
        }
    }
}
