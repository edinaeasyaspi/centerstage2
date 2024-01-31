package edu.edina.opmodes.teleop.test;

import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.LEFT;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.Point;
import edu.edina.library.util.Position;
import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.DriveDirection;

@Autonomous
public class AutoTestBlueFront extends LinearOpMode {
    private PiBot piBot;

    private Selected position;

    public void runOpMode() {
        ImageProcessor imageProcessor = new ImageProcessor(telemetry);

        VisionPortal.Builder visionPortalBuilder = new VisionPortal.Builder();
        VisionPortal visionPortal = visionPortalBuilder
                .enableLiveView(true)
                .addProcessor(imageProcessor)
                .setCamera(hardwareMap.get(WebcamName.class, "LogitechC270_8034PI"))
                .setCameraResolution(new Size(640, 480))
                .build();

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

        waitForStart();

        while (opModeIsActive()) {
            if (position == LEFT) {
                Positioning posn = new Positioning(new RobotHardware(hardwareMap));
                Position startPos = posn.getCurrPos();
                piBot.planDriveToClosestPoint(new Point (startPos.x + 36, startPos.y), DriveDirection.Lateral);
            }
        }
    }
}
