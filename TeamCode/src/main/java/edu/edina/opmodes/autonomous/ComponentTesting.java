package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Robot: Autonomous Drive by time", group = "Autonomous")
public class ComponentTesting extends LinearOpMode {

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

    static final double FORWARD_SPEED = 1.0;
    static final double TURN_SPEED = 1.0;

    @Override
    public void runOpMode() {
        initializeHardware();

        waitForStart();

        strafeRight(3.0);
        driveForward(2.0);
        intakeServoForward(1.0);
        strafeLeft(1.0);
        turn(-TURN_SPEED, 1.0);
        extendLiftMotor(3.0);  // Placeholder, replace with actual method
        moveLiftServosBack(1.5);  // Placeholder, replace with actual method
        reverseIntakeServos(8.0);
        swingBackLiftServos(1.5);  // Placeholder, replace with actual method
        lowerLiftMotor(3.0);  // Placeholder, replace with actual method
        strafeLeft(1.0);
        goBackward(2.0);  // Placeholder, replace with actual method

        stopAll();

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

    private void strafeLeft(double duration) {
        setDrivePower(-FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED, -FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void turn(double power, double duration) {
        frontLeftMotor.setPower(power);
        backLeftMotor.setPower(power);
        frontRightMotor.setPower(-power);
        backRightMotor.setPower(-power);
        sleep((long) (duration * 1100));
        stopAll();
    }

    private void extendLiftMotor(double duration) {
        liftMotor.setPower(FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }

    private void moveLiftServosBack(double duration) {
        leftLiftServo.setPosition(1.0);
        rightLiftServo.setPosition(0.0);
        sleep((long) (duration * 1000));
    }

    private void reverseIntakeServos(double duration) {
        setIntakeServoPower(1, -1);
        sleep((long) (duration * 1000));

        stopAll();
    }

    private void swingBackLiftServos(double duration) {
        leftLiftServo.setPosition(1.0);
        rightLiftServo.setPosition(0.0);
        sleep((long) (duration * 500));
    }

    private void lowerLiftMotor(double duration) {
        liftMotor.setPower(-FORWARD_SPEED);
        sleep((long) (duration * 500));
    }

    private void goBackward(double duration) {
        setDrivePower(-FORWARD_SPEED, -FORWARD_SPEED, -FORWARD_SPEED, -FORWARD_SPEED);
        sleep((long) (duration * 1000));
    }


    private void stopAll() {
        setDrivePower(0, 0, 0, 0);
        liftMotor.setPower(0);
        setIntakeServoPower(0, 0);
        leftLiftServo.setPosition(1);
        rightLiftServo.setPosition(1);
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
}
