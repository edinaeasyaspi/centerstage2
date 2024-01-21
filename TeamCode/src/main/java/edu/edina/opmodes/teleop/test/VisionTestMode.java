package edu.edina.opmodes.teleop.test;


import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;


@Autonomous

public class VisionTestMode extends LinearOpMode {
    @Override
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
            if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
                visionPortal
                        .getCameraControl(WhiteBalanceControl.class)
                        .setMode(WhiteBalanceControl.Mode.AUTO);
            }

            ImageProcessor.Selected selectedSpike = imageProcessor.getSelection();
            telemetry.addData("Selected value", selectedSpike);
            telemetry.addData("diag", imageProcessor.getDiagString());
            telemetry.update();
        }

        waitForStart();

        while (opModeIsActive()) {
            sleep(1);
        }
    }
}
