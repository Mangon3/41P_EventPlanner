package com.example.myapplication.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.example.myapplication.data.Event;
import com.example.myapplication.databinding.FragmentAddEditEventBinding;
import com.example.myapplication.util.EventValidator;
import com.example.myapplication.viewmodel.EventViewModel;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditEventFragment extends Fragment {
    private FragmentAddEditEventBinding binding;
    private EventViewModel eventViewModel;
    private Calendar calendar = Calendar.getInstance();
    private int eventId = -1;
    private Event currentEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEditEventBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        String[] categories = new String[] { "Work", "Social", "Travel", "Other" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                categories);
        binding.tvCategory.setAdapter(adapter);

        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId", -1);
        }

        if (eventId != -1) {
            binding.btnDelete.setVisibility(View.VISIBLE);
            binding.btnDelete.setOnClickListener(v -> deleteEvent(view));

            eventViewModel.getEventById(eventId).observe(getViewLifecycleOwner(), event -> {
                if (event != null && currentEvent == null) {
                    currentEvent = event;
                    binding.etTitle.setText(event.getTitle());
                    binding.tvCategory.setText(event.getCategory(), false);
                    binding.etLocation.setText(event.getLocation());
                    calendar.setTimeInMillis(event.getDateTime());
                    updateDateTimeText();
                }
            });
        }

        binding.btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTimeText();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.btnPickTime.setOnClickListener(v -> {
            new TimePickerDialog(requireContext(), (view12, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateDateTimeText();
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        binding.btnSave.setOnClickListener(v -> saveEvent(view));
    }

    private void updateDateTimeText() {
        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault());
        binding.tvSelectedDateTime.setText(format.format(calendar.getTime()));
    }

    private void saveEvent(View view) {
        String title = binding.etTitle.getText() == null ? "" : binding.etTitle.getText().toString();
        String category = binding.tvCategory.getText().toString();
        String location = binding.etLocation.getText() == null ? "" : binding.etLocation.getText().toString();
        long dateTime = calendar.getTimeInMillis();

        if (!EventValidator.isValid(title, dateTime)) {
            Snackbar.make(view, "Invalid Input: Title required & date must be in future", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (currentEvent != null) {
            currentEvent.setTitle(title);
            currentEvent.setCategory(category);
            currentEvent.setLocation(location);
            currentEvent.setDateTime(dateTime);
            eventViewModel.update(currentEvent);
        } else {
            Event newEvent = new Event(title, category, location, dateTime);
            eventViewModel.insert(newEvent);
        }

        Navigation.findNavController(view).popBackStack();
    }

    private void deleteEvent(View view) {
        if (currentEvent != null) {
            eventViewModel.delete(currentEvent);
            Navigation.findNavController(view).popBackStack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
