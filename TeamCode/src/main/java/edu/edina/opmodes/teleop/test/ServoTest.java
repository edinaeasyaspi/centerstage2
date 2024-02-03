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
            if(gamepad1.right_bumper) {
                hangRight.setPosition(-1);
                hangLeft.setPosition(1);
            }  else {
                hangRight.setPosition(0);
                hangLeft.setPosition(0);
            }
        }
    }
}
