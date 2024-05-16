package com.os.frontend;

import com.os.backend.Process.PriorityProcess;
import com.os.backend.Process.Process;
import com.os.frontend.Colors.Colors;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class SchedulerViewController implements Initializable {


    public ScrollPane scrollPane;
    @FXML
    private TableView<Process> processTable;
    @FXML
    private TableColumn<Process, Integer> pidColumn;
    @FXML
    private TableColumn<Process, Integer> arrivalColumn;
    @FXML
    private TableColumn<Process, Integer> burstColumn;
    @FXML
    private TableColumn<Process, Integer> priorityColumn;
    @FXML
    private TableColumn<Process, Integer> remainingColumn;

    @FXML
    private Label CPUScheduler;
    @FXML
    private BarChart<String, Integer> barChart;
    XYChart.Series<String, Integer> processSeries = new XYChart.Series<>();
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private int scrolViewChange;


    public HBox ganttBox;
    public Button addButton;
    public TextField arrivalField;
    public TextField priorityField;
    public TextField burstField;
    public Button stopButton;
    public Button startButton;


    ObservableList<Process> processList = FXCollections.observableArrayList(
            new Process(0, 5, 6),
            new Process(0, 30, 28),
            new Process(0, 1, 7)

    );


    private Map<Integer, String> seriesColors = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTable();
        initializeChart();
        scrollPane.setHvalue(0);


    }

    public void initializeTable() {
        schedularViewAnimation();
       
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("arrivalTime"));
        burstColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("burstTime"));

        processTable.setItems(processList);
    }


    private void initializeChart() {
        xAxis.setLabel("Processes");
        yAxis.setLabel("Burst Time");
        String labelStyle = "-fx-text-fill: #76ABAE;";
        xAxis.setStyle(labelStyle);
        yAxis.setStyle(labelStyle);
        
        ObservableList<String> categories = FXCollections.observableArrayList();

       
        for (Process process : processList) {
            categories.add("P" + (processList.indexOf(process) + 1));
        }

        
        xAxis.setCategories(categories);

        for (Process process : processList) {
            int seriesIndex = processList.indexOf(process);
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("P" + (seriesIndex + 1)); 
            series.getData().add(new XYChart.Data<>(series.getName(), process.getBurstTime()));


            setBarColorForSeries(series, seriesIndex);


            barChart.getData().add(series);
        }
    }


    private void setBarColorForSeries(XYChart.Series<String, Integer> series, int seriesIndex) {
    }


    private void customizeLegend() {
        AnchorPane legend = (AnchorPane) barChart.lookup(".chart-legend");
        ObservableList<javafx.scene.Node> legendItems = legend.getChildren();

        for (javafx.scene.Node legendItem : legendItems) {
            if (legendItem instanceof Label) {
                Label label = (Label) legendItem;
                int seriesIndex = legendItems.indexOf(legendItem);
                System.out.println(Colors.getColor(seriesIndex));
                Rectangle rectangle = new Rectangle(10, 10, Color.web(Colors.getColor(seriesIndex)));
                label.setGraphic(rectangle);
            }
        }
    }


    public void addNewProcess(ActionEvent actionEvent) {
        PriorityProcess process = new PriorityProcess(0, parseInt(arrivalField.getText()),
                parseInt(burstField.getText()),
                priorityField.getText() == null ? parseInt(priorityField.getText()) : 0);

        processList.add(process);
        arrivalField.setText("");
        burstField.setText("");
        priorityField.setText("");

        updateChartWithNewProcess(process);
    }

    private void updateChartWithNewProcess(Process process) {
 
        xAxis.getCategories().add("P" + (processList.size()));

      
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("P" + (processList.size())); 
        series.getData().add(new XYChart.Data<>(series.getName(), process.getBurstTime()));
        setBarColorForSeries(series, processList.indexOf(process));


     
        barChart.getData().add(series);

    }


    public void executeTest(ActionEvent actionEvent) {
        
        int selectedIndex = processTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) { 
            executeProcess(selectedIndex);
        }
    }

    private void executeProcess(int index) {
        Process process = processList.get(index);
        if (process.getBurstTime() > 0) {
            process.setBurstTime(process.getBurstTime() - 1);
            System.out.println(process.getBurstTime());
            updateChart(index, process);
            updateTable(index, process);
            ganttChartUpdate(process);

        }
    }

    private void updateChart(int index, Process process) {
        XYChart.Series<String, Integer> series = barChart.getData().get(index);

      
        XYChart.Data<String, Integer> lastDataPoint = series.getData().get(series.getData().size() - 1);
        lastDataPoint.setYValue(process.getBurstTime());

        setBarColorForSeries(series, processList.indexOf(process));


    }

    private void updateTable(int index, Process process) {

        Process updatedProcess = processTable.getItems().get(index);


        updatedProcess.setBurstTime(process.getBurstTime());


        processTable.refresh();

        processTable.getSelectionModel().select(updatedProcess);

    }

    private void ganttChartUpdate(Process process) {
        
        StackPane box = new StackPane();
        box.setPrefHeight(75);
        box.setPrefWidth(50);
        String colorStyle = Colors.getColor(processList.indexOf(process));
        box.setStyle("-fx-background-color:" + colorStyle); 
        
        Label label = new Label("P" + (processList.indexOf(process) + 1));
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold");
        box.getChildren().add(label);


        ganttBox.getChildren().add(box);


        ganttBox.setPrefWidth(ganttBox.getPrefWidth() + 9);
        double viewValue = (double) (ganttBox.getChildren().size() * 75 + 250) / 1060;


        scrolViewChange();

    }

    private void scrolViewChange() {
        scrolViewChange++;
        if (scrolViewChange > 19) {
            System.out.println(scrollPane.getHvalue());
            scrollPane.setHvalue(1);
        }

    }

    private String getDefaultSeriesColor(Process process) {
      
        int seriesIndex = processList.indexOf(process);

       
        if (seriesIndex >= 0 && seriesIndex < barChart.getData().size()) {
          
            String seriesStyleClass = "default-color" + seriesIndex;

            
            for (String styleClass : barChart.getStyleClass()) {
                
                if (styleClass.contains(seriesStyleClass)) {
                   
                    int colorIndex = styleClass.lastIndexOf("-");
                    return styleClass.substring(colorIndex + 1);
                }
            }
        }

        return null; 
    }

    private void schedularViewAnimation() {
        TranslateTransition moveInTransition = new TranslateTransition(Duration.seconds(1), CPUScheduler);
        moveInTransition.setFromY(0); 
        moveInTransition.setToY(-20); 
        
        TranslateTransition moveOutTransition = new TranslateTransition(Duration.seconds(1), CPUScheduler);
        moveOutTransition.setFromY(-20); 
        moveOutTransition.setToY(0); 
        moveInTransition.play();
    }

}
