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
        Servo hangLiftLeft=hardwareMap.get(Servo.class, "hangLiftLeft");
        Servo hangLiftRight=hardwareMap.get(Servo.class, "hangLiftRight");


        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.right_bumper) {
                hangRight.setPosition(Servo.MAX_POSITION);
                hangLeft.setPosition(Servo.MIN_POSITION);
                hangLiftLeft.setPosition(0.8);
                hangLiftRight.setPosition(0.2);
            }  else {
                hangRight.setPosition(Servo.MIN_POSITION);
                hangLeft.setPosition(Servo.MAX_POSITION);
                hangLiftLeft.setPosition(0.2);
                hangLiftRight.setPosition(0.8);
            }
        }
    }
}
