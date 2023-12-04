package edu.edina.opmodes.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "ColorSensor", group = "TeleOp")
class ColorSensorTeleOp extends LinearOpMode {

    private ColorSensor colorSensor;

    @Override
    public void runOpMode() {

        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");


        waitForStart();

        while (opModeIsActive()) {

            int redValue = colorSensor.red();
            int blueValue = colorSensor.blue();
            int greenValue = colorSensor.green();

            final int RED_THRESHOLD = 50;
            final int BLUE_THRESHOLD = 50;

            if (redValue > RED_THRESHOLD && blueValue < BLUE_THRESHOLD) {
                telemetry.addData("Detected Color", "Red");
            } else if (blueValue > BLUE_THRESHOLD && redValue < RED_THRESHOLD) {
                telemetry.addData("Detected Color", "Blue");
            } else {
                telemetry.addData("Detected Color", "Unknown");
            }

            telemetry.addData("Red", redValue);
            telemetry.addData("Blue", blueValue);
            telemetry.addData("Green", greenValue);
            telemetry.update();

        }
    }
}
