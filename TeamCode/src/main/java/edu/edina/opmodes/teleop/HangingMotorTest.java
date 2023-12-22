package edu.edina.opmodes.teleop;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import edu.edina.library.util.Robot;
@TeleOp
public class HangingMotorTest extends OpMode {
    public Servo leftHangServo = null;
    public Servo rightHangServo = null;
    @Override
    public void init() {
        leftHangServo = hardwareMap.get(Servo.class, "S1");
        rightHangServo = hardwareMap.get(Servo.class, "S2");
    }
    @Override
    public void loop() {
        HangingMotor_code();
    }
    public void HangingMotor_code() {
        if (gamepad1.x) {
            leftHangServo.setPosition(1.0);
            rightHangServo.setPosition(1.0);
        } else {
            leftHangServo.setPosition(0.0);
            rightHangServo.setPosition(0.0);
        }
    }
}