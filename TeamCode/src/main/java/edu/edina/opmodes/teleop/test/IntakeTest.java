package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class IntakeTest extends OpMode {
    public Servo leftLiftServo = null;
    public Servo rightLiftServo = null; //Telling the control hub which pieces to use and how to call them from the robot hardware

    private ElapsedTime runtime = new ElapsedTime();

    private double extendSpeed = 0.75; // I'll ask Ratik his preferred speed for the claw to open and close
    private double retractSpeed = -0.75;

    @Override
    public void init() {
        leftLiftServo = hardwareMap.get(Servo.class, "S1"); //Declare the components used
        rightLiftServo = hardwareMap.get(Servo.class, "S2");
    }

    @Override
    public void loop() {
        IntakeRun(); //Loop the code
    }

    public void IntakeRun() {
            if (gamepad1.right_bumper) {
                extendServos(); //If the right bumper is pressed it will call the "extendServos" and it will continue making the new servo positions
            }
            else if (gamepad1.left_bumper) {
                retractServos(); //If the left bumper is pressed, it will call the "retractServos" and make the new servo positions from there
            }
            else {
                stopServos(); //If neither the left or the right bumpers are pressed, it will stop the servos in their current position
            }
        }


    private void extendServos() {
        double currentPosition = leftLiftServo.getPosition();
        double newPosition = currentPosition + extendSpeed * runtime.seconds();
        newPosition = Math.max(Servo.MIN_POSITION, Math.min(Servo.MAX_POSITION, newPosition));
        leftLiftServo.setPosition(newPosition); //Set the positions based on the runtime
        rightLiftServo.setPosition(newPosition);
        runtime.reset();
    }

    private void retractServos() {
        double currentPosition = leftLiftServo.getPosition();
        double newPosition = currentPosition + retractSpeed * runtime.seconds();
        newPosition = Math.max(Servo.MIN_POSITION, Math.min(Servo.MAX_POSITION, newPosition));
        leftLiftServo.setPosition(newPosition); //Set the new positions based on the new runtime data
        rightLiftServo.setPosition(newPosition);
        runtime.reset();
    }

    private void stopServos() {
        leftLiftServo.setPosition(leftLiftServo.getPosition()); //Reset the servos
        rightLiftServo.setPosition(rightLiftServo.getPosition());
        runtime.reset(); //Rest the runtime
    }
}

        /* When the right bumper is clicked, the servo will move until the right bumper is not pressed
         When the left bumper is clicked, the servo will move the opposite way until the left bumper is not pressed
         When either of the bumpers are pressed, the servos will move to a different position until the bumpers are not pressed
         The servos will lock their position when the bumpers are not pressed
         They should be in dual mode and they should both be able to have their own command to move independently
         They should also have another command that makes them both move to the same position with a single push of a button
         I got a start on this code so we can test it on thursday or whenever the claw is finished and ready for the coders to code */