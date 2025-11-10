package com.adivya.myapplication;

import android.provider.CallLog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CallLogEntry item);
    }

    private final List<CallLogEntry> callLogEntries;
    private final OnItemClickListener listener;

    public CallLogAdapter(List<CallLogEntry> callLogEntries, OnItemClickListener listener) {
        this.callLogEntries = callLogEntries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_item, parent, false);
        return new CallLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
        CallLogEntry entry = callLogEntries.get(position);
        holder.callerNameTextView.setText(entry.getName());
        holder.callerNumberTextView.setText(entry.getNumber());
        holder.callTimeTextView.setText(DateUtils.getRelativeTimeSpanString(entry.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));

        switch (entry.getType()) {
            case CallLog.Calls.INCOMING_TYPE:
                holder.callTypeIcon.setImageResource(R.drawable.ic_call_received);
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                holder.callTypeIcon.setImageResource(R.drawable.ic_call_made);
                break;
            case CallLog.Calls.MISSED_TYPE:
                holder.callTypeIcon.setImageResource(R.drawable.ic_call_missed);
                break;
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(entry));
    }

    @Override
    public int getItemCount() {
        return callLogEntries.size();
    }

    static class CallLogViewHolder extends RecyclerView.ViewHolder {
        ImageView callTypeIcon;
        TextView callerNameTextView;
        TextView callerNumberTextView;
        TextView callTimeTextView;

        public CallLogViewHolder(@NonNull View itemView) {
            super(itemView);
            callTypeIcon = itemView.findViewById(R.id.call_type_icon);
            callerNameTextView = itemView.findViewById(R.id.caller_name_text_view);
            callerNumberTextView = itemView.findViewById(R.id.caller_number_text_view);
            callTimeTextView = itemView.findViewById(R.id.call_time_text_view);
        }
    }
}
