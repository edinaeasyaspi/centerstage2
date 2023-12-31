package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import edu.edina.library.util.Robot;




@TeleOp
public class HangingMotorTest extends OpMode {
    public DcMotor frontLeftMotor = null;

    protected Robot robot;

    @Override
    public void init() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void loop() {
        HangingMotor_code();
    }

    @Override
    public void stop() {
    }

    public void HangingMotor_code() {
        double HangingMotorPower = 0.3;
        double HangingMotorPower2 = -0.3;

        if (gamepad1.x) {
            frontLeftMotor.setPower(1);
        } else {
            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontLeftMotor.setPower(0);

        }
        if (gamepad1.y) {
            frontLeftMotor.setPower(-1);
        } else {
            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontLeftMotor.setPower(0);
        }
    }
}
