package edu.edina.opmodes.teleop.test;

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



        //Servo hangLeft = hardwareMap.get(Servo.class, "hangLeft");
        // Servo hangRight = hardwareMap.get(Servo.class, "hangRight");
        // Servo hangLiftLeft = hardwareMap.get(Servo.class, "hangLiftLeft");
        // Servo hangLiftRight = hardwareMap.get(Servo.class, "hangLiftRight");

        waitForStart();

        // hangRight.setPosition(Servo.MIN_POSITION + 0.05);
        hw.hangLeft.setPosition(Servo.MAX_POSITION - 0.05);
        // hangLiftLeft.setPosition(0.2);
        // hangLiftRight.setPosition(0.8);

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                // hangRight.setPosition(Servo.MAX_POSITION - 0.05);
                hw.hangLeft.setPosition(Servo.MIN_POSITION + 0.05);
                // hangLiftLeft.setPosition(0.8);
                // hangLiftRight.setPosition(0.2);
            }
        }
    }
}
