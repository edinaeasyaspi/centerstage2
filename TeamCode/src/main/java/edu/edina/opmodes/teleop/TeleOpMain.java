package edu.edina.opmodes.teleop;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.PiBot;
import edu.edina.library.util.RobotHardware;

@TeleOp(name = "TeleOpMain 🥧")
public class TeleOpMain extends LinearOpMode {
    protected int endGameTime;

    public TeleOpMain() {
        endGameTime = 90;
    }

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
        hw.liftMotor.setZeroPowerBehavior(BRAKE);

        PwmControl[] otherServos = new PwmControl[]{
                ((PwmControl) hw.intakeSwingLeft),
                ((PwmControl) hw.intakeSwingRight),
                ((PwmControl) hw.intakeLeft),
                ((PwmControl) hw.intakeRight),
                ((PwmControl) hw.hangLeft),
                ((PwmControl) hw.hangRight)
        };

//        hw.hangRight.setPosition(Servo.MIN_POSITION + 0.05);
//        hw.hangLeft.setPosition(Servo.MAX_POSITION - 0.05);
//        hw.hangLiftLeft.setPosition(0.2);
//        hw.hangLiftRight.setPosition(0.8);

        waitForStart();
        hw.hangRight.setPosition(Servo.MIN_POSITION + 0.05);
        hw.hangLeft.setPosition(Servo.MAX_POSITION - 0.05);
        ElapsedTime startTime = new ElapsedTime();

        double noLift = hw.liftMotor.getCurrentPosition();

        double powerLimit = 1;
        double liftPosition = noLift;

        ElapsedTime dropTimer = null;
        boolean invertSteering = false;

        while (opModeIsActive()) {
            double max;

            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            if (invertSteering) {
                lateral = -lateral;
//                yaw = -yaw;
            }

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            int currPos = hw.liftMotor.getCurrentPosition();
            if (gamepad2.dpad_up && currPos < noLift + 1500) {
                hw.liftMotor.setPower(0.7);
                liftPosition = currPos;
            } else if (gamepad2.dpad_down && currPos > noLift) {
                hw.liftMotor.setPower(-0.6);
                liftPosition = currPos;
            } else {
                double y = currPos - liftPosition;
                hw.liftMotor.setPower(-y / 100);
            }

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            if (gamepad1.right_bumper) {
                piBot.grab(GrabberSide.Both);
            }
            if (gamepad1.left_bumper) {
                piBot.drop(GrabberSide.Both);
            }
            if (gamepad1.left_bumper && gamepad1.b) {
                piBot.grab(GrabberSide.Left);
            }
            if (gamepad1.right_bumper && gamepad1.b) {
                piBot.grab(GrabberSide.Right);
            }


            if (gamepad1.a)
                powerLimit = 1.0;
            else
                powerLimit = 0.5;

            hw.frontLeftMotor.setPower(leftFrontPower * powerLimit);
            hw.backLeftMotor.setPower(leftBackPower * powerLimit);
            hw.frontRightMotor.setPower(rightFrontPower * powerLimit);
            hw.backRightMotor.setPower(rightBackPower * powerLimit);

            if (gamepad2.left_trigger > 0.8) {
                piBot.positionGrabber(0.9, false);
                invertSteering = true;
            }

            if (gamepad2.right_trigger > 0.8) {
                piBot.positionGrabber(0, true);
                piBot.grab(GrabberSide.Both);
                invertSteering = false;
            }

            piBot.runGrabber();

            if (startTime.seconds() > endGameTime) {
                if (gamepad2.x && gamepad2.y && gamepad2.dpad_left) {
                    hw.droneLauncher.setPower(-1);
//                } else {
//                    hw.droneLauncher.setPower(0);
                }
            }

            if (startTime.seconds() > endGameTime) {
                if (gamepad2.a) {
                    for (PwmControl c : otherServos) {
                        c.setPwmDisable();
                    }

                    hw.hangLiftLeft.setPosition(0.1);
                    hw.hangLiftRight.setPosition(0.9);
                    sleep(200);

                    for (PwmControl c : otherServos) {
                        c.setPwmEnable();
                    }

                    hw.hangRight.setPosition(Servo.MAX_POSITION - 0.05);
                    hw.hangLeft.setPosition(Servo.MIN_POSITION + 0.05);
                }

                if (gamepad2.y) {
                    hw.hangRight.setPosition(Servo.MIN_POSITION + 0.05);
                    hw.hangLeft.setPosition(Servo.MAX_POSITION - 0.05);
                }
            }

            telemetry.addData("timer", startTime.seconds());
            telemetry.update();
        }
    }
}
