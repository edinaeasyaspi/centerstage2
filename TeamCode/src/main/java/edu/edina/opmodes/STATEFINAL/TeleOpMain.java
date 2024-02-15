package edu.edina.opmodes.STATEFINAL;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.edina.library.util.DriveStatus;
import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.PiBot;
import edu.edina.library.util.PixelDetect;
import edu.edina.library.util.Point;
import edu.edina.library.util.Position;
import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.DriveDirection;

@TeleOp(name = "TeleOpMain 🥧")
public class TeleOpMain extends LinearOpMode {
    protected int endGameTime;

    protected int liftLimit;

    public TeleOpMain() {
        endGameTime = 90;
        liftLimit = 2500;
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

        while (opModeInInit()) {
            piBot.setupVision(false, true);
        }

        hw.hangRight.setPosition(Servo.MIN_POSITION + 0.05);
        hw.hangLeft.setPosition(Servo.MAX_POSITION - 0.05);
        ElapsedTime startTime = new ElapsedTime();

        double noLift = hw.liftMotor.getCurrentPosition();

        double powerLimit = 1;
        double liftPosition = noLift;

        boolean invertStrafe = false;

        while (opModeIsActive()) {
            piBot.setupVision(false, true);

            double max;

            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            if (invertStrafe) {
                lateral = -lateral;
            }

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            int currPos = hw.liftMotor.getCurrentPosition();
            if (gamepad2.dpad_up && currPos < noLift + liftLimit) {
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
            if (gamepad1.dpad_right) {
//                hw.intakeRight.setPosition(0.6);
                piBot.drop(GrabberSide.Right);
            }
            if (gamepad1.dpad_left) {
                piBot.drop(GrabberSide.Left);
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
                invertStrafe = true;
            }

            if (gamepad2.right_trigger > 0.8) {
                piBot.positionGrabber(0, true);
                invertStrafe = false;
            }

            piBot.runGrabber();

            if (startTime.seconds() > endGameTime) {
                if (gamepad2.left_stick_button && gamepad2.right_stick_button) {
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

            PixelDetect.Result r = piBot.detectPixel();
            if (gamepad1.x && gamepad1.b) {
                if (r != null) {
                    telemetry.addData("pixel detected", "yes");
                    telemetry.addData("angleDeg", "dest=%.1f", r.angleDeg);
                    telemetry.addData("lateral", "%.1fin", r.s);
                    telemetry.update();

                    piBot.planRelativeRotate(r.angleDeg);
                    while (opModeIsActive()) {
                        if (piBot.runRotate() == DriveStatus.Done) {
                            break;
                        }
                    }

                    sleep(100);

                    piBot.planRelativeDrive(r.s, DriveDirection.Lateral);
                    while (opModeIsActive()) {
                        if (piBot.runDrive() == DriveStatus.Done) {
                            break;
                        }
                    }

                    sleep(100);

                    piBot.planRelativeDrive(r.d / 2, DriveDirection.Axial);
                    while (opModeIsActive()) {
                        if (piBot.runDrive() == DriveStatus.Done) {
                            break;
                        }
                    }

                    piBot.setZeroPowerBrake();
                }
            }

            telemetry.addData("timer", "%.1f", startTime.seconds());
            telemetry.addData("power limit", "%.1f", powerLimit);
            telemetry.addData("lift pos/tgt/lim", "%d/%.1f/%d",
                    hw.liftMotor.getCurrentPosition(),
                    liftPosition,
                    liftLimit);
            telemetry.addData("invert", invertStrafe);
            if (r != null)
                telemetry.addData("pixel detected", "yes @ %.1f", r.s);
            else
                telemetry.addData("pixel detected", "no");
            telemetry.update();
        }
    }
}
