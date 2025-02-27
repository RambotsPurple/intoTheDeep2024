package org.firstinspires.ftc.teamcode.Ops.AUTO;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.RR.SparkFunOTOSDrive;

@Config
@Autonomous(name = "Real Auton ", group = "Autonomous")
public class AutoRedSpeci extends LinearOpMode {

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

        Pose2d initialPose = new Pose2d(0, -60, Math.toRadians(90));
        SparkFunOTOSDrive drive = new SparkFunOTOSDrive(hardwareMap, initialPose);
        Claw claw = new Claw();
        // make a Lift instance
        Lift lift = new Lift();

//        @TODO trajectorty

        TrajectoryActionBuilder firstClip = drive.actionBuilder(initialPose)
                //start
                .lineToY(-34)
                //rotate
                .setTangent(Math.PI)
                .lineToX(33)
                //x
                .setTangent(Math.PI/2)
                .lineToY(-5)
                .setTangent(Math.PI)
                .lineToX(45)
                .waitSeconds(1)

                .setTangent(Math.PI/2)
                .lineToY(-40)
                .lineToY(-5)

                .setTangent(Math.PI)
                .lineToX(54)
                .waitSeconds(1)

                .setTangent(Math.PI/2)
                .lineToY(-40)
                .lineToY(-5)
                .waitSeconds(1)

                .setTangent(Math.PI)
                .waitSeconds(1)
                .lineToX(62)

                .setTangent(Math.PI/2)
                .lineToY(-40)

                .waitSeconds(1)

                .setTangent(Math.PI)
                .lineToX(40)
                .waitSeconds(1)

                .turn(Math.toRadians(90))

                .setTangent(Math.PI)  // Inverse of Math.PI
                .lineToX(-48)  // Inverse of 48
                .setTangent(Math.PI/-2)  // Inverse of Math.PI/2
                .lineToY(55)  // Inverse of -55
                .lineToY(5)  // Inverse of -5
                .setTangent(Math.PI)  // Inverse of Math.PI
                .lineToX(-54)  // Inverse of 54
                .setTangent(Math.PI/-2)  // Inverse of Math.PI/2
                .lineToY(55)  // Inverse of -55
                .lineToY(5)  // Inverse of -5
                .setTangent(Math.PI)  // Inverse of Math.PI
                .lineToX(-62)  // Inverse of 62
                .setTangent(Math.PI/-2)  // Inverse of Math.PI/2
                .lineToY(55)  // Inverse of -55
                .setTangent(Math.PI)  // Inverse of Math.PI
                .lineToX(-40)  // Inverse of 40
                .turn(Math.toRadians(-90))  // Inverse of 90°


// Clips
                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(34)  // Inverse of -34
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(55)  // Inverse of -55

                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(34)  // Inverse of -34
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(55)  // Inverse of -55

                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(34)  // Inverse of -34
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(55)  // Inverse of -55

                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(34)  // Inverse of -34
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(55)  // Inverse of -55

                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .lineToY(34)  // Inverse of -34
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .turn(Math.toRadians(-180))  // Inverse of 180°
                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
                .waitSeconds(3);

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