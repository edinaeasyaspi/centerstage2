package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

public class Positioning {
    private static final boolean useGyro = false;

    public AprilTagProcessor getMyAprilTagProc() {
        return myAprilTagProc;
    }

    public AprilTagProcessor myAprilTagProc;

    private boolean rearFacing;

    private IMU imu;

    private ModernRoboticsI2cGyro gyro;

    private double initialHeading;
    private final double[] allmx = {29.381, 35.381, 41.381, 100.0435, 106.0435, 112.0435};
    private final double[] allmy = {132.492908, 132.492908, 132.492908, 132.492908, 132.492908, 132.492908, 0, 0, 0, 0};
    private final double camOffsetX = 5.25, camOffsetY = 8;
    private Position currPos;

    public Positioning(RobotHardware hw, boolean rearFacing) {
        this.rearFacing = rearFacing;
        this.gyro = hw.gyro;
        this.imu = hw.imu;

        AprilTagProcessor.Builder myAprilTagProcBuilder = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true);

        myAprilTagProc = myAprilTagProcBuilder.build();

        setCurrPos(new Position(72, 72, 0));
    }

    public Position getCurrPos() {
        return currPos;
    }

    public void setCurrPos(Position newPos) {
        currPos = newPos;

        if (useGyro) {
            initialHeading = gyro.getHeading() - newPos.a;
        } else {
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            initialHeading = robotOrientation.getYaw(AngleUnit.DEGREES) - newPos.a;
        }
    }

    // todo: actually update position
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

                double a = readHeading(false);
                double yaw = detection.ftcPose.yaw;
                if (rearFacing) {
                    yaw = yaw + 180;
                }
                double dx = Math.sin(Math.toRadians(yaw));
                double dy = Math.cos(Math.toRadians(yaw));

                double qc = dx * mc;
                double rx = qc + mx;

                double mq = dy * mc;
                double ry = my - mq;

                Position p = new Position(rx, ry, a);

                if (updatePosition) {
                    currPos = p;
                }

                return p;
            }
        }

        return null;
    }

    public double readHeading(boolean updatePosition) {
        double zAxis;

        if (useGyro) {
            zAxis = gyro.getHeading();
        } else {
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            zAxis = robotOrientation.getYaw(AngleUnit.DEGREES);
        }

        double heading = zAxis - initialHeading;

        if (updatePosition) {
            currPos = new Position(currPos.x, currPos.y, heading);
        }

        return heading;
    }
}
