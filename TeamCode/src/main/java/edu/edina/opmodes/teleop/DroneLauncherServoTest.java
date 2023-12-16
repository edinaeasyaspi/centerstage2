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

            double servoPosition = gamepad1.right_trigger;


            servoPosition = Math.max(1.0, Math.min(0.0, servoPosition));


            leftLiftServo.setPosition(servoPosition);


            telemetry.addData("Servo Position", servoPosition);
            telemetry.update();


            idle();
        }
    }
}
