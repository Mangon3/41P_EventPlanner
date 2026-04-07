package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.data.Event;
import com.example.myapplication.databinding.ItemEventBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventAdapter extends ListAdapter<Event, EventAdapter.EventHolder> {

    private OnItemClickListener listener;
    private SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault());

    public EventAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK = new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                   oldItem.getDateTime() == newItem.getDateTime();
        }
    };

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding binding = ItemEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        Event currentEvent = getItem(position);
        holder.binding.tvTitle.setText(currentEvent.getTitle());
        holder.binding.tvCategory.setText(currentEvent.getCategory());
        holder.binding.tvLocation.setText(currentEvent.getLocation());
        holder.binding.tvDateTime.setText(format.format(new Date(currentEvent.getDateTime())));
    }
    
    public Event getEventAt(int position) {
        return getItem(position);
    }

    class EventHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;

        public EventHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
