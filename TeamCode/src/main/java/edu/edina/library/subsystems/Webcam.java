package edu.edina.library.subsystems;

import org.openftc.easyopencv.OpenCvCamera;

import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;

public class Webcam extends Subsystem{
    private Robot robot;
    private OpenCvCamera webcam;
    public Webcam(Robot robot) {
        this.robot = robot;
    }
    private YourImageProcessingPipeline imageProcessingPipeline;

    @Override
    public void init() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

       // int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardware.webcam, hardware.cameraMonitorViewId);
//        webcam.openCameraDevice();
//
//        // Set up image processing pipeline
//        imageProcessingPipeline = new YourImageProcessingPipeline(); // Implement this class
//        webcam.setPipeline(imageProcessingPipeline);
//
//        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    @Override
    public void start() {
        Mat processedFrame = imageProcessingPipeline.getResultMat();
    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (robot.Started) {
            // Access the result of image processing through the pipeline
            Mat processedFrame = imageProcessingPipeline.getResultMat();

        }
    }


    // Implement your image processing pipeline
    public static class YourImageProcessingPipeline extends OpenCvPipeline {
        Mat resultMat;

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
}
