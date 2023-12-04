package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;
import org.opencv.core.Mat;

public class TeleopOpMode extends OpMode {
    protected RobotHardware robotHardware;
    protected SmartGamepad driver1Gamepad;
    protected SmartGamepad driver2Gamepad;
    private OpenCvCamera webcam;
    private YourImageProcessingPipeline imageProcessingPipeline;

    @Override
    public void init() {
        driver1Gamepad = new SmartGamepad(gamepad1);
        driver2Gamepad = new SmartGamepad(gamepad2);


        // Initialize webcam
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(robotHardware.LogitechC270_8034PI, cameraMonitorViewId);
        webcam.openCameraDevice();

        // Set up image processing pipeline
        imageProcessingPipeline = new YourImageProcessingPipeline(); // Implement this class
        webcam.setPipeline(imageProcessingPipeline);

        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    @Override
    public void init_loop() {
        // Initialization loop logic
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        driver1Gamepad.update();
        driver2Gamepad.update();

        // Access the result of image processing through the pipeline
        Mat processedFrame = imageProcessingPipeline.getResultMat();

        // Perform actions based on processed frames
        // ...

        // Display information on the driver station telemetry
        telemetry.addData("Some Information", "...");

        // For example, display the average color value from the processed frame
        double averageColorValue = calculateAverageColor(processedFrame);
        telemetry.addData("Average Color Value", averageColorValue);

        // Update telemetry
        telemetry.update();
    }

    @Override
    public void stop() {
        // Actions to perform when OpMode stops
        webcam.stopStreaming();
    }

    // Implement your image processing pipeline
    public static class YourImageProcessingPipeline extends OpenCvPipeline {
        private Mat resultMat;

        @Override
        public Mat processFrame(Mat input) {
            // Implement your image processing here
            // Modify 'input' as needed
            resultMat = input.clone(); // Example: simply clone the input

            return resultMat;
        }

        public Mat getResultMat() {
            return resultMat;
        }
    }

    // Example method to calculate the average color value from a frame
    private double calculateAverageColor(Mat frame) {
        // Implement your logic to calculate average color value
        // This is just a placeholder, replace it with your actual logic
        return 0.0;
    }
}
