package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.Event;
import com.example.myapplication.databinding.FragmentEventListBinding;
import com.example.myapplication.viewmodel.EventViewModel;
import com.google.android.material.snackbar.Snackbar;

public class EventListFragment extends Fragment {
    private FragmentEventListBinding binding;
    private EventViewModel eventViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);

        EventAdapter adapter = new EventAdapter();
        binding.recyclerView.setAdapter(adapter);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.submitList(events);
        });

        binding.fabAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_eventList_to_addEditEvent);
        });

        adapter.setOnItemClickListener(event -> {
            Bundle bundle = new Bundle();
            bundle.putInt("eventId", event.getId());
            Navigation.findNavController(view).navigate(R.id.action_eventList_to_addEditEvent, bundle);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Event eventToDelete = adapter.getEventAt(viewHolder.getAdapterPosition());
                eventViewModel.delete(eventToDelete);
                Snackbar.make(view, "Event deleted", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
