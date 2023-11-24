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

    static final double FORWARD_SPEED = 1.0;
    static final double TURN_SPEED = 0.3;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
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

        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        waitForStart();

        // Step 1: Strafe right for 3 seconds
        frontLeftMotor.setPower(FORWARD_SPEED);
        frontRightMotor.setPower(-FORWARD_SPEED);
        backLeftMotor.setPower(-FORWARD_SPEED);
        backRightMotor.setPower(FORWARD_SPEED);
        sleep(3000); // Sleep for 3 seconds

        // Step 2: Drive forward for 2 seconds
        frontLeftMotor.setPower(FORWARD_SPEED);
        frontRightMotor.setPower(FORWARD_SPEED);
        backLeftMotor.setPower(FORWARD_SPEED);
        backRightMotor.setPower(FORWARD_SPEED);
        sleep(2000); // Sleep for 2 seconds

        // Step 3: Intake servo for 1 second
        leftIntakeServo.setPower(1);
        rightIntakeServo.setPower(1);
        sleep(1000); // Sleep for 1 second

        // Step 4: Turn 180 degrees for 1.5 seconds
        frontLeftMotor.setPower(TURN_SPEED);
        frontRightMotor.setPower(-TURN_SPEED);
        backLeftMotor.setPower(TURN_SPEED);
        backRightMotor.setPower(-TURN_SPEED);
        sleep(1500); // Sleep for 1.5 seconds

        // Step 5: Lift motor for 3 seconds
        liftMotor.setPower(FORWARD_SPEED);
        sleep(3000); // Sleep for 3 seconds

        // Step 6: Lift servos for 1.5 seconds
        leftLiftServo.setPosition(1.0);
        rightLiftServo.setPosition(1.0);
        sleep(1500); // Sleep for 1.5 seconds

        // Step 7: Intake servo reverse for 8 seconds
        leftIntakeServo.setPower(-1);
        rightIntakeServo.setPower(-1);
        sleep(8000); // Sleep for 8 seconds

        // Step 8: Strafe left for 1 second
        frontLeftMotor.setPower(-FORWARD_SPEED);
        frontRightMotor.setPower(FORWARD_SPEED);
        backLeftMotor.setPower(FORWARD_SPEED);
        backRightMotor.setPower(-FORWARD_SPEED);
        sleep(1000); // Sleep for 1 second

        // Step 9: Stop all motors
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        liftMotor.setPower(0);
        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
        leftLiftServo.setPosition(0);
        rightLiftServo.setPosition(0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
