package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

public class Positioning {
    public AprilTagProcessor getMyAprilTagProc() {
        return myAprilTagProc;
    }

    public AprilTagProcessor myAprilTagProc;

    private IMU imu;

    private ModernRoboticsI2cGyro gyro;

    private double initialHeading;
    private final double[] allmx = {29.381, 35.381, 41.381, 100.0435, 106.0435, 112.0435};
    private final double[] allmy = {132.492908, 132.492908, 132.492908, 132.492908, 132.492908, 132.492908, 0, 0, 0, 0};
    private final double camOffsetX = 5.25, camOffsetY = 8;
    private Position currPos;

    public Positioning(RobotHardware hw) {
        this.gyro = hw.gyro;

        AprilTagProcessor.Builder myAprilTagProcBuilder = new AprilTagProcessor.Builder().setDrawTagID(true).setDrawTagOutline(true).setDrawAxes(true).setDrawCubeProjection(true);

        myAprilTagProc = myAprilTagProcBuilder.build();

        initialHeading = readImuHeading(false);

        currPos = new Position(72, 72, 0);
    }

    public Position getCurrPos() {
        return currPos;
    }

    public void setCurrPos(Position newPos) {
        currPos = newPos;
    }

    public Position readAprilTagPosition(boolean updatePosition) {
        List<AprilTagDetection> currentDetections = myAprilTagProc.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null && detection.id <= 6) {
                int i = detection.id - 1;

                double mx = allmx[i];
                double my = allmy[i];

                double px = detection.ftcPose.x + camOffsetX;
                double py = detection.ftcPose.y + camOffsetY;

                double mc = Math.sqrt((px * px) + (py * py));

                double a = readImuHeading(false);
                double dx = Math.sin(Math.toRadians(detection.ftcPose.yaw));
                double dy = Math.cos(Math.toRadians(detection.ftcPose.yaw));

                double qc = dx * mc;
                double rx = qc + mx;

                double mq = dy * mc;
                double ry = my - mq;

                return new Position(rx, ry, a);
            }
        }

        return null;
    }

    public double readImuHeading(boolean updatePosition) {
        double zAxis = gyro.getHeading();

        double heading = zAxis - initialHeading;

        if (updatePosition) {
            currPos = new Position(currPos.x, currPos.y, heading);
        }

        return heading;
    }
}
