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

            double leftTriggerValue = gamepad1.left_trigger;
            double rightTriggerValue = gamepad1.right_trigger;


            if (leftTriggerValue > 0.5) {
                leftLiftServo.setPosition(1.0);
            }
            
            else if (rightTriggerValue > 0.5) {
                leftLiftServo.setPosition(0.0);
            }

            telemetry.addData("Servo Position", leftLiftServo.getPosition());
            telemetry.update();

            idle();
        }
    }
}
