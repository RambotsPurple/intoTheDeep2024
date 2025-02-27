package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotConfig {


    public static DcMotorEx encoder = null;
    public static DcMotorEx frontLeft = null, rearLeft = null;
    public static DcMotorEx frontRight = null, rearRight = null;
    public static DcMotorEx slideExtension = null;
    public static DcMotorEx arm1 = null;

    public static DcMotorEx arm2 = null;
    public static Servo wrist1 = null;

    public static Servo intake = null;


    public static HardwareMap hardwareMap;
    public static final double COUNTS_PER_MOTOR_REV = 537.6; // eg: TETRIX Motor Encoder
    public static IMU imu;
    public static double direction;
    public static double lastAngles;

    // ARM POSITIONS
    // TODO TUNE
    public static final  int ABD_SPEC = -2933;
    public final static int ABD_FISH = 0;
    public final static int EXT_BASKET = -62; //@TODO  get the encoder values

    public final static  int EXT_REG = 0;
    public final static int ABD_PICKUP = -883;
    public final static double WRIST_START = 0.16;
    public final static double WRIST_PICKUP = 0.37;


    public static void initialize(HardwareMap hw) {
        hardwareMap = hw;

        imu = hardwareMap.get(IMU.class, "imu");

        imu.initialize(
                new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                ))
        );

        // Drive Train motor
        frontLeft = hardwareMap.get(DcMotorEx.class, "leftFront");
        frontRight = hardwareMap.get(DcMotorEx.class, "rightFront");
        rearLeft = hardwareMap.get(DcMotorEx.class, "leftBack");
        rearRight = hardwareMap.get(DcMotorEx.class, "rightBack");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

// DcMotors for Linear slide
        slideExtension = hardwareMap.get(DcMotorEx.class, "slideExtend");
        wrist1 = hardwareMap.get(Servo.class, "wrist1");
        arm1 = hardwareMap.get(DcMotorEx.class, "slideAbd");
        arm2 = hardwareMap.get(DcMotorEx.class, "slideAbd2");

//        encoder
        encoder = hardwareMap.get(DcMotorEx.class,"encoder");

        slideExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm2.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));


        // Takers
        intake = hardwareMap.get(Servo.class, "intake");


        // MaybeIntake = hardwareMap.get(DcMotorEx.class, "intake");
        // Setting the direction for the motor on where to rotate

        // Orientation for drivetrain
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        rearRight.setDirection(DcMotor.Direction.REVERSE);

        // intake
        boolean intakeReleased = true;

        //catch intake
        boolean CatchRelease = true;

        // linear slide
        slideExtension.setDirection(DcMotor.Direction.FORWARD);
        arm1.setDirection(DcMotor.Direction.FORWARD);
        arm2.setDirection(DcMotor.Direction.FORWARD);

        // normalize motor positions
        double normalizedAbdPos1 = (double)arm1.getCurrentPosition() / COUNTS_PER_MOTOR_REV; // TODO test

        // ENCODERS


        arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideExtension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        resetAngle();
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

    public static void drive(double x, double y, double turn, double direction){
        // input: theta and power
        // theta is where we want the direction the robot to go
        // power is (-1) to 1 scale where increasing power will cause the engines to go faster
        double theta = Math.atan2(y, x) - Math.toRadians(direction);
        double power = Math.hypot(x, y);
        double sin = Math.sin(theta - Math.PI / 4);
        double cos = Math.cos(theta - Math.PI / 4);
        // max variable allows to use the motors at its max power with out disabling it
        double max = Math.abs(Math.max(Math.abs(sin) + turn, Math.abs(cos) + turn));
        max = Math.max(max, 1);

        double frontLeftPower = power * cos / max + turn;
        double frontRightPower = power * sin / max - turn;
        double rearLeftPower = power * sin / max + turn;
        double rearRightPower = power * cos / max - turn;

        // Prevents the motors exceeding max power thus motors will not seize and act sporadically
        if ((power + Math.abs(turn)) > 1) {
            frontLeftPower /= power + turn;
            frontRightPower /= power - turn;
            rearLeftPower /= power + turn;
            rearRightPower /= power - turn;
        }

        // Power to the wheels
        frontLeft.setPower(frontLeftPower);
        rearLeft.setPower(rearLeftPower);
        frontRight.setPower(frontRightPower);
        rearRight.setPower(rearRightPower);

    }


}