package edu.edina.opmodes.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.edina.library.subsystems.MecanumDrive;
import edu.edina.library.util.Robot;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Disabled
@Config
@Autonomous(name="BottomLeftBlue", group = "autonomous")
public class AutonomousDriveBottomLeftBlue extends LinearOpMode {
    protected Robot robot;
    public double DISTANCE1 = 24;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new Robot(telemetry, hardwareMap, false);
        robot.init();

        //   robot.start();

        waitForStart();

        int i=0;

        while(i<=8)
        {
            robot.MecanumDrive.driveToPose(0,100,0);
            i = i+1;
        }

        i=0;
        while(i<=380)
        {
            robot.MecanumDrive.driveToPose(-100,0,0);
            i = i+1;
        }


////        drive.turn(Math.toRadians(90));
////        drive.followTrajectory(autonomousTrajectory);

//            while (opModeIsActive() && !isStopRequested()) {

//
//            }

    }
}
//}
