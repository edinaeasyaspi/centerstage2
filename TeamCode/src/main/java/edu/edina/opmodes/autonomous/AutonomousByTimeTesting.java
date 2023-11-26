package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Robot: Autonomous Drive by time", group = "Autonomous")
public class AutonomousByTimeTesting extends LinearOpMode {

    private DcMotor frontLeftMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backRightMotor = null;
    private DcMotor liftMotor = null;
    private Servo leftLiftServo = null;
    private Servo rightLiftServo = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;

    private ElapsedTime runtime = new ElapsedTime();

    // Constants for tuning (calibrate based on your robot)
    static final double FORWARD_SPEED = 1.0;
    static final double TURN_SPEED = 0.3;
    static final int COUNTS_PER_DEGREE = 10; // Adjust this based on your robot's characteristics

    @Override
    public void runOpMode() {
        initializeHardware();
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



        waitForStart();

        strafeRight(3.0);
        driveForward(2.0);
        intakeServoForward(1.0);
        turnLeft(180.0); // Turning 180 degrees using encoder counts
        liftMotorUp(3.0);
        liftServosUp(1.5);
        intakeServoReverse(8.0);
        strafeLeft(1.0);

        stopAll();

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

    private void initializeHardware() {
        // Your existing hardware initialization code remains unchanged
        // ...

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    private void strafeRight(double duration) {
        setDrivePower(FORWARD_SPEED, -FORWARD_SPEED, -FORWARD_SPEED, FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void driveForward(double duration) {
        setDrivePower(FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void intakeServoForward(double duration) {
        setIntakeServoPower(1, 1);
        sleep((long) (duration * 1000));
    }

    private void turnLeft(double targetDegrees) {
        int targetCounts = (int) (targetDegrees * COUNTS_PER_DEGREE);
        resetDriveEncoders();

        while (opModeIsActive() && Math.abs(frontLeftMotor.getCurrentPosition()) < targetCounts) {
            setDrivePower(-TURN_SPEED, TURN_SPEED, -TURN_SPEED, TURN_SPEED);
        }

        stopAll();
    }

    private void liftMotorUp(double duration) {
        liftMotor.setPower(FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void liftServosUp(double duration) {
        leftLiftServo.setPosition(1.0);
        rightLiftServo.setPosition(1.0);
        sleep((long) (duration * 1000));
    }

    private void intakeServoReverse(double duration) {
        setIntakeServoPower(-1, -1);
        sleep((long) (duration * 1000));
    }

    private void strafeLeft(double duration) {
        setDrivePower(-FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED, -FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void stopAll() {
        setDrivePower(0, 0, 0, 0);
        liftMotor.setPower(0);
        setIntakeServoPower(0, 0);
        leftLiftServo.setPosition(0);
        rightLiftServo.setPosition(0);
    }

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

    private void resetDriveEncoders() {
        // Reset encoder positions
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set the mode to run with encoders
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
