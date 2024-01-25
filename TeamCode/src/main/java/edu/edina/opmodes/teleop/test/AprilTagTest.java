package edu.edina.opmodes.teleop.test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.util.Size;

import com.qualcomm.hardware.bosch.BNO055IMUNew;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

import edu.edina.library.util.Position;

import edu.edina.library.util.Parts;

@Autonomous
public class AprilTagTest extends LinearOpMode {

    private ImageProcessor imageProcessor;
    private VisionPortal.Builder visionPortalBuilder;
    private VisionPortal visionPortal;
    private Positioning aprilTagPos;

    public void runOpMode(){
        Parts parts = new Parts(hardwareMap);

        imageProcessor = new ImageProcessor(telemetry);

        aprilTagPos = new Positioning(parts.imu, telemetry);
        visionPortalBuilder = new VisionPortal.Builder();
        visionPortal = visionPortalBuilder
                .enableLiveView(true)
                .addProcessor(imageProcessor)
                .addProcessor(aprilTagPos.getMyAprilTagProc())
                .setCamera(parts.webcam)
                .setCameraResolution(new Size(640, 480))
                .build();

        waitForStart();

        while (opModeIsActive()) {
            aprilTagPos.getPosition();

            Position p = aprilTagPos.getPosition();

            if (p != null) {
                telemetry.addData("position", p);
            }

            telemetry.addData("heading", aprilTagPos.getHeading());
            telemetry.update();
        }
    }
}
