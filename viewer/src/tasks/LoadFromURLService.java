package tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;

public class LoadFromURLService extends Service<List<String>> {

    private String url = " ";

    @Override
    protected Task<List<String>> createTask(){
        return new LoadFromURLTask(url);
    }

    public void restart(String url){
        this.url = url;
        super.restart();
    }

}
