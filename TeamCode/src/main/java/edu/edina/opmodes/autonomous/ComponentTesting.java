package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Component Testing", group = "Autonomous")
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

    frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
    frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
    backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
    backRightMotor.setDirection(DcMotor.Direction.FORWARD);

    telemetry.addData("Status", "Ready to run");
    telemetry.update();

    waitForStart();

    // Step 1: Strafe right for 3 seconds
    // frontLeftMotor.setPower(FORWARD_SPEED);
   // frontRightMotor.setPower(-FORWARD_SPEED);
   // backLeftMotor.setPower(-FORWARD_SPEED);
   // backRightMotor.setPower(FORWARD_SPEED);
    //sleep(3000); // Sleep for 3 seconds

    //frontLeftMotor.setPower(FORWARD_SPEED);
    //frontRightMotor.setPower(FORWARD_SPEED);
   // backLeftMotor.setPower(FORWARD_SPEED);
    //backRightMotor.setPower(FORWARD_SPEED);
    //sleep(3000); // Sleep for 2 seconds

    // Step 3: Intake servo for 1 second
    //leftIntakeServo.setPower(-1);
    //rightIntakeServo.setPower(1);
    //sleep(3000); // Sleep for 1 second

    //frontLeftMotor.setPower(TURN_SPEED);
   // frontRightMotor.setPower(-TURN_SPEED);
   // backLeftMotor.setPower(TURN_SPEED);
   // backRightMotor.setPower(-TURN_SPEED);
   // sleep(1175); // Sleep for 1.5 seconds

    //final double  FORWARD_SPEED = 1.0;

    //liftMotor.setPower(FORWARD_SPEED);
    //sleep(1200); // Sleep for 3 seconds

    //leftLiftServo.setPosition(0);
    //rightLiftServo.setPosition(1.0);
    //sleep(1500); // Sleep for 1.5 seconds

    // Move leftLiftServo to position 0.0
    //leftLiftServo.setPosition(0.0);
    sleep(3000);  // Sleep for 1 second

// Move rightLiftServo to position 1.0
    //rightLiftServo.setPosition(1.0);
    sleep(3000);  // Sleep for 1 second

    //telemetry.addData("Left Servo Position", leftLiftServo.getPosition());
    //telemetry.addData("Right Servo Position", rightLiftServo.getPosition());
    //telemetry.update();



    //leftLiftServo.setPosition(0.0);  // Set to 0.0 for one direction
    //rightLiftServo.setPosition(1.0); // Set to 1.0 for the opposite direction
    //sleep(2000);


    //leftIntakeServo.setPower(1);
    //rightIntakeServo.setPower(-1);
    //sleep(4000); // Sleep for 8 seconds

    //frontLeftMotor.setPower(-FORWARD_SPEED);
    //frontRightMotor.setPower(FORWARD_SPEED);
    //backLeftMotor.setPower(FORWARD_SPEED);
    //backRightMotor.setPower(-FORWARD_SPEED);
    //sleep(1000);

}
}


