package edu.edina.opmodes.autonomous;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.opmodes.teleop.test.BackProjectionVisionProcessor;
import edu.edina.opmodes.teleop.test.PropDetectingVisionProcessor;
import edu.edina.opmodes.teleop.test.WhiteBalance;

@Autonomous
public class ProjectionTestMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        BackProjectionVisionProcessor proc = new BackProjectionVisionProcessor();

        VisionPortal visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "LogitechC270_8034PI"))
                .setCameraResolution(new Size(640, 480))
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(proc)
                .build();

        WhiteBalanceControl wb = null;

        while (opModeInInit()) {
            if (wb == null && visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
                wb = visionPortal.getCameraControl(WhiteBalanceControl.class);
                wb.setMode(WhiteBalanceControl.Mode.AUTO);
            }

            if (wb != null) {
                telemetry.addData("white balance", wb.getWhiteBalanceTemperature());
            }

            String s = proc.getDiagString();
            if (s != null) {
                telemetry.addData("project", s);
            }

            telemetry.update();
        }

        while (opModeIsActive()) {
            sleep(1);
        }
    }
}
