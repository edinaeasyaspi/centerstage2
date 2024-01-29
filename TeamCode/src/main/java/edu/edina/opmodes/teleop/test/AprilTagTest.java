package edu.edina.opmodes.teleop.test;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.library.util.Position;

import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class AprilTagTest extends LinearOpMode {

    private ImageProcessor imageProcessor;
    private VisionPortal.Builder visionPortalBuilder;
    private VisionPortal visionPortal;
    private Positioning aprilTagPos;

    public void runOpMode(){
        RobotHardware hw = new RobotHardware(hardwareMap);

        imageProcessor = new ImageProcessor(telemetry);

        aprilTagPos = new Positioning(hw);
        visionPortalBuilder = new VisionPortal.Builder();
        visionPortal = visionPortalBuilder
                .enableLiveView(true)
                .addProcessor(imageProcessor)
                .addProcessor(aprilTagPos.getMyAprilTagProc())
                .setCamera(hw.webcam)
                .setCameraResolution(new Size(640, 480))
                .build();

        waitForStart();

        while (opModeIsActive()) {
            Position p = aprilTagPos.readAprilTagPosition(false);

            if (p != null) {
                telemetry.addData("position", p);
            }

            telemetry.addData("heading", aprilTagPos.readImuHeading(false));
            telemetry.update();
        }
    }
}
