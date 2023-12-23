package edu.edina.opmodes.teleop;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class DroneLauncherServoTest extends OpMode {
    public CRServo leftIntakeServo = null;

    @Override
    public void init() {
        leftIntakeServo = hardwareMap.get(CRServo.class, "F1");
    }

    @Override
    public void loop() {
        droneLauncherServo();
    }

    public void droneLauncherServo() {
        if (gamepad2.dpad_up) {
            leftIntakeServo.setPower(-0.2);
        } else if (gamepad2.dpad_down) {
            leftIntakeServo.setPower(0.2);
        } else {
            leftIntakeServo.setPower(0);
        }
    }
}
