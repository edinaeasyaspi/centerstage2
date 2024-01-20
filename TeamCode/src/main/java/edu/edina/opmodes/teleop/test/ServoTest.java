package edu.edina.opmodes.teleop.test;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends LinearOpMode {
    @Override
    public void runOpMode(){
        Servo hangLeft=hardwareMap.get(Servo.class, "hangLeft");
        Servo hangRight=hardwareMap.get(Servo.class, "hangRight");
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.right_bumper){
                hangRight.setPosition(-0.8);
            }
            if(gamepad1.right_trigger>0.8){
                hangRight.setPosition(0);
            }
            if(gamepad1.left_bumper){
                hangLeft.setPosition(1);
            }
            if(gamepad1.left_trigger>0.8){
                hangLeft.setPosition(0.25);
            }
        }
    }
}
