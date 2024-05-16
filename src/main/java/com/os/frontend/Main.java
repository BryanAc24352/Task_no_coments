package com.os.frontend;

import com.os.backend.main.Backend;
import com.os.frontend.scheduling_window.components.SchedulerWindow;
import com.os.frontend.start_window.StartWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private Backend backend;
    private Stage stage;
    private static String[] args;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StartWindowView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Scheduler");
        this.stage = stage;
        stage.setScene(scene);
        setIcon();
        stage.show();
        ((StartWindowController)    fxmlLoader.getController()).setMain(this);
    }

    private void setIcon() {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/processor.png")));
        this.stage.getIcons().add(icon);
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public void moveToSchedulerView() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/os/frontend/schedulerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1200,750);

        SchedulerWindow schedulerWindowController = fxmlLoader.getController();
     
        schedulerWindowController.setBackend(backend);
        schedulerWindowController.setMain(this);
     
        this.stage.setScene(scene);
  
        this.backend.startSchedule();

    }
    public static void main(String[] args) {
        Main.args = args;
        launch();
    }

    public Backend backend() {
        return backend;
    }

    public void restart() {
        this.stage.close();

        this.backend = null;
        this.stage = null;
        System.gc();

        try {
            Main newMain = new Main();
            newMain.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}