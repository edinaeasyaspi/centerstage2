package edu.edina.opmodes.teleop.test;

import com.acmerobotics.roadrunner.Pose2d;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

import edu.edina.library.util.Position;
import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;

import android.content.Context;
import android.util.Size;


@Disabled
@Autonomous(name = "auto")
public class PropDetectionThroughWebcam extends OpMode {
    MecanumDrive drive;
    private PropDetectingVisionProcessor imageProcessor;
    private VisionPortal.Builder visionPortalBuilder;
    private VisionPortal visionPortal;
    private PropDetectingVisionProcessor.Selected selectedSpike;
    private Telemetry.Item teleSelected;
    private Pose2d startPose;
    private Context context;
    private boolean readyToStart;
    private GamepadEx gamepad;
    private int delaySeconds;
    private ElapsedTime runTime;
    AutonomousConfiguration autonomousConfiguration = new AutonomousConfiguration();
    private Positioning aprilTagPos;


    public void init() {
        RobotHardware hw = new RobotHardware(hardwareMap);
        gamepad = new GamepadEx(gamepad1);
        imageProcessor = new PropDetectingVisionProcessor();
        aprilTagPos = new Positioning(hw,true);
        visionPortalBuilder = new VisionPortal.Builder();
        visionPortal = visionPortalBuilder
                .enableLiveView(true)
                .addProcessor((VisionProcessor) imageProcessor)
                .addProcessor(aprilTagPos.getMyAprilTagProc())
                .setCamera(hardwareMap.get(WebcamName.class, "LogitechC270_8034PI"))
                .setCameraResolution(new Size(640, 480))
                .build();

        runTime = new ElapsedTime();
        teleSelected = telemetry.addData("Selected", teleSelected);
        autonomousConfiguration.init(gamepad, this.telemetry, hardwareMap.appContext);
    }


    public void init_loop() {
        // Wait for the camera to be open
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            return;
        }

        // Use auto white balance
        if (visionPortal.getCameraControl(WhiteBalanceControl.class).getMode() != WhiteBalanceControl.Mode.AUTO) {
            visionPortal.getCameraControl(WhiteBalanceControl.class).setMode(WhiteBalanceControl.Mode.AUTO);
        }

        // Keep checking the camera
        selectedSpike = imageProcessor.getSelection();
        teleSelected.setValue(selectedSpike);
        telemetry.addData("Selected value", selectedSpike);
        // Get the menu options
        autonomousConfiguration.init_loop();
    }

    public void start() {
        // Make sure the required menu options are set.
        if (!autonomousConfiguration.getReadyToStart()) {
            telemetry.addLine("Alert - Not ready to start!");
            //  telemetry.speak("Not ready to start!");
            runTime.reset();
            visionPortal.stopStreaming();
            while (runTime.seconds() < 2) {
            }
            requestOpModeStop();
        }

        // Save resources
        visionPortal.stopStreaming();
        // Get drive started
        drive = new MecanumDrive(hardwareMap, getDriveStartPose());
        delaySeconds = autonomousConfiguration.getDelayStartSeconds();
        runTime.reset();
    }

    private Pose2d getDriveStartPose() {
        AutonomousOptions.StartPosition startPosition = autonomousConfiguration.getStartPosition();
        double startPositionX;
        double startPositionY;
        double startHeading;
        if (autonomousConfiguration.getAlliance() == AutonomousOptions.AllianceColor.Red) {
            startHeading = Math.toRadians(90);
            startPositionY = -60f;
            if (startPosition == AutonomousOptions.StartPosition.Left) {
                startPositionX = -36f;
            } else {
                startPositionX = 12f;
            }
        } else {
            // Blue Alliance
            startHeading = Math.toRadians(-90);
            startPositionY = 60f;
            if (startPosition == AutonomousOptions.StartPosition.Left) {
                startPositionX = 12f;
            } else {
                startPositionX = -36f;
            }
        }

        return new Pose2d(startPositionX, startPositionY, startHeading);
    }


    @Override
    public void loop() {
        Position p = aprilTagPos.readAprilTagPosition(true);
        if (p != null) {
            telemetry.addData("position", p);
            telemetry.update();
        }
    }
}
