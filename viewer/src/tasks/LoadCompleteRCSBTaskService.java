package tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class LoadCompleteRCSBTaskService extends Service<List<String>> {

    @Override
    protected Task<List<String>> createTask(){
        return new LoadCompleteRCSBTask();
    }


}
