package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Config {
    public static DcMotorEx frontLeft;
    public static DcMotorEx frontRight;
    public static DcMotorEx rearLeft;
    public static DcMotorEx rearRight;

    public static DcMotorEx arm;
    public static DcMotorEx slide;
    public static CRServo intakeLeft;
    public static CRServo intakeRight;

    public static HardwareMap hardwareMap;
    public static Telemetry telemery;

    public static IMU imu;
    public static double direction;
    private static double lastAngles;



    public static void initialize(HardwareMap hw, Telemetry tele) {
        hardwareMap = hw;
        telemery = tele;

        imu = hardwareMap.get(IMU.class, "imu");

        imu.initialize(
                new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                ))
        );

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotorEx.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "rearRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        arm = hardwareMap.get(DcMotorEx.class, "arm");
        slide = hardwareMap.get(DcMotorEx.class, "slide");

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        slide.setDirection(DcMotorSimple.Direction.FORWARD);

        arm.setPower(1);
        slide.setPower(1);
        arm.setTargetPosition(0);
        slide.setTargetPosition(0);

        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        intakeLeft = hardwareMap.get(CRServo.class, "intakeLeft");
        intakeRight = hardwareMap.get(CRServo.class, "intakeRight");

        intakeRight.setDirection(CRServo.Direction.REVERSE);
    }

    public static void resetAngle()
    {
        lastAngles = imu.getRobotYawPitchRollAngles().getYaw();

        direction = 0;
    }

    public static double getAngle()
    {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        double angles = imu.getRobotYawPitchRollAngles().getYaw();

        double deltaAngle = angles - lastAngles;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        direction += deltaAngle;

        lastAngles = angles;

        return direction;
    }



}