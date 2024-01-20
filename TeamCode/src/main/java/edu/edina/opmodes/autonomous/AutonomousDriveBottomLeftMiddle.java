package edu.edina.opmodes.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.edina.library.subsystems.MecanumDrive;
import edu.edina.library.util.Robot;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
public class AutonomousDriveBottomLeftMiddle extends LinearOpMode {
    public double DISTANCE1 = 96;
    protected Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        // Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        robot = new Robot(telemetry, hardwareMap, false);
        robot.init();

        //   robot.start();

        //  Pose2d poseEstimate = drive.getPoseEstimate();
        //  telemetry.addData("finalX", poseEstimate.getX());
        //  telemetry.addData("finalY", poseEstimate.getY());
        //  telemetry.addData("finalHeading", poseEstimate.getHeading());
        //  telemetry.update();

        waitForStart();

        int i = 0;

        while (i <= 175) {
            robot.MecanumDrive.driveToPose(0, 100, 0);
            i = i + 1;
        }

        i = 0;
        while (i <= 380) {
            robot.MecanumDrive.driveToPose(-100, 0, 0);
            i = i + 1;
        }


////        drive.turn(Math.toRadians(90));
////        drive.followTrajectory(autonomousTrajectory);

//            while (opModeIsActive() && !isStopRequested()) {
//                drive.followTrajectory(autonomousTrajectory);
//
//            }

    }
}
//}


