package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "RambotsPurpleTeleOp")
public class linearOpMode extends LinearOpMode {
  private DcMotor frontLeft = null, rearLeft = null;
  private DcMotor frontRight = null, rearRight = null;
  private DcMotor CongislideExtension = null;
  private DcMotor arm1 = null;
  private DcMotor arm2 = null;


  private Servo  wrist1 = null;
  private Servo  wrist2 = null;
  private Servo intake = null;
  private double intakePower = .7;

  static final double COUNTS_PER_MOTOR_REV = 537.6; // eg: TETRIX Motor Encoder

  final private int ABD_TO_RUNG = 100;
  final private int ABD_DOWN = 0;
  private boolean runningPreset = false;


  // THE SENSOR
  private ColorSensor sensor  = null;

  // wrist
  private double wristPos = 0;
  private double wrist2Pos = 0;



  @Override
  public void runOpMode() {


    // initializing hardware

    // Drive Train motor
    frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
    frontRight = hardwareMap.get(DcMotor.class, "rightFront");
    rearLeft = hardwareMap.get(DcMotor.class, "leftBack");
    rearRight = hardwareMap.get(DcMotor.class, "rightBack");

    frontLeft.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    frontRight.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    rearLeft.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    rearRight.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

// DcMotors for Linear slide
    slideExtension = hardwareMap.get(DcMotor.class, "slideExtend");
    wrist1 = hardwareMap.get(Servo.class, "wrist1");
    wrist2 = hardwareMap.get(Servo.class,"wrist2");
    arm1 = hardwareMap.get(DcMotor.class, "slideAbd");
    arm2 = hardwareMap.get(DcMotor.class, "slideAbd2");

    slideExtension.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    arm1.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    arm2.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);



    // Takers
    intake = hardwareMap.get(Servo.class, "intake");
    sensor = hardwareMap.get(ColorSensor.class, "sensor");

    // MaybeIntake = hardwareMap.get(DcMotor.class, "intake");
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
    arm2.setDirection(DcMotor.Direction.REVERSE);

    // normalize motor positions
    double normalizedAbdPos1 = (double)arm1.getCurrentPosition() / COUNTS_PER_MOTOR_REV; // TODO test

    // ENCODERS
    arm2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    arm2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    slideExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    slideExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


//    leftIntake.setPosition(0);
//    rightIntake.setPosition(0);
    //wrist
    waitForStart();

    if (isStopRequested()) return;

