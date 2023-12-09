package edu.edina.opmodes.autonomous;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutoTopLeftBlueSpikemark", group = "Autonomous")
public class AutonomousByTimeTopLeftBlueSpikemark extends LinearOpMode {

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


        goForward(1.4);
        goBackward2((short) 0.1);
        turn(-TURN_SPEED, 0.5);
        goForward1(1.0);
        turn(-TURN_SPEED, 1);
        goBackward2((short) 0.35);
        extendLiftMotor((short) 2.0);
        moveLiftServosBack((short) 2.0);
        reverseIntakeServos((short) 1.2);
        swingBackLiftServos((short)2.0);
        lowerLiftMotor((short) 2.0);

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

        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    private void goForward(double duration) {
        frontRightMotor.setPower(1);
        frontLeftMotor.setPower(1);
        backRightMotor.setPower(1);
        backLeftMotor.setPower(1);
        sleep(1400);
    }
    private void goBackward2(short duration) {
        setDrivePower(-FORWARD_SPEED,-FORWARD_SPEED,-FORWARD_SPEED,-FORWARD_SPEED);
        sleep((short) (duration * 100));
    }
    private void turn(double power, double duration) {
        frontLeftMotor.setPower(-power);
        backLeftMotor.setPower(-power);
        frontRightMotor.setPower(power);
        backRightMotor.setPower(power);
        sleep((long) (duration * 500));

        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);

    }
    private void goForward1(double duration) {
        setDrivePower(FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED, FORWARD_SPEED);
        sleep((long) (duration * 700));

        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);

    };


    private void extendLiftMotor(short duration) {
        liftMotor.setPower(FORWARD_SPEED);
        sleep((short) (duration * 1500));

        stopAll();
    }

    private void moveLiftServosBack(short duration) {
        leftLiftServo.setPosition(0.9);
        rightLiftServo.setPosition(0.1);
        sleep((short) (duration * 2000));



        telemetry.addData("Left Lift Servo Position", leftLiftServo.getPosition());
        telemetry.addData("Right Lift Servo Position", rightLiftServo.getPosition());
        telemetry.update();

    }

    private void reverseIntakeServos(short duration) {
        setIntakeServoPower(1, -1);
        sleep((short) (duration * 1200));

        stopAll();
    }

    private void swingBackLiftServos(short duration) {
        leftLiftServo.setPosition(0.1);
        rightLiftServo.setPosition(0.9);
        sleep((short) (duration * 2000));
    }

    private void lowerLiftMotor(double duration) {
        liftMotor.setPower(-FORWARD_SPEED);
        sleep((short) (duration * 800));

        liftMotor.setPower(0);
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


