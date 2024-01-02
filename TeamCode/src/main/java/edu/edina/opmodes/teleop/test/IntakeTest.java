package edu.edina.opmodes.teleop.test;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;



@TeleOp

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
        if (gamepad1.right_bumper) {
            leftLiftServo.setPosition(
                }
            }
        }






