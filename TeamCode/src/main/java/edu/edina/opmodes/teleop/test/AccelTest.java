package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.PiMotor;

@Autonomous
public class AccelTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        PiBot bot = new PiBot(new RobotHardware(hardwareMap));
        waitForStart();

        ElapsedTime accel = new ElapsedTime();
        while (opModeIsActive()) {
            if (accel.seconds() < 1) {
                for (int i = 0; i < 4; i++) {
                    PiMotor m = bot.drive.get(i);
                    m.setDriving(true, 1);
                    m.sample();
                    double s = m.getDegSpeed();
                    m.run(s, 1, 1, 0);
                }
            } else {
                break;
            }
        }

        Accelerometer
        while (opModeIsActive()) {
            for (int i = 0; i < 4; i++) {
                PiMotor m = bot.drive.get(i);
                m.setDriving(false, 1);
                m.sample();
                double s = m.getDegSpeed();
                m.run(s, 1, 1, 0);
                if (s < 0)
                    break;
            }
        }
    }
}
