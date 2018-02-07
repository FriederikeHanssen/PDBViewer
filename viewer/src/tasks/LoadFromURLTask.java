package tasks;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class LoadFromURLTask extends Task<List<String>> {

    private final String url;

    public LoadFromURLTask(String url){
        this.url = url;
    }

    @Override
    public List<String> call() throws IOException{
        final HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        final List<String> lines = new ArrayList<>();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader((connection.getInputStream())))) {
            String line;
            while ((line = rd.readLine()) != null) {
                lines.add(line);
            }
        }catch(Exception exp){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "RCSB could not be reached: \n" + exp.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        return lines;
    }
}
