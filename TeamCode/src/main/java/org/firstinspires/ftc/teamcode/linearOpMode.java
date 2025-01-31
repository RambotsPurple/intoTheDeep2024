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
  private DcMotor frontLeftMotor = null, backLeftMotor = null;
  private DcMotor frontRightMotor = null, backRightMotor = null;
  private DcMotor slideExtension = null;
  private DcMotor slideAbduction = null;
  private DcMotor slideAbduction2 = null;

  private DcMotor Catch = null;

  private Servo CatchEx = null;

  private  Servo CatchClaw2 = null;
  private  Servo CatchClaw1 = null;
  private Servo  wrist1 = null;
  private Servo leftIntake = null;
  private Servo rightIntake = null;
  private double intakePower = 1;

  static final double COUNTS_PER_MOTOR_REV = 537.6; // eg: TETRIX Motor Encoder

  final private int ABD_TO_RUNG = 100;
  final private int ABD_DOWN = 0;
  private boolean runningPreset = false;

  private double catchintake =1;


  // THE SENSOR
  private ColorSensor sensor  = null;

  // wrist
  private double wristPos = 0;

  @Override
  public void runOpMode() {

    // initializing hardware

    // Drive Train motor
    frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
    frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
    backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
    backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");

    frontLeftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    frontRightMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    backLeftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    backRightMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

//    Dc motor for catch

    Catch = hardwareMap.get(DcMotor.class, "catch");
    CatchEx = hardwareMap.get(Servo.class,"catchEx");
    CatchClaw1 = hardwareMap.get(Servo.class, "catchClaw1");
    CatchClaw2 = hardwareMap.get(Servo.class, "catchClaw2");

    // DcMotors for Linear slide
    slideExtension = hardwareMap.get(DcMotor.class, "slideExtend");
    wrist1 = hardwareMap.get(Servo.class, "wrist1");
    slideAbduction = hardwareMap.get(DcMotor.class, "slideAbd");
    slideAbduction2 = hardwareMap.get(DcMotor.class, "slideAbd2");

    slideExtension.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    slideAbduction.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    slideAbduction2.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);



    // Takers
    leftIntake = hardwareMap.get(Servo.class, "l_intake");
    rightIntake = hardwareMap.get(Servo.class, "r_intake");
    sensor = hardwareMap.get(ColorSensor.class, "sensor");

    // MaybeIntake = hardwareMap.get(DcMotor.class, "intake");
    // Setting the direction for the motor on where to rotate

    // Orientation for drivetrain
    frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
    frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
    backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
    backRightMotor.setDirection(DcMotor.Direction.REVERSE);

    // intake
    boolean intakeReleased = true;

    //catch intake
    boolean CatchRelease = true;

    // linear slide
    slideExtension.setDirection(DcMotor.Direction.FORWARD);
    slideAbduction.setDirection(DcMotor.Direction.FORWARD);
    slideAbduction2.setDirection(DcMotor.Direction.REVERSE);

    // normalize motor positions
    double normalizedAbdPos1 = (double)slideAbduction.getCurrentPosition() / COUNTS_PER_MOTOR_REV; // TODO test

    // ENCODERS
    slideAbduction2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    slideAbduction2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    slideAbduction.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    slideAbduction.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
      double backLeftPower = power * sin / max + turn;
      double backRightPower = power * sin / max - turn;

      // Prevents the motors exceeding max power thus motors will not seize and act sporadically
      if ((power + Math.abs(turn)) > 1) {
        frontLeftPower /= power + turn;
        frontRightPower /= power - turn;
        backLeftPower /= power + turn;
        backRightPower /= power - turn;
      }

      if (gamepad2.a && intakeReleased) {
        intakePower = 1 - intakePower;
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
        slideAbduction.setTargetPosition(ABD_TO_RUNG);
        slideAbduction2.setTargetPosition(ABD_TO_RUNG);
      } else if (gamepad2.dpad_down && !runningPreset) {
        runningPreset = true;
        slideAbduction.setTargetPosition(ABD_DOWN);
        slideAbduction2.setTargetPosition(ABD_DOWN);
      } // if

      // STOP ALL PRESETS
      if (gamepad2.dpad_left) {
        runningPreset = false;
      }

      if (runningPreset && slideAbduction.getCurrentPosition() > slideAbduction.getTargetPosition() - 5 && slideAbduction.getCurrentPosition() < slideAbduction.getTargetPosition() + 5) {
        runningPreset = false;
      }

      // Power to the wheels
      frontLeftMotor.setPower(frontLeftPower);
      backLeftMotor.setPower(backLeftPower);
      frontRightMotor.setPower(frontRightPower);
      backRightMotor.setPower(backRightPower);

      // Power to the arm
      if(runningPreset) {
        // TODO FIX
        slideAbduction.setPower(0.5);
        slideAbduction2.setPower(0.5);
      } else {
        slideAbduction.setPower(-slideAbdPower * 0.65);
        slideAbduction2.setPower(-slideAbdPower * 0.65);
      } // if else

      slideExtension.setPower(slideExtendPower);

      // Wrist power
      wrist1.setPosition(wristPos);
      if (gamepad2.left_trigger > 0) {
        wristPos = Math.min(1, wristPos + 0.020);
      } else if (gamepad2.left_bumper) {
        wristPos = Math.max(0, wristPos - 0.020);
      }

      //catch power
      if (gamepad2.dpad_right && CatchRelease) {
        catchintake = 1 - catchintake;
        CatchRelease = false;
      }

      if(!gamepad2.dpad_right){
        CatchRelease = true;

      }

      // Power to the intake
      leftIntake.setPosition(intakePower);
      rightIntake.setPosition(1 - intakePower);

      // Telemetry
      telemetry.addData("RUNNING PRESET:", runningPreset);
      telemetry.addData("RUNMODE:", slideAbduction.getMode());
      telemetry.addData("Normalized Abd 1 position:", normalizedAbdPos1);
      telemetry.addData("Abd 1 position:", slideAbduction.getCurrentPosition());
      telemetry.addData("Abd 2 position:", slideAbduction2.getCurrentPosition());
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