package edu.edina.library.util;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class Parts {
    public WebcamName webcam;

    public BHI260IMU imu;

    public Parts(HardwareMap hardwareMap) {
        webcam = hardwareMap.get(WebcamName.class, "LogitechC270_8034PI");
        imu = hardwareMap.get(BHI260IMU.class, "imu");
    }
}
