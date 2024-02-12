package edu.edina.opmodes.teleop.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.edina.library.util.projection.Point;
import edu.edina.library.util.projection.Projector;
import edu.edina.library.util.projection.Vec;

public class PixelDetectProcessor implements org.firstinspires.ftc.vision.VisionProcessor {
    private final Vec[] vectors;
    private Point[][] points;
    private String diagString;
    private Projector proj;
    private Mat hsvMat;
    private final double SAT_THRESHOLD = 75;

    private final double VAL_THRESHOLD = 80;

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
            points = new Point[9][];

            Vec a = vectors[0];
            Vec b = vectors[1].sub(vectors[0]);
            Vec c = vectors[3].sub(vectors[0]);

            for (int i = 0; i < 9; i++) {
                points[i] = new Point[9];
                for (int j = 0; j < 9; j++) {
                    Vec v = b.mul(i / 8.0).add(c.mul(j / 8.0)).add(a);
                    points[i][j] = proj.project(v);

                    if (i == 0 && j == 0)
                        diagString = String.format("v=%s, p=%s", v, points[i][j]);
                }
            }
        } catch (Exception e) {
            diagString = String.format("error during init: %s", e.toString());
        }
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);

        diagString = "";
        for (int i = 0; i < 9; i++) {
            diagString += "\n";

            for (int j = 0; j < 9; j++) {
                Point p = points[i][j];
                double[] pix = hsvMat.get((int) p.x, (int) p.y);
                if (p.x < 0 || p.x >= 640)
                    return new RuntimeException("bad x");
                if (p.x < 0 || p.x >= 480)
                    return new RuntimeException("bad y");
                if (pix[1] > SAT_THRESHOLD || pix[2] > VAL_THRESHOLD)
                    diagString += "1";
                else
                    diagString += "0";
            }
        }

        return diagString;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(scaleCanvasDensity * 4);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Point p = points[i][j];
                int x = (int) (p.x * scaleBmpPxToCanvasPx);
                int y = (int) (p.y * scaleBmpPxToCanvasPx);
                canvas.drawRect(new Rect(x, y, x + 1, y + 1), paint);
            }
        }
    }
}
