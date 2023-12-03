package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Mat;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.imgproc.Imgproc;

@TeleOp(name = "TeleOpWithPropDetection", group = "TeleOp")
public class TeamPropDetectionOpMode extends OpMode {
    private OpenCvCamera webcam;
    private PropDetectionPipeline propDetectionPipeline;

    @Override
    public void init() {
        // Initialize the webcam and prop detection
        initWebcam();
    }

    @Override
    public void loop() {
        // Access information about the detected prop
        String propInfo = propDetectionPipeline.getPropInfo();

        // Your TeleOp control code here

        telemetry.addData("Prop Info", propInfo);
        telemetry.update();
    }

    private void initWebcam() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Use the name of your webcam from the hardware map
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        // Configure the webcam
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        propDetectionPipeline = new PropDetectionPipeline();
        webcam.setPipeline(propDetectionPipeline);

        // Set camera parameters as needed
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }
}

// Define PropDetectionPipeline as a separate class
class PropDetectionPipeline extends org.openftc.easyopencv.OpenCvPipeline {
    private String propInfo = "No prop detected";

    @Override
    public Mat processFrame(Mat input) {
        // Your prop detection code here

        // Example: Draw a circle at the center of the screen
        Imgproc.circle(input, new Point(input.width() / 2, input.height() / 2), 10, new Scalar(255, 0, 0), 2);

        // Update prop information
        propInfo = "Prop detected";

        return input;
    }

    public String getPropInfo() {
        return propInfo;
    }
}
