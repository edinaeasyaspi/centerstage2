package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp
public class LiftTest extends LinearOpMode {
    public DcMotorEx liftMotor = null;

    @Override
    public void runOpMode() throws InterruptedException {
        liftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        liftMotor = hardwareMap.get(DcMotorEx.class, "liftMotor");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad2.right_bumper) {
                liftMotor.setTargetPosition(500);
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftMotor.setPower(0.1);
            } else liftMotor.setPower(0);
        }
    }
}



//    @Override
//    public void init() {
//        DcMotorEx liftMotor = hardwareMap.get(DcMotorEx.class, "liftMotor");
//    }
//
//    @Override
//    public void loop() {
//        LiftTest();
//    }

