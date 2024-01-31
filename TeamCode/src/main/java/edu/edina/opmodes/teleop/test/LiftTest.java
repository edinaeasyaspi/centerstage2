package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp
public class LiftTest extends OpMode {
    public DcMotorEx liftMotor = null;

    @Override
    public void init() {
        DcMotorEx liftMotor = hardwareMap.get(DcMotorEx.class, "liftMotor");
    }

    @Override
    public void loop() {
        LiftTest();
    }

    public void LiftTest() {
        if (gamepad2.back && gamepad2.start) {
            liftMotor.setPower(-0.1);
        }  else liftMotor.setPower(0);
    }

}
