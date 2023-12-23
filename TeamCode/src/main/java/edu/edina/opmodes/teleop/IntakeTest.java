package edu.edina.opmodes.teleop;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Disabled
public class IntakeTest extends OpMode {
    public Servo leftLiftServo = null;
    public Servo rightLiftServo = null;
    @Override
    public void init() {
        leftLiftServo = hardwareMap.get(Servo.class, "S1");
        rightLiftServo = hardwareMap.get(Servo.class, "S2");
    }
    @Override
    public void loop() {
        Intake();
    }
    public void Intake() {
        if (gamepad1.dpad_up) {
            leftLiftServo.setPosition(Servo.MAX_POSITION);
            rightLiftServo.setPosition(Servo.MIN_POSITION);
        } else {
            leftLiftServo.setPosition(Servo.MIN_POSITION);
            rightLiftServo.setPosition(Servo.MAX_POSITION);
        }
    }
}