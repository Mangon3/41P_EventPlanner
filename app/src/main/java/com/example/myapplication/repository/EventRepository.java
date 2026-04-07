package com.example.myapplication.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.Event;
import com.example.myapplication.data.EventDao;

import java.util.List;

public class EventRepository {
    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;

    public EventRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEventsSorted();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public LiveData<Event> getEventById(int id) {
        return eventDao.getEventById(id);
    }

    public void insert(Event event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(event);
        });
    }

    public void update(Event event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(event);
        });
    }

    public void delete(Event event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(event);
        });
    }
}
