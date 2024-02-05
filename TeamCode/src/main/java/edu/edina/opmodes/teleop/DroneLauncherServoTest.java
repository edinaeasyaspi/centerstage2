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
        droneLauncherServoTest();
    }

    public void droneLauncherServoTest() {
        if (gamepad2.back && gamepad2.start) {
            leftIntakeServo.setPower(-1);
        }  else leftIntakeServo.setPower(0);
    }

}

