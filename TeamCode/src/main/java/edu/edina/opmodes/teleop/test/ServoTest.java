package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        Servo hangLeft = hardwareMap.get(Servo.class, "hangLeft");
        Servo hangRight = hardwareMap.get(Servo.class, "hangRight");
        Servo hangLiftLeft = hardwareMap.get(Servo.class, "hangLiftLeft");
        Servo hangLiftRight = hardwareMap.get(Servo.class, "hangLiftRight");

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                hangRight.setPosition(1);
                hangLeft.setPosition(0);
            }
            if (gamepad1.right_trigger > 0.8) {
                hangRight.setPosition(0.25);
                hangLeft.setPosition(0.75);
            }
            if (gamepad1.left_bumper) {
                hangLiftLeft.setPosition(0);
                hangLiftRight.setPosition(0);
            }
            if (gamepad1.left_trigger > 0.8) {
                hangLiftLeft.setPosition(1);
                hangLiftRight.setPosition(0);
            }
        }
    }
}
