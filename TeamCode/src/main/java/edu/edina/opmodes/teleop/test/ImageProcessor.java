package edu.edina.opmodes.teleop.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor implements org.firstinspires.ftc.vision.VisionProcessor {
    private int rectY = 160;
    public Rect rectMiddle = new Rect(80, 200, 160, 80);
    public Rect rectRight = new Rect(540, rectY, 80, 160);
    Selected selection = Selected.NONE;

    private final double SAT_THRESHOLD = 75;

    private final double VAL_THRESHOLD = 80;
    private final double SCORE_THRESHOLD = 0.4;

    public double scoreRectMiddle;
    public double scoreRectRight;
    Telemetry telemetry;
    Mat submat = new Mat();
    Mat hsvMat = new Mat();
    private String diagString;

    public ImageProcessor(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);

        //  satRectLeft = getAvgSaturation(hsvMat, rectLeft);
        scoreRectMiddle = objectScore(hsvMat, rectMiddle);
        scoreRectRight = objectScore(hsvMat, rectRight);

        diagString = String.format("scores: middle=%.3f, right=%.3f", scoreRectMiddle, scoreRectRight);

        if (scoreRectRight > scoreRectMiddle && scoreRectRight > SCORE_THRESHOLD) {
            return Selected.RIGHT;
        }
        if (scoreRectMiddle > scoreRectRight && scoreRectMiddle > SCORE_THRESHOLD) {
            return Selected.MIDDLE;
        }
        return Selected.LEFT;
    }


    protected double getAvgSaturation(Mat input, Rect rect) {
        //to do; boolean blue is true
        submat = input.submat(rect);
        Scalar color = Core.mean(submat);
        return color.val[1];
    }

    private double objectScore(Mat input, Rect rect) {
        submat = input.submat(rect);
        double pixelCount = 0.0;
        for (int y = 0; y < submat.rows(); y = y + 1) {
            for (int x = 0; x < submat.cols(); x = x + 1) {
                double[] pix = submat.get(y, x);
                if (pix[1] > SAT_THRESHOLD && pix[2] > VAL_THRESHOLD) {
                    pixelCount = pixelCount + 1;
                }
            }
        }
        double pixelFraction = pixelCount / (submat.cols() * submat.rows());

        return pixelFraction;
    }

    private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);

        return new android.graphics.Rect(left, top, right, bottom);
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight,
                            float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);

        Paint nonSelectedPaint = new Paint(selectedPaint);
        nonSelectedPaint.setColor(Color.GREEN);

//        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(rectLeft,
//                scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(rectMiddle,
                scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleRight = makeGraphicsRect(rectRight,
                scaleBmpPxToCanvasPx);

        selection = (Selected) userContext;
        switch (selection) {
            case LEFT:
                //              canvas.drawRect(drawRectangleLeft, selectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
            case MIDDLE:
                //              canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, selectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
            case RIGHT:
                //              canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, selectedPaint);
                break;
            case NONE:
                //             canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;

        }
    }

    public Selected getSelection() {
        return selection;
    }

    public String getDiagString() {
        return diagString;
    }

    public enum Selected {
        NONE,
        LEFT,
        MIDDLE,
        RIGHT
    }
}
