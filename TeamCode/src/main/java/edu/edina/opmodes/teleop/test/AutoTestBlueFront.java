package edu.edina.opmodes.teleop.test;

import static edu.edina.library.util.drivecontrol.DriveDirection.Axial;
import static edu.edina.library.util.drivecontrol.DriveDirection.Lateral;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.LEFT;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.MIDDLE;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.RIGHT;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.library.util.DriveStatus;
import edu.edina.library.util.PiBot;
import edu.edina.library.util.Point;
import edu.edina.library.util.Position;
import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.DriveDirection;

@Autonomous
public class AutoTestBlueFront extends LinearOpMode {
    private static final boolean testMode = true;
    private PiBot piBot;

    private RobotHardware hw;

    private Selected position;

    public void runOpMode() {
        hw = new RobotHardware(hardwareMap, telemetry);
        piBot = new PiBot(hw);

        while (opModeInInit()) {
            position = piBot.getSelection();
        }

        Positioning posn = piBot.getPositioning();
        posn.setCurrPos(new Position(0, 36, -90));

        waitForStart();

        while (opModeIsActive()) {
            if (position == LEFT) {
                driveToClosestPoint(24, 36, Axial);
                driveToClosestPoint(24, 26, Lateral);
                //open purple
                driveToClosestPoint(20, 26, Axial);
                //close to pick up white pixel
            } else if (position == MIDDLE) {
                driveToClosestPoint(33, 36, Axial);
                //open purple
                driveToClosestPoint(24, 36, Axial);
            } else if (position == RIGHT) {
                driveToClosestPoint(24, 36, Axial);
                driveToClosestPoint(24, 42, Lateral);
                //open purple
                driveToClosestPoint(20, 42, Axial);
                //close to pick up white pixel
            }

            rotateToPoint(24, 18);
            driveToClosestPoint(24, 18, Axial);
            rotateToHeading(180);
            driveToClosestPoint(36, 18, Lateral);
            driveToClosestPoint(36, 15, Axial);
            rotateToPoint(58, 21);
            driveToClosestPoint(58, 21, Lateral);
            rotateToPoint(66, 50);
            driveToClosestPoint(66, 50, Lateral);
            rotateToHeading(0);
            driveToClosestPoint(66, 110, Lateral);
            rotateToHeading(180);
            driveToClosestPoint(42, 110, Axial);
            //drop pixel
            driveToClosestPoint(10, 110, Axial);
        }
    }

    private void driveToClosestPoint(double x, double y, DriveDirection driveDirection) {
        piBot.planDriveToClosestPoint(new Point(x, y), driveDirection);
        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }

        if (testMode)
            sleep(1000);
    }

    private void rotateToHeading(double targetHeading) {
        piBot.planRotateToHeading(targetHeading);
        while (opModeIsActive()) {
            if (piBot.runRotate() == DriveStatus.Done) break;
        }

        if (testMode)
            sleep(1000);
    }

    private void rotateToPoint(double x, double y) {
        piBot.planRotateToPoint(new Point(x, y));
        while (opModeIsActive()) {
            if ((piBot.runRotate() == DriveStatus.Done)) break;
        }

        if (testMode)
            sleep(1000);
    }
}