    while (opModeIsActive()) {

      /*
        GamePad Map
        GamePad 1 (Driver)
          Left JoyStick = lateral, diagonal, forwards and backwards movements
          Right JoyStick = Rotation of drive train
        GamePad 2 (Operator)
          Button A = toggle position of claw to open or closed (We start closed)
          left stick x = slide extension
          right stick y = slide abduction
       */

      // linear slide controls


      double slideExtendPower = gamepad2.left_stick_y;
      double slideAbdPower = gamepad2.right_stick_y;

      // drive train controls
      double y = -gamepad1.left_stick_y;
      double x = gamepad1.left_stick_x * 1.1;
      double turn = gamepad1.right_stick_x;

      // input: theta and power
      // theta is where we want the direction the robot to go
      // power is (-1) to 1 scale where increasing power will cause the engines to go faster
      double theta = Math.atan2(y, x);
      double power = Math.hypot(x, y);
      double sin = Math.sin(theta - Math.PI / 4);
      double cos = Math.cos(theta - Math.PI / 4);
      // max variable allows to use the motors at its max power with out disabling it
      double max = Math.max(Math.abs(sin), Math.abs(cos));

      double frontLeftPower = power * cos / max + turn;
      double frontRightPower = power * cos / max - turn;
      double rearLeftPower = power * sin / max + turn;
      double rearRightPower = power * sin / max - turn;

      // Prevents the motors exceeding max power thus motors will not seize and act sporadically
      if ((power + Math.abs(turn)) > 1) {
        frontLeftPower /= power + turn;
        frontRightPower /= power - turn;
        rearLeftPower /= power + turn;
        rearRightPower /= power - turn;
      }

      if (gamepad2.a && intakeReleased) {
        intakePower = .45 - intakePower;
        intakeReleased = false;
      }
      if (gamepad2.b && intakeReleased) {
        intakePower = Math.max(0, intakePower - 0.1);
        intakeReleased = false;
      } else if (gamepad2.y && intakeReleased) {
        intakePower = Math.min(1, intakePower + 0.1);
        intakeReleased = false;
      }

      if(!gamepad2.a && !gamepad2.b && !gamepad2.y) {
        intakeReleased = true;
      }

      // presets
      if (gamepad2.dpad_up && !runningPreset) {
        runningPreset = true;
        arm1.setTargetPosition(ABD_TO_RUNG);
        arm2.setTargetPosition(ABD_TO_RUNG);
      } else if (gamepad2.dpad_down && !runningPreset) {
        runningPreset = true;
        arm1.setTargetPosition(ABD_DOWN);
        arm2.setTargetPosition(ABD_DOWN);
      } // if

      // STOP ALL PRESETS
      if (gamepad2.dpad_left) {
        runningPreset = false;
      }

      if (runningPreset && arm1.getCurrentPosition() > arm1.getTargetPosition() - 5 && arm1.getCurrentPosition() < arm1.getTargetPosition() + 5) {
        runningPreset = false;
      }

      // Power to the wheels
      frontLeft.setPower(frontLeftPower);
      rearLeft.setPower(rearLeftPower);
      frontRight.setPower(frontRightPower);
      rearRight.setPower(rearRightPower);

      // Power to the arm
      if(runningPreset) {
        // TODO FIX
        arm1.setPower(0.5);
        arm2.setPower(0.5);
      } else {
        arm1.setPower(-slideAbdPower * 0.65);
        arm2.setPower(-slideAbdPower * 0.65);
      } // if else

      slideExtension.setPower(slideExtendPower);

      // Wrist power
      if (gamepad2.left_trigger > 0) {
        wristPos = Math.min(1, wristPos + 0.020);
      } else if (gamepad2.left_bumper) {
        wristPos = Math.max(0, wristPos - 0.020);
      }

      if (gamepad2.right_trigger > 0) {
        wrist2Pos = Math.min(1, wrist2Pos + 0.020);
      } else if (gamepad2.right_bumper) {
        wrist2Pos = Math.max(0, wrist2Pos - 0.020);
      }

      wrist1.setPosition(wristPos);
      wrist2.setPosition(wrist2Pos);


      // Power to the intake
      intake.setPosition(intakePower);


      // Telemetry
      telemetry.addData("RUNNING PRESET:", runningPreset);
      telemetry.addData("RUNMODE:", arm1.getMode());
      telemetry.addData("Normalized Abd 1 position:", normalizedAbdPos1);
      telemetry.addData("Abd 1 position:", arm1.getCurrentPosition());
      telemetry.addData("Abd 2 position:", arm2.getCurrentPosition());
      telemetry.addData("Ext position:", slideExtension.getCurrentPosition());
      telemetry.addData("wrist pos:", wrist1);
      telemetry.addData("X", x);
      telemetry.addData("Y", y);
      telemetry.addData("Alpha", sensor.alpha());
      telemetry.addData("Red  ", sensor.red());
      telemetry.addData("Green", sensor.green());
      telemetry.addData("Blue ", sensor.blue());
      telemetry.addData("Intake pos (right is inverse): ", intakePower);
      telemetry.addData("Slide extension power: ", slideExtendPower);
      telemetry.addData("Slide abduction power: ", slideAbdPower);
//      telemetry.addData("Wrist power: ", wristpower);
      telemetry.update();

    }
  }

}