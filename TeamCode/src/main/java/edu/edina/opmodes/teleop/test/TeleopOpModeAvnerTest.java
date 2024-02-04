package edu.edina.opmodes.teleop.test;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.PiBot;
import edu.edina.library.util.RobotHardware;

@TeleOp
public class TeleopOpModeAvnerTest extends LinearOpMode {

    private PiBot piBot;

    @Override
    public void runOpMode() {
        RobotHardware hw = new RobotHardware(hardwareMap);
        piBot = new PiBot(hw);

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

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (gamepad1.dpad_up)
                hw.liftMotor.setPower(0.5);
            else if (gamepad1.dpad_down)
                hw.liftMotor.setPower(-0.4);
            else
                hw.liftMotor.setPower(0);

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            if (gamepad1.b) {
                piBot.grab(GrabberSide.Both);
            }
            if (gamepad1.x) {
                piBot.grab(GrabberSide.Both);
            }
            if (gamepad1.y) {
                piBot.drop(GrabberSide.Both);
            }
            if (gamepad1.a) {
                piBot.drop(GrabberSide.Both);
            }

            hw.frontLeftMotor.setPower(leftFrontPower);
            hw.backLeftMotor.setPower(leftBackPower);
            hw.frontRightMotor.setPower(rightFrontPower);
            hw.backRightMotor.setPower(rightBackPower);

            telemetry.addData("pos intake left", hw.intakeLeft.getPosition());
            telemetry.update();
        }
    }
}