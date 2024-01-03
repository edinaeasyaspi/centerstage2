package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class IntakeTest extends OpMode {
    public Servo leftLiftServo = null;
    public Servo rightLiftServo = null;
    public double leftextendClaw = 0.8;
    public double rightextendClaw = 0.3;
    public double leftretractClaw = 0.3;
    public double rightretractClaw = 0.8;

    @Override
    public void init() {
        leftLiftServo = hardwareMap.get(Servo.class, "S1");
        rightLiftServo = hardwareMap.get(Servo.class, "S2");
        leftLiftServo.setPosition(leftretractClaw);
        rightLiftServo.setPosition(rightretractClaw);
    }

    @Override
    public void loop() {
        Intake();
    }

    public void Intake() {
        if (gamepad1.right_bumper) {
            leftLiftServo.setPosition(leftextendClaw);
            rightLiftServo.setPosition(rightextendClaw);
        } else if (gamepad1.left_bumper) {
            leftLiftServo.setPosition(leftretractClaw);
            rightLiftServo.setPosition(rightretractClaw);
        } else {
            leftLiftServo.setPosition(leftretractClaw);
            rightLiftServo.setPosition(rightretractClaw);
        }}}

        /* When the right bumper is clicked, the servo will move until the right bumper is not pressed
         When the left bumper is clicked, the servo will move the opposite way until the left bumper is not pressed
         When either of the bumpers are pressed, the servos will move to a different position until the bumpers are not pressed
         The servos will lock their position when the bumpers are not pressed
         They should be in dual mode and they should both be able to have their own command to move independently
         They should also have another command that makes them both move to the same position with a single push of a button
         I got a start on this code so we can test it on thursday or whenever the claw is finished and ready for the coders to code */