package edu.edina.opmodes.autonomous;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.library.util.projection.Vec;
import edu.edina.opmodes.teleop.test.PixelDetectProcessor;

@Autonomous
public class PixelDetectTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        PixelDetectProcessor proc = new PixelDetectProcessor(new Vec[]{
                new Vec(-0.1677, -.4518, 2.8624),
                new Vec(.8298, -.4965, 2.8070),
                new Vec(.8922, -.3197, 3.7893),
                new Vec(-.1053, -.2750, 3.8446),
        });

        new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "LogitechC270_8034PI"))
                .setCameraResolution(new Size(640, 480))
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(proc)
                .build();

        while (opModeInInit()) {
            if (proc.getDiagString() != null) {
                telemetry.addData("diag", proc.getDiagString());
                telemetry.update();
            }
        }

        while (opModeIsActive()) {
            sleep(1);
        }
    }
}
