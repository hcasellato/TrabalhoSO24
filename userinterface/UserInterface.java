package userinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.ControlInterface;
import interfaces.NotificationInterface;
import interfaces.SubmissionInterface;

public class UserInterface extends JFrame implements NotificationInterface, Runnable {

    private ControlInterface controlInterface;
    private SubmissionInterface submissionInterface;
    private JTextArea displayArea;
    private JTextField programInput;

    public UserInterface(ControlInterface controlInterface, SubmissionInterface submissionInterface) {
        this.controlInterface = controlInterface;
        this.submissionInterface = submissionInterface;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Process Scheduler Simulation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlInterface.startSimulation();
            }
        });

        JButton suspendButton = new JButton("Suspend");
        suspendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlInterface.suspendSimulation();
            }
        });

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlInterface.resumeSimulation();
            }
        });

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlInterface.stopSimulation();
            }
        });

        JButton displayQueuesButton = new JButton("Display Queues");
        displayQueuesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlInterface.displayProcessQueues();
            }
        });

        buttonsPanel.add(startButton);
        buttonsPanel.add(suspendButton);
        buttonsPanel.add(resumeButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(displayQueuesButton);

        controlPanel.add(buttonsPanel);

        JPanel programPanel = new JPanel();
        programPanel.setLayout(new FlowLayout());

        programInput = new JTextField(30);
        JButton submitButton = new JButton("Submit Program");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String programPath = programInput.getText();
                if (programPath != null && !programPath.isEmpty()) {
                    boolean success = submissionInterface.submitJob(programPath);
                    if (success) {
                        display("Program submitted: " + programPath);
                    } else {
                        display("Failed to submit program: " + programPath);
                    }
                }
            }
        });

        programPanel.add(new JLabel("Program Path:"));
        programPanel.add(programInput);
        programPanel.add(submitButton);

        controlPanel.add(programPanel);

        add(controlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void display(String info) {
        displayArea.append(info + "\n");
    }

    @Override
    public void run() {
        setVisible(true);
    }
}

