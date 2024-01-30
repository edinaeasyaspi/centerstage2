package edu.edina.opmodes.teleop.test;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import edu.edina.library.util.RobotHardware;

@TeleOp
public class TeleopOpModeAvnerTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        RobotHardware hw = new RobotHardware(hardwareMap);
        hw.frontLeftMotor.setDirection(REVERSE);
        hw.backLeftMotor.setDirection(REVERSE);
        hw.frontRightMotor.setDirection(FORWARD);
        hw.backRightMotor.setDirection(FORWARD);

        hw.frontLeftMotor.setZeroPowerBehavior(BRAKE);
        hw.backLeftMotor.setZeroPowerBehavior(BRAKE);
        hw.frontRightMotor.setZeroPowerBehavior(BRAKE);
        hw.backRightMotor.setZeroPowerBehavior(BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            double max;

            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            boolean forward = gamepad1.dpad_up;
            boolean backward = gamepad1.dpad_down;
            boolean left = gamepad1.dpad_left;
            boolean right = gamepad1.dpad_right;

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            if (forward) {
                hw.frontLeftMotor.setPower(1);
                hw.backLeftMotor.setPower(1);
                hw.frontRightMotor.setPower(1);
                hw.backRightMotor.setPower(1);
            }
            if (backward) {
                hw.frontLeftMotor.setPower(-1);
                hw.backLeftMotor.setPower(-1);
                hw.frontRightMotor.setPower(-1);
                hw.backRightMotor.setPower(-1);
            }
            if (left) {
                hw.frontLeftMotor.setPower(-1);
                hw.backLeftMotor.setPower(1);
                hw.frontRightMotor.setPower(1);
                hw.backRightMotor.setPower(-1);
            }
            if (right) {
                hw.frontLeftMotor.setPower(1);
                hw.backLeftMotor.setPower(-1);
                hw.frontRightMotor.setPower(-1);
                hw.backRightMotor.setPower(1);
            }


            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            hw.frontLeftMotor.setPower(leftFrontPower);
            hw.backLeftMotor.setPower(leftBackPower);
            hw.frontRightMotor.setPower(rightFrontPower);
            hw.backRightMotor.setPower(rightBackPower);
        }
    }
}