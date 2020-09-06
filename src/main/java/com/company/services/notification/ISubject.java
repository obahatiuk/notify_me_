package com.company.services.notification;

public interface ISubject {
    void register(IObserver observer);
    void unregister(IObserver observer);
    void notifyObservers();
}
