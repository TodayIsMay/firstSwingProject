package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * calls method for updating symbols ones a second
 */
public class UpdateScheduler extends TimerTask {
    List<UpdateListener> listeners = new ArrayList<>();

    public UpdateScheduler() {
        run();
    }

    public void addListener(UpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void run() {
        for(UpdateListener listener: listeners) {
            listener.update();
        }
    }
}