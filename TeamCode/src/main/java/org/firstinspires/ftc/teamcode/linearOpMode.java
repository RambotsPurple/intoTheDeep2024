package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "RambotsPurpleTeleOp")
public class linearOpMode extends LinearOpMode {

    private double intakePower = .7;
    final private int ABD_TO_RUNG = 100;
    final private int ABD_DOWN = 0;
    private boolean runningPreset = false;

    // wrist
    private double wristPos = 0;
    private double wrist2Pos = 0;
    private double targetDir = 0;
    double dir;
    int targetPos = 0;

    @Override
    public void runOpMode() {

        // initializing hardware
        RobotConfig.initialize(hardwareMap);

        waitForStart();

        boolean intakeReleased = true;

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            dir = RobotConfig.getAngle();

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
            if (gamepad2.right_stick_y != 0) {
                RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                targetPos = RobotConfig.arm1.getCurrentPosition();
                RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                targetPos = RobotConfig.arm2.getCurrentPosition();
            } else {
                RobotConfig.arm1.setTargetPosition(targetPos);
                RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RobotConfig.arm1.setVelocity(1000);
                RobotConfig.arm2.setTargetPosition(targetPos);
                RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RobotConfig.arm2.setVelocity(1000);
            }

            if (gamepad1.right_stick_x != 0) {
                targetDir = dir ; // TODO tune
            }

            double turn = (dir - targetDir) / 30 + gamepad1.right_stick_x;
            turn = Math.min(1, turn);
            turn = Math.max(-1, turn);

            RobotConfig.drive(gamepad1.left_stick_x * 1.1, -gamepad1.left_stick_y, turn, dir); // no dir correction

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

            if (!gamepad2.a && !gamepad2.b && !gamepad2.y) {
                intakeReleased = true;
            }

            // presets
            if (gamepad2.dpad_up && !runningPreset) {
                runningPreset = true;
                RobotConfig.arm1.setTargetPosition(ABD_TO_RUNG);
                RobotConfig.arm2.setTargetPosition(ABD_TO_RUNG);

            } else if (gamepad2.dpad_down && !runningPreset) {
                runningPreset = true;
                RobotConfig.arm1.setTargetPosition(ABD_DOWN);
                RobotConfig.arm2.setTargetPosition(ABD_DOWN);

            } // if

            // STOP ALL PRESETS
            if (gamepad2.dpad_left) {
                runningPreset = false;
            }

            if (runningPreset && RobotConfig.arm1.getCurrentPosition() > RobotConfig.arm1.getTargetPosition() - 5 && RobotConfig.arm1.getCurrentPosition() < RobotConfig.arm1.getTargetPosition() + 5) {
                runningPreset = false;
            }

            // Power to the arm
            if (runningPreset) {
                // TODO FIX
                RobotConfig.arm1.setPower(0.5);
                RobotConfig.arm2.setPower(0.5);

            } else {
                RobotConfig.arm1.setPower(slideAbdPower);
                RobotConfig.arm2.setPower(slideAbdPower);

            } // if else

            RobotConfig.slideExtension.setPower(slideExtendPower);

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

            RobotConfig.wrist1.setPosition(wristPos);


            // Power to the intake
            RobotConfig.intake.setPosition(intakePower);

            // Telemetry
            telemetry.addData("RUNNING PRESET:", runningPreset);
            telemetry.addData("RUNMODE:", RobotConfig.arm1.getMode());
            telemetry.addData("Abd 1 position:", RobotConfig.arm1.getCurrentPosition());
            telemetry.addData("intake pos BETTER VERSION:", RobotConfig.intake.getPosition());

            telemetry.addData("Ext position:", RobotConfig.slideExtension.getCurrentPosition());
            telemetry.addData("wrist pos:", RobotConfig.wrist1);

            telemetry.addData("Intake pos (right is inverse): ", intakePower);
            telemetry.addData("Slide extension power: ", slideExtendPower);
            telemetry.addData("Slide abduction power: ", slideAbdPower);
            telemetry.addData("Target abduction pos: ", targetPos);
            telemetry.addData("Slide abduction pos: ", RobotConfig.arm1.getCurrentPosition());
            telemetry.addData("Slide extension pos: ", RobotConfig.slideExtension.getCurrentPosition());

            telemetry.addData("heading", dir);

            telemetry.addData("Wrist pos: ", wristPos);
            telemetry.update();

        }
    }

}