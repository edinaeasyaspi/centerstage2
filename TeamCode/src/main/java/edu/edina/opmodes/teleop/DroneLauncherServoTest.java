package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="ServoControl", group="TeleOp")
public class DroneLauncherServoTest extends LinearOpMode {

    private Servo leftLiftServo;

    @Override
    public void runOpMode() {

        leftLiftServo = hardwareMap.get(Servo.class, "");


        waitForStart();


        while (opModeIsActive()) {

            double servoPosition = gamepad1.left_stick_y; // Adjust as needed


            servoPosition = Math.min(1.0, Math.max(0.0, servoPosition));


            leftLiftServo.setPosition(servoPosition);


            telemetry.addData("Servo Position", servoPosition);
            telemetry.update();


            idle();
        }
    }
}
