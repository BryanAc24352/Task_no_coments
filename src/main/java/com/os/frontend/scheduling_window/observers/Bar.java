package com.os.frontend.scheduling_window.observers;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessAtTime;
import com.os.backend.main.SystemScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.chart.BarChart;

public class Bar extends AnchorPane implements Observer, Initializable {

    public BarChart<String, Integer> barChart;
    private List<ProcessAtTime> processAtTimeList;

    @FXML
    public BarChart<String, Integer> bar;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private List<Process> processList;

    private ObservableList<String> categories;

    private int previousProcessListSize;

    @Override
    public void update(SystemScheduler systemScheduler) {


        if (previousProcessListSize < processList.size()) {
            if (previousProcessListSize == 0) {

                initializeChart();


                //previousProcessListSize = processList.size();
            } else {
                while (previousProcessListSize < processList.size()) {
                    updateChartWithNewProcess(processList.get(previousProcessListSize));
                    previousProcessListSize++;
                }
            }
            previousProcessListSize = processList.size();

        }



        Process currentRunningProcess = systemScheduler.getCurrentRunningProcess();
   
        int index = processList.indexOf(currentRunningProcess);

        if (index != -1) {
            XYChart.Series<String, Integer> seriesToUpdate = barChart.getData().get(index);
            XYChart.Data<String, Integer> dataToUpdate = seriesToUpdate.getData().get(seriesToUpdate.getData().size() - 1);
            
            System.out.println(dataToUpdate.getYValue());

           
            if (currentRunningProcess.getRemainingTime() >= 0) {
                dataToUpdate.setYValue(currentRunningProcess.getRemainingTime());
            }

            System.out.println("APOLO");
        }
        else System.out.println("KALOLO");
    }

    private void updateChartWithNewProcess(Process process) {
      
        xAxis.getCategories().add("P" + (previousProcessListSize + 1));

        
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("P" + (previousProcessListSize + 1)); 
        series.getData().add(new XYChart.Data<>(series.getName(), process.getRemainingTime()));
        


        
        barChart.getData().add(series);


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void initializeChart() {
        xAxis.setLabel("Processes");
        yAxis.setLabel("Remaining Time");

        
        categories = FXCollections.observableArrayList();

        
        for (Process process : processList) {
            categories.add("P" + (processList.indexOf(process) + 1));
        }

        
        xAxis.setCategories(categories);

       
        for (Process process : processList) {
            int seriesIndex = processList.indexOf(process);
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("P" + (seriesIndex + 1)); 
            series.getData().add(new XYChart.Data<>(series.getName(), process.getRemainingTime()));



            barChart.getData().add(series);
        }
    }



    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }
}