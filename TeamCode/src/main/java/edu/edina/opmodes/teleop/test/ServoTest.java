package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.RobotHardware;

@TeleOp
public class ServoTest extends LinearOpMode {
    private RobotHardware hw;

    @Override
    public void runOpMode() {
        hw = new RobotHardware(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                telemetry.addData("lift", "raise");
                telemetry.update();

                hw.hangRight.setPosition(Servo.MAX_POSITION - 0.05);
                hw.hangLeft.setPosition(Servo.MIN_POSITION + 0.05);
                hw.hangLiftLeft.setPosition(0.1);
                sleep(1000);
                hw.hangLiftRight.setPosition(0.5);
            }
            if (gamepad1.left_bumper) {
                hw.hangRight.setPosition(Servo.MIN_POSITION + 0.05);
                hw.hangLeft.setPosition(Servo.MAX_POSITION - 0.05);
                hw.hangLiftLeft.setPosition(1);
                sleep(1000);
                hw.hangLiftRight.setPosition(0);
            }
        }
    }
}

