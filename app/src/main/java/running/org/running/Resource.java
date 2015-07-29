package running.org.running;

import java.util.ArrayList;

public abstract class Resource {
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void setState(Object context) {
        for (Observer observer : observers) {
            observer.update(context);
        }
    }

    public abstract void destroy();
}
