package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp
public class HangingMotorTest extends OpMode {
    public DcMotor HangingMotor = null;

    @Override
    public void init() {
        HangingMotor = hardwareMap.get(DcMotor.class, "hangingMotor");
        HangingMotor.setDirection(DcMotor.Direction.FORWARD);
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
            HangingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);// I used the zero power behaviour that the guy at the tournament recommended us
            HangingMotor.setPower(1);
        } else {
           HangingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            HangingMotor.setPower(-1);
        }
    }
}
