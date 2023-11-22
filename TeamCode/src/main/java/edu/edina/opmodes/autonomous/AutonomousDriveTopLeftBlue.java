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

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
public class AutonomousDriveTopLeftBlue extends LinearOpMode {
    public double DISTANCE1 = 96;

    @Override
    public void runOpMode() throws InterruptedException {
       // Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        MecanumDrive drive = new MecanumDrive(hardwareMap);


        Trajectory autonomousTrajectory = drive.setTrajectory(new Pose2d())//-35,61
                .lineToLinearHeading(new Pose2d(25, 0, Math.toRadians(0)))

                // .splineTo(new Vector2d(-24,6), Math.toRadians(0))
                // .splineTo(new Vector2d(-6,26), Math.toRadians(0))


                //.strafeLeft(25)
                // .forward(25)
                // .strafeLeft(25)
                // .back(25)
                //.strafeLeft(45)
                .build();


//          //  TrajectoryBuilder trajectory = drive.trajectoryBuilder(new Pose2d());
//          //  trajectory.strafeLeft(DISTANCE1);
//          //  trajectory.build();

      //  Pose2d poseEstimate = drive.getPoseEstimate();
      //  telemetry.addData("finalX", poseEstimate.getX());
      //  telemetry.addData("finalY", poseEstimate.getY());
      //  telemetry.addData("finalHeading", poseEstimate.getHeading());
      //  telemetry.update();

        waitForStart();

////        drive.turn(Math.toRadians(90));
////        drive.followTrajectory(autonomousTrajectory);

//            while (opModeIsActive() && !isStopRequested()) {
//                drive.followTrajectory(autonomousTrajectory);
//
//            }

    }
}
//}


