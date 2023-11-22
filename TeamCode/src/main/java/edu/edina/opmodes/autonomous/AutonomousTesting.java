package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@Autonomous(name = "PixelDrop Autonomous", group = "Autonomous")
public class AutonomousTesting extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    @Override
    public void runOpMode() {
        // Initialize motors
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        // Reverse the right motors to ensure both sides move in the same direction
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start button to be pressed
        waitForStart();

        // Move forward for 1 second to simulate dropping on the backboard
        moveForward(1);

        // Turn right for 1 second to simulate moving to the spikemark
        turnRight(1);

        // Stop the robot
        stopRobot();
    }

    private void moveForward(double seconds) {
        // Calculate the target time
        long targetTime = System.currentTimeMillis() + (long) (seconds * 1000);

        // Move forward until the target time is reached
        while (System.currentTimeMillis() < targetTime && opModeIsActive()) {
            frontLeftMotor.setPower(0.5);
            frontRightMotor.setPower(0.5);
            backLeftMotor.setPower(0.5);
            backRightMotor.setPower(0.5);
        }
    }

    private void turnRight(double seconds) {
        // Calculate the target time
        long targetTime = System.currentTimeMillis() + (long) (seconds * 1000);

        // Turn right until the target time is reached
        while (System.currentTimeMillis() < targetTime && opModeIsActive()) {
            frontLeftMotor.setPower(0.5);
            frontRightMotor.setPower(-0.5);
            backLeftMotor.setPower(0.5);
            backRightMotor.setPower(-0.5);
        }
    }

    private void stopRobot() {
        // Stop all motors
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
    }
}
