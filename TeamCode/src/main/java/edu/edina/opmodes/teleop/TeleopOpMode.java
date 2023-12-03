package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

public class TeleopOpMode extends OpMode {
    protected Robot robot;

    RobotHardware robotHardware;

    protected SmartGamepad driver1Gamepad;
    protected SmartGamepad driver2Gamepad;
    private WebcamName WebcamName;

    @Override
    public void init() {
        driver1Gamepad = new SmartGamepad(gamepad1);
        driver2Gamepad = new SmartGamepad(gamepad2);
        robotHardware = new RobotHardware(hardwareMap);
    }

    @Override
    public void init_loop() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robotHardware; // Changed 'robot.RobotHardware' to 'robotHardware'
        RobotConfiguration config = RobotConfiguration.getInstance();

        telemetry.update();
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        driver1Gamepad.update();
        driver2Gamepad.update();

        // Access the webcam as needed
        WebcamName  = robotHardware.LogitechC270_8034PI;
        // You can use 'webcamName' here or pass it to relevant robot methods

        robot.MecanumDrive.setProperties(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        robot.Intake.setProperties(driver1Gamepad.left_bumper, driver1Gamepad.right_bumper);

        robot.Lift.setProperties(gamepad2.right_trigger != 0, gamepad2.left_trigger != 0,
                gamepad2.left_bumper, gamepad2.right_bumper);

        telemetry.addData("Gamepad2.right", gamepad2.right_trigger);
        telemetry.addData("Gamepad2.left", gamepad2.left_trigger);
        telemetry.addData("Lift Position", RobotState.getInstance().liftTipState);
        robot.update();
        robot.telemetry();
    }

    @Override
    public  void stop() {
        robot.stop();
    }
}
