package com.os.backend.main;

import com.os.backend.Process.*;
import com.os.backend.Process.Process;
import com.os.backend.Schedule.FCFS;
import com.os.frontend.scheduling_window.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class SystemScheduler{


    public static void main(String[] args) {
        Backend backend = new Backend();
        backend.setAlgo(new FCFS());

        List<Process> pList = new ArrayList<>();
        pList.add(new Process(1,0,3));
        pList.add(new Process(2,2,2));
        pList.add(new Process(3,3,4));
        backend.updateProcessesList(pList);
        backend.startSchedule();

    }

    private final Backend backend;
    private final List<Observer> observersList = new ArrayList<>(4);
    private Process currentRunningProcess;
    private List<ProcessAtTime> processesAtTime;
    private int time;
    public SystemScheduler(Backend backend) {
        this.backend = backend;
        time = 0;
    }

    public void attach(Observer observer) {
        observersList.add(observer);
    }

    public void notifyObservers() {
        observersList.forEach(observer -> observer.update(this));
    }

    public void updateTimeForProcess(Process process, int time){

        if(process.getArrivalTime() < time && process.getRemainingTime() != 0){
          
            process.setWaitingTime(process.getWaitingTime() + 1);
            process.setTurnaroundTime(process.getWaitingTime() + (process.getBurstTime() - process.getRemainingTime()));
        }


    }

    public Process getCurrentProcess(ProcessTable pTable,int time){
  
        List<ProcessExecutionEvent> pExecEventList = pTable.getProcessesList(time);
     
        for(ProcessExecutionEvent e: pExecEventList){
            if( e.getState() == ProcessState.RUNNING || e.getState() == ProcessState.STARTED) {
 
                for(Process p : backend.getProcessList()){
                    if (p.getProcessNumber() == e.getProcessNumber()) {
                        p.decrementRemainingTime();
                        p.setTurnaroundTime(p.getBurstTime()- p.getRemainingTime() + p.getWaitingTime());
                        continue;
                    }
                    updateTimeForProcess(p, time);
                }
              
                return backend.getProcessList().get(e.getProcessNumber()-1);
            }
        }
        return null;
    }

    public List<ProcessAtTime> getCurrentProcessesTable(){
        List<Process> processList = backend.getProcessList();
        List<ProcessAtTime> result = new ArrayList<>(processList.size());
        for(Process process : processList){
            result.add(new ProcessAtTime(process, time, backend.getTable()));
        }
        time++;
        return result;
    }

    public Process getCurrentRunningProcess() {
        return currentRunningProcess;
    }

    public List<ProcessAtTime> getProcessesAtTime() {
        return processesAtTime;
    }

    public void setCurrentRunningProcess(Process currentRunningProcess) {
        this.currentRunningProcess = currentRunningProcess;
    }

    public void setProcessesAtTime(List<ProcessAtTime> processesAtTime) {
        this.processesAtTime = processesAtTime;
    }
}
