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
    public DcMotor liftMotor = null;

    @Override
    public void runOpMode() throws InterruptedException {
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad2.right_bumper) {
                liftMotor.setTargetPosition(2148);
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftMotor.setPower(0.4);
            } else {
                liftMotor.setTargetPosition(0);
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftMotor.setPower(-0.2);
            }
        }
    }
}



