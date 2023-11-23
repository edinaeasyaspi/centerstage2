package edu.edina.opmodes.autonomous;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



@Autonomous(name="Robot: Autonomous Drive by time", group="Autonomous")

public class AutonomousByTimeTesting extends LinearOpMode {


    private DcMotor       frontLeftMotor  = null;
    private DcMotor       backLeftMotor  = null;

    private DcMotor       frontRightMotor = null;

    private DcMotor        backRightMotor = null;
    private DcMotor     liftMotor = null;
    private Servo   leftLiftServo = null;
    private Servo   rightLiftServo = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;

    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 1.0;
    static final double     TURN_SPEED    = 0.3;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        leftLiftServo = hardwareMap.get(Servo.class, "S1");
        rightLiftServo = hardwareMap.get(Servo.class, "S2");
        leftIntakeServo = hardwareMap.get(CRServo.class, "F2");
        rightIntakeServo = hardwareMap.get(CRServo.class, "F1");


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Step 1:  Strafe right for 3 seconds
        frontLeftMotor.setPower(FORWARD_SPEED);
        frontRightMotor.setPower(-FORWARD_SPEED);
        backLeftMotor.setPower(-FORWARD_SPEED);
        backRightMotor.setPower(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }


        // Step 3:  Drive forward for 1 Second
        frontLeftMotor.setPower(FORWARD_SPEED);
        frontRightMotor.setPower(FORWARD_SPEED);
        backLeftMotor.setPower(FORWARD_SPEED);
        backRightMotor.setPower(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.0)) {
            telemetry.addData("Path", "Leg 2: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();


        }

        leftIntakeServo.setPower(1);
        rightIntakeServo.setPower(1);
        while (opModeIsActive() && (runtime.seconds() < 1)) {
            telemetry.addData("IntakeServo", "Leg 3: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 1: Turn 180 degrees
        double turnStartTime = runtime.seconds();
        double turnDuration = 1.5; // Adjust this value based on testing

        frontLeftMotor.setPower(TURN_SPEED);
        frontRightMotor.setPower(-TURN_SPEED);
        backLeftMotor.setPower(TURN_SPEED);
        backRightMotor.setPower(-TURN_SPEED);

        while (opModeIsActive() && (runtime.seconds() - turnStartTime < turnDuration)) {
            telemetry.addData("Path", "Turning: %4.1f S Elapsed", runtime.seconds() - turnStartTime);
            telemetry.update();
            // Add a slight delay to give the motors time to act
            sleep(50);
        }

// Stop the motors after turning
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);




    

        liftMotor.setPower(FORWARD_SPEED);
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Lift", "Leg 5: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();

        }


        leftLiftServo.setPosition(1.0);
        rightLiftServo.setPosition(1.0);
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            telemetry.addData("Servo Swing", "Leg 6: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();

        }


        leftIntakeServo.setPower(-1);
        rightIntakeServo.setPower(-1);
        while (opModeIsActive() && (runtime.seconds() < 8)) {
            telemetry.addData("IntakeServo", "Leg 7: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        frontLeftMotor.setPower(-FORWARD_SPEED);
        frontRightMotor.setPower(FORWARD_SPEED);
        backLeftMotor.setPower(FORWARD_SPEED);
        backRightMotor.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 8: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        frontLeftMotor.setPower(-FORWARD_SPEED);
        frontRightMotor.setPower(-FORWARD_SPEED);
        backLeftMotor.setPower(-FORWARD_SPEED);
        backRightMotor.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 9: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 4:  Stop
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
