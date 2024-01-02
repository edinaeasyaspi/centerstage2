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
            //When the right bumper is clicked, the servo will move until the right bumper is not pressed
            //When the left bumper is clicked the servo will move the opposite way until the left bumper is not pressed
            //When either of the bumpers are pressed, the servos will move to a different position until the bumpers are not pressed
            //The servos will lock their position when the bumpers are not pressed
            //They should be in dual mode and they should both be able to have their own command to move independently
            //They should also have another command that makes them both move to the same position will the single push of a button
                }
            }
        }






