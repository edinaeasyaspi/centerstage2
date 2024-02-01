package edu.edina.opmodes.teleop.test;

import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.LEFT;

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
    private PiBot piBot;

    private RobotHardware hw;

    private Selected position;

    public void runOpMode() {
        hw = new RobotHardware(hardwareMap);
        piBot = new PiBot(hw);

        ImageProcessor imageProcessor = new ImageProcessor(telemetry);

        VisionPortal.Builder visionPortalBuilder = new VisionPortal.Builder();
        VisionPortal visionPortal = visionPortalBuilder.enableLiveView(true).addProcessor(imageProcessor).setCamera(hardwareMap.get(WebcamName.class, "LogitechC270_8034PI")).setCameraResolution(new Size(640, 480)).build();

        while (opModeInInit()) {
//            if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
//                visionPortal
//                        .getCameraControl(WhiteBalanceControl.class)
//                        .setMode(WhiteBalanceControl.Mode.AUTO);
//            }

            Selected selectedSpike = imageProcessor.getSelection();
            telemetry.addData("Selected value", selectedSpike);
            telemetry.addData("diag", imageProcessor.getDiagString());
            telemetry.update();
        }

        Positioning posn = new Positioning(new RobotHardware(hardwareMap));
        posn.setCurrPos(new Position(0, 36, -90));

        waitForStart();

        while (opModeIsActive()) {
//            if (position == LEFT) {
            telemetry.addData("position", posn.getCurrPos());
            telemetry.update();
            sleep(2000);
            driveToClosestPoint(new Point(36, 36), DriveDirection.Axial);
            driveToClosestPoint(new Point(36, 30), DriveDirection.Lateral);
            //open purple
            driveToClosestPoint(new Point(30, 30), DriveDirection.Axial);
            rotateToHeading(180);
            driveToClosestPoint(new Point(30, 5), DriveDirection.Axial);
            //close to pick up white pixel
            //}
            break;
        }
    }

    private void driveToClosestPoint(Point point, DriveDirection driveDirection) {
        piBot.planDriveToClosestPoint(point, driveDirection);
        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }
    }

    private void rotateToHeading(double targetHeading) {
        piBot.planRotateToHeading(targetHeading);
        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }
    }

    private void rotateToPoint(Point point) {
        piBot.planRotateToPoint(point);
        while (opModeIsActive()) {
            if ((piBot.runRotate() == DriveStatus.Done)) break;
        }
    }
}
