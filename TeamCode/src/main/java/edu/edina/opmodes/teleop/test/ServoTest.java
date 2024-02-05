package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.RobotHardware;

@Disabled
@TeleOp
public class ServoTest extends LinearOpMode {
    private RobotHardware hw;

    @Override
    public void runOpMode() {
        hw = new RobotHardware(hardwareMap);

        waitForStart();

        hw.hangRight.setPosition(Servo.MIN_POSITION + 0.05);
        hw.hangLeft.setPosition(Servo.MAX_POSITION - 0.05);
        hw.hangLiftLeft.setPosition(0.2);
        hw.hangLiftRight.setPosition(0.8);

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                hw.hangRight.setPosition(Servo.MAX_POSITION - 0.05);
                hw.hangLeft.setPosition(Servo.MIN_POSITION + 0.05);
                hw.hangLiftLeft.setPosition(0.8);
                hw.hangLiftRight.setPosition(0.2);
            }
        }
    }
}
