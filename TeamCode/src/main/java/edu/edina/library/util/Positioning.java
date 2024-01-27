package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

public class Positioning {
    public AprilTagProcessor getMyAprilTagProc() {
        return myAprilTagProc;
    }

    public AprilTagProcessor myAprilTagProc;

    private IMU imu;

    private double intialHeading;
    private final double[] allmx = {29.381, 35.381, 41.381, 100.0435, 106.0435, 112.0435};
    private final double[] allmy = {132.492908, 132.492908, 132.492908, 132.492908, 132.492908, 132.492908, 0, 0, 0, 0};
    private final double camOffsetX = 5.25, camOffsetY = 8;
    private Position currPos;

    public Positioning(RobotHardware hw) {
        this.imu = hw.imu;

        AprilTagProcessor.Builder myAprilTagProcBuilder = new AprilTagProcessor.Builder().setDrawTagID(true).setDrawTagOutline(true).setDrawAxes(true).setDrawCubeProjection(true);

        myAprilTagProc = myAprilTagProcBuilder.build();

        intialHeading = readImuHeading();

        currPos = new Position(72, 72, 0, 1, 0);
    }

    public Position getCurrPos() {
        return currPos;
    }

    public void setCurrPos(Position newPos) {
        currPos = newPos;
    }

    public Position readAprilTagPosition() {
        List<AprilTagDetection> currentDetections = myAprilTagProc.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null && detection.id <= 6) {
                int i = detection.id - 1;

                double mx = allmx[i];
                double my = allmy[i];

                double px = detection.ftcPose.x + camOffsetX;
                double py = detection.ftcPose.y + camOffsetY;

                double mc = Math.sqrt((px * px) + (py * py));

                double a = readImuHeading();
                double dx = Math.sin(Math.toRadians(detection.ftcPose.yaw));
                double dy = Math.cos(Math.toRadians(detection.ftcPose.yaw));

                double qc = dx * mc;
                double rx = qc + mx;

                double mq = dy * mc;
                double ry = my - mq;

                return new Position(rx, ry, dx, dy, a);
            }
        }

        return null;
    }

//    public Position getRelPosition(Position p) { //rename
//        List<AprilTagDetection> currentDetections = myAprilTagProc.getDetections();
//        for (AprilTagDetection detection : currentDetections) {
//            if (detection.metadata != null && detection.id <= 6) {
//                int i = detection.id - 1;
//
//                double detX = detection.ftcPose.x + camOffsetX;
//                double detY = detection.ftcPose.y + camOffsetY;
//
//                return new Position(p.x + detX, p.y + detY, p.dx, p.dy, p.a);
//            }
//        }
//
//        return null;
//    }

    public double readImuHeading() {
        Orientation myRobotOrientation;

        myRobotOrientation = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        double zAxis = myRobotOrientation.thirdAngle;

        return zAxis - intialHeading;
    }


    public void resetHeading() {
        intialHeading = readImuHeading();
    }

}
