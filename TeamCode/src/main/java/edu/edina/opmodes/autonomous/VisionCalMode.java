package edu.edina.opmodes.autonomous;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.concurrent.TimeUnit;

import edu.edina.opmodes.teleop.test.VisionCalProcessor;

@Autonomous
public class VisionCalMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        AprilTagProcessor.Builder myAprilTagProcBuilder = new AprilTagProcessor.Builder()
                .setDrawTagID(false)
                .setDrawTagOutline(false)
                .setDrawAxes(false)
                .setDrawCubeProjection(false);

        AprilTagProcessor myAprilTagProc = myAprilTagProcBuilder.build();

        VisionCalProcessor proc = new VisionCalProcessor(myAprilTagProc);

        VisionPortal visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "LogitechC270_8034PI"))
                .setCameraResolution(new Size(640, 480))
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(myAprilTagProc)
                .addProcessor(proc)
                .build();

        WhiteBalanceControl wb = null;
        ExposureControl xc = null;

        while (opModeInInit()) {
            if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
                if (wb == null) {
                    wb = visionPortal.getCameraControl(WhiteBalanceControl.class);
                    wb.setMode(WhiteBalanceControl.Mode.AUTO);
                }

                if (xc == null) {
                    xc = visionPortal.getCameraControl(ExposureControl.class);
                    xc.setMode(ExposureControl.Mode.Auto);
                }
            }

            if (wb != null) {
                telemetry.addData("white balance", wb.getWhiteBalanceTemperature());
            }

            if (xc != null) {
                telemetry.addData("exposure", "%dms, supported? %d-%dms, %d auto? %s, manual? %s",
                        xc.getExposure(TimeUnit.MILLISECONDS),
                        xc.isExposureSupported(),
                        xc.getMinExposure(TimeUnit.MILLISECONDS),
                        xc.getMaxExposure(TimeUnit.MILLISECONDS),
                        xc.isModeSupported(ExposureControl.Mode.Auto),
                        xc.isModeSupported(ExposureControl.Mode.Manual));
            }

            String s = proc.getDiagString();
            if (s != null) {
                telemetry.addData("processor", s);
            }

            telemetry.update();
        }

        while (opModeIsActive()) {
            sleep(1);
        }
    }
}
