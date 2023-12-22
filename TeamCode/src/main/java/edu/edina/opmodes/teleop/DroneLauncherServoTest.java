package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="DroneLauncherServoTest", group="TeleOp")
public class DroneLauncherServoTest extends LinearOpMode {

    private Servo leftLiftServo;

    @Override
    public void runOpMode() {
        leftLiftServo = hardwareMap.get(Servo.class, "S1");

        waitForStart();

        while (opModeIsActive()) {
            double servoPosition = gamepad2.right_trigger;


            servoPosition = servoPosition * 2.0 - 1.0;


            servoPosition = Math.max(-1.0, Math.min(1.0, servoPosition));


            leftLiftServo.setPosition(servoPosition);

            telemetry.addData("Gamepad Trigger Value", gamepad2.right_trigger);
            telemetry.addData("Servo Position", servoPosition);
            telemetry.update();

            idle();
        }
    }
}
