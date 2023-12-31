package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import edu.edina.library.util.SmartGamepad;

public class TeleopOpMode extends OpMode {
    protected Robot robot;
    protected SmartGamepad _gamepad1;
    protected SmartGamepad _gamepad2;

    @Override
    public void init() {
        _gamepad1 = new SmartGamepad(gamepad1);
        _gamepad2 = new SmartGamepad(gamepad2);
    }

    @Override
    public void init_loop() {
        // hit after init is called and before play
        // great place to put vision code to detect where to go for autonomous
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {

        _gamepad1.update();
        _gamepad2.update();

        robot.MecanumDrive.setDriveProperties(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        robot.update();
        robot.telemetry();
    }

    @Override
    public  void stop() {
        robot.stop();
    }
}
