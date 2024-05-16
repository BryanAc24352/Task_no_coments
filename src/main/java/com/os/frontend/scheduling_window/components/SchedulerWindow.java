package com.os.frontend.scheduling_window.components;

import com.os.backend.Process.PriorityProcess;
import com.os.backend.Process.Process;
import com.os.backend.main.Backend;
import com.os.frontend.Main;
import com.os.frontend.scheduling_window.observers.Bar;
import com.os.frontend.scheduling_window.observers.GanttChart;
import com.os.frontend.scheduling_window.observers.Observer;
import com.os.frontend.start_window.ProcessBlockController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SchedulerWindow extends StackPane implements Initializable {
    public HBox ganttChartBox;
    public HBox barAndTableBox;
    public HBox addProcessBox;
    public VBox timerBox;
    public Label timeLabel;
    public ToggleButton startStopButton;
    public Label schedulerType;
    private Backend backend;
    private GanttChart ganttChart;
    private ProcessBlockController processBlockController;
    private final List<Observer> observers = new ArrayList<>(3);
    private int seconds;
    private Timeline timeline;
    private Main main;
    private boolean systemStopped = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ganttCharInit();
            tableInit();
            barChartInit();
            addProcessInit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ganttCharInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/os/frontend/GanttChartView.fxml"));
        Node ganttChartNode = fxmlLoader.load();
        ganttChartBox.getChildren().add(ganttChartNode);
       
        this.observers.add(fxmlLoader.getController());
        this.ganttChart = fxmlLoader.getController();
        this.ganttChart.setHvalue(1);

        VBox.setVgrow(ganttChartNode, javafx.scene.layout.Priority.ALWAYS);
    }

    private void barChartInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/os/frontend/Bar.fxml"));
        Node node = fxmlLoader.load();
        barAndTableBox.getChildren().add(node);
        node.getStyleClass().add("table-box");
       
        this.observers.add(fxmlLoader.getController());
        VBox.setVgrow(node, javafx.scene.layout.Priority.ALWAYS);
    }

    private void tableInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/os/frontend/ProcessesTableView.fxml"));
        Node node = fxmlLoader.load();
        barAndTableBox.getChildren().add(node);
        
        this.observers.add(fxmlLoader.getController());
        VBox.setVgrow(node, javafx.scene.layout.Priority.ALWAYS);

    }

    private void addProcessInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/os/frontend/ProcessBlockView.fxml"));
        this.addProcessBox.getChildren().add(2, fxmlLoader.load());
        this.processBlockController = fxmlLoader.getController();
    }

    private void timerInit() {
     
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event -> {
                            
                            timeLabel.setText(String.valueOf(seconds));
                            
                            this.processBlockController.setArrivalTime(seconds + 1);
                            
                            seconds++;
                        }
                ),
                new KeyFrame(Duration.seconds(1)) 
        );
        timeline.setCycleCount(Animation.INDEFINITE); 
        timeline.play();
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
        
        this.observers.forEach(backend::attach);
        
        if (this.backend.getProcessList().get(0) instanceof PriorityProcess) {
            
            this.processBlockController.showPriority();
        } else {
            this.processBlockController.hidePriority();
        }

        //set process list to the bar chart
        ((Bar) this.observers.get(2)).setProcessList(this.backend.getProcessList());

        //timer init
        timerInit();

        this.schedulerType.setText(this.backend.getScheduler().getSchedulerName());

    }

    public void addNewProcessToBackEnd(ActionEvent ignoredActionEvent) {
        boolean stoppedByUs = false;
        if (!systemStopped) {
            
            backend.pauseSystem();
        
            this.timeline.stop();
            stoppedByUs = true;
        }

        Process newProcess = this.processBlockController.createProcess();
        
        this.backend.addProcess(newProcess);
       
        this.processBlockController.incrementProcessNumber();

        if (stoppedByUs) {
            
            backend.continueSystem();
           
            this.timeline.play();
        }
    }

    public void resetProcessBlock(ActionEvent ignoredActionEvent) {
        
        this.processBlockController.reset();
    }


    public void startStop(ActionEvent ignoredActionEvent) {
        if (startStopButton.isSelected()) {
            startStopButton.setText("Resume");
            if (!systemStopped) {
                timeline.stop();
                backend.pauseSystem();
            }
            systemStopped = true;
        } else {
            startStopButton.setText("Stop");
            if (systemStopped) {
                timeline.play();
                backend.continueSystem();
            }
            systemStopped = false;
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void restartApp() {
        main.restart();
    }
}
