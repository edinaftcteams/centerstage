package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import edu.edina.library.subsystems.MecanumDrive;
import edu.edina.library.subsystems.Subsystem;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Robot {
    private ExecutorService subsystemUpdateExecutor;
    public boolean Started;
    private boolean runMultiThreaded;
    public RobotState RobotState;
    public RobotHardware RobotHardware;
    private List<Subsystem> subsystems;
    private Telemetry telemetry;
    public MecanumDrive MecanumDrive;

    private Runnable subsystemUpdateRunnable = () -> {
        while (!Thread.currentThread().isInterrupted()) {
            internal_update();
        }
    };

    public Robot(Telemetry telemetry, HardwareMap map, boolean runMultiThreaded) {
        this.RobotState = new RobotState(this);
        this.telemetry = telemetry;
        this.runMultiThreaded = runMultiThreaded;

        RobotHardware = new RobotHardware(map, this);

        subsystems = new ArrayList<>();

        this.MecanumDrive = new MecanumDrive(map, this);

        subsystems.add(this.MecanumDrive);
        
        if (this.runMultiThreaded) {
            // setup the thread executor
            subsystemUpdateExecutor = ThreadPool.newSingleThreadExecutor("subsystem update");
        }
    }

    public void init() {
        for (Subsystem subsystem : subsystems) {
            subsystem.init();
        }
    }

    public void update() {
        // check to see if running update on different thread
        if (!runMultiThreaded) {
            // we are not so run it here
            internal_update();
        }
    }

    private void internal_update() {
        for (Subsystem subsystem : subsystems) {
            subsystem.update();
        }
    }

    public void start() {
        if (runMultiThreaded){
            if (!Started) {
                subsystemUpdateExecutor.submit(subsystemUpdateRunnable);
            }
        }

        Started = true;
    }

    public void stop() {
        if (runMultiThreaded){
            if (subsystemUpdateExecutor != null) {
                subsystemUpdateExecutor.shutdownNow();
                subsystemUpdateExecutor = null;
            }
        }

        Started = false;
    }

    public void telemetry() {
        RobotState.telemetry(this.telemetry);
        telemetry.update();
    }
}
