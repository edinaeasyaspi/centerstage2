package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;

@Autonomous(name = "Robot: Autonomous Drive by time1", group = "Autonomous")
public class AutonomousByTimeBottomLeftBlue extends LinearOpMode {

    private DcMotor frontLeftMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backRightMotor = null;
    private DcMotor liftMotor = null;
    private Servo leftLiftServo = null;
    private Servo rightLiftServo = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;

    private OpenCvCamera webcam;
    private YourImageProcessingPipeline imageProcessingPipeline;

    private ElapsedTime runtime = new ElapsedTime();

    static final double FORWARD_SPEED = 1.0;
    static final double TURN_SPEED = 1.0;

    @Override
    public void runOpMode() {
        initializeHardware();
        initializeCamera();

        waitForStart();



        while (opModeIsActive()) {

            Mat processedFrame = imageProcessingPipeline.getResultMat();



            telemetry.addData("Autonomous Status", "Running");
            telemetry.update();
        }

        stop();

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

    private void initializeHardware() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        leftLiftServo = hardwareMap.get(Servo.class, "S1");
        rightLiftServo = hardwareMap.get(Servo.class, "S2");
        leftIntakeServo = hardwareMap.get(CRServo.class, "F2");
        rightIntakeServo = hardwareMap.get(CRServo.class, "F1");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    private void initializeCamera() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "webcamName"), cameraMonitorViewId);
        webcam.openCameraDevice();


        imageProcessingPipeline = new YourImageProcessingPipeline();
        webcam.setPipeline(imageProcessingPipeline);

        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    public static class YourImageProcessingPipeline extends OpenCvPipeline {
        private Mat resultMat;

        @Override
        public Mat processFrame(Mat input) {

            resultMat = input.clone();

            return resultMat;
        }

        public Mat getResultMat() {
            return resultMat;
        }
    }



    private void strafeRight(double duration) {
        setDrivePower(FORWARD_SPEED, -FORWARD_SPEED, -FORWARD_SPEED, FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void driveForward(double duration) {
        setDrivePower(FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED);
        sleep((long) (duration * 2000));
    }

    private void intakeServoForward(double duration) {
        setIntakeServoPower(1, 1);
        sleep((long) (duration * 1000));
    }

    private void strafeLeft(double duration) {
        setDrivePower(-FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED, -FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void turn(double power, double duration) {
        frontLeftMotor.setPower(power);
        backLeftMotor.setPower(power);
        frontRightMotor.setPower(-power);
        backRightMotor.setPower(-power);
        sleep((long) (duration * 1350));

        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);

    }

    // ... (rest of the existing code)

    private void setDrivePower(double frontLeft, double frontRight, double backLeft, double backRight) {
        frontLeftMotor.setPower(frontLeft);
        frontRightMotor.setPower(frontRight);
        backLeftMotor.setPower(backLeft);
        backRightMotor.setPower(backRight);
    }

    private void setIntakeServoPower(double leftPower, double rightPower) {
        leftIntakeServo.setPower(leftPower);
        rightIntakeServo.setPower(rightPower);
    }

}
