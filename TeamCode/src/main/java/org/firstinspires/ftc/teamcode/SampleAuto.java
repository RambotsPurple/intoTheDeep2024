package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@Autonomous(name = "Sample ", group = "Autonomous")
public class SampleAuto extends LinearOpMode {

    int targetPos = 0;

    public class Lift {

        // component
        public Lift() {
            RobotConfig.arm1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            RobotConfig.arm1.setDirection(DcMotorSimple.Direction.FORWARD);

            RobotConfig.arm2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            RobotConfig.arm2.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        // raises lift
        public class LiftUp implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    targetPos = RobotConfig.ABD_SPEC;
                    RobotConfig.arm1.setTargetPosition(targetPos);
                    RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm1.setVelocity(1000);
                    RobotConfig.arm2.setTargetPosition(targetPos);
                    RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm2.setVelocity(1000);
                    initialized = true;
                }

                double pos = RobotConfig.arm1.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos < RobotConfig.ABD_SPEC) {
                    return true;
                } else {
                    targetPos = 0;
                    RobotConfig.arm1.setPower(targetPos);
                    RobotConfig.arm2.setPower(targetPos);
                    return false;
                }
            }
        }

        // function for raising lift
        public Action liftUp() {
            return new LiftUp();
        }


        // pickup
        public class LiftDown implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    targetPos = RobotConfig.ABD_PICKUP;
                    RobotConfig.arm1.setTargetPosition(targetPos);
                    RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm1.setVelocity(1000);
                    RobotConfig.arm2.setTargetPosition(targetPos);
                    RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm2.setVelocity(1000);
                    initialized = true;
                }

                double pos = RobotConfig.arm1.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos > RobotConfig.ABD_PICKUP) {
                    return true;
                } else {
                    targetPos = 0;
                    RobotConfig.arm1.setPower(targetPos);
                    RobotConfig.arm2.setPower(targetPos);
                    return false;
                }
            }
        }

        // lower lift
        public Action liftDown(){
            return new LiftDown();
        }
    }

    // Claw component
    public class Claw {


        public class CloseClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(0.55);
                return false;
            }
        }
        public Action closeClaw() {
            return new CloseClaw();
        }

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(1.0);
                return false;
            }
        }
        public Action openClaw() {
            return new OpenClaw();
        }
    }

    @Override
    public void runOpMode() {
        RobotConfig.initialize(hardwareMap);

        ParallelAction moveAndTurn = new ParallelAction(

        );

        Pose2d initialPose = new Pose2d(0, 60, Math.toRadians(90));
        SparkFunOTOSDrive drive = new SparkFunOTOSDrive(hardwareMap, initialPose);
        Claw claw = new Claw();
        // make a Lift instance
        Lift lift = new Lift();

//        @TODO trajectorty

        TrajectoryActionBuilder firstClip = drive.actionBuilder(initialPose)
                .setTangent(Math.PI)
                .lineToX(50)
                .setTangent(Math.PI/2)
                .lineToY(40)
                .waitSeconds(2)

                .setTangent(Math.PI)
                .lineToX(56)
                .waitSeconds(2)

                .lineToX(62)

                .setTangent(Math.PI/2)
                .lineToY(50)
                .setTangent(Math.PI)
                .lineToX(-50)
                .waitSeconds(3);
                //start

        Action trajectoryActionCloseOut = firstClip.endTrajectory().fresh()
                .lineToY(-5)
                .build();

        // actions that need to happen on init; for instance, a claw tightening.
        Actions.runBlocking(claw.closeClaw());




        waitForStart();

        if (isStopRequested()) return;

        Action trajectoryActionChosen;


        trajectoryActionChosen = firstClip.build();
        Actions.runBlocking(
                new SequentialAction(
                        trajectoryActionChosen,
                        lift.liftUp(),
                        claw.openClaw(),
                        lift.liftDown(),
                        trajectoryActionCloseOut
                )
        );
    }
}