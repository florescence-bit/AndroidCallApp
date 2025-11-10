package com.adivya.myapplication;

import android.net.Uri;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecentCallsAdapter extends RecyclerView.Adapter<RecentCallsAdapter.RecentCallViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CallLogEntry item);
    }

    private final List<CallLogEntry> callLogEntries;
    private final OnItemClickListener listener;

    public RecentCallsAdapter(List<CallLogEntry> callLogEntries, OnItemClickListener listener) {
        this.callLogEntries = callLogEntries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentCallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_call_item, parent, false);
        return new RecentCallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentCallViewHolder holder, int position) {
        CallLogEntry entry = callLogEntries.get(position);
        holder.callerNameTextView.setText(entry.getName());
        String callInfo;
        switch (entry.getType()) {
            case CallLog.Calls.INCOMING_TYPE:
                callInfo = "Incoming call ";
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                callInfo = "Outgoing call ";
                break;
            case CallLog.Calls.MISSED_TYPE:
                callInfo = "Missed call ";
                break;
            default:
                callInfo = "Call ";
        }
        callInfo += DateFormat.format("hh:mm", entry.getTime());
        holder.callInfoTextView.setText(callInfo);

        if (entry.getPhotoUri() != null) {
            holder.contactInitial.setVisibility(View.GONE);
            holder.contactPhoto.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(Uri.parse(entry.getPhotoUri())).into(holder.contactPhoto);
        } else {
            holder.contactPhoto.setVisibility(View.GONE);
            holder.contactInitial.setVisibility(View.VISIBLE);
            if (entry.getName() != null && !entry.getName().isEmpty()) {
                holder.contactInitial.setText(String.valueOf(entry.getName().charAt(0)));
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(entry));
    }

    @Override
    public int getItemCount() {
        return callLogEntries.size();
    }

    static class RecentCallViewHolder extends RecyclerView.ViewHolder {
        ImageView contactPhoto;
        TextView contactInitial;
        TextView callerNameTextView;
        TextView callInfoTextView;
        ImageView callButton;

        public RecentCallViewHolder(@NonNull View itemView) {
            super(itemView);
            contactPhoto = itemView.findViewById(R.id.contact_photo);
            contactInitial = itemView.findViewById(R.id.contact_initial);
            callerNameTextView = itemView.findViewById(R.id.caller_name_text_view);
            callInfoTextView = itemView.findViewById(R.id.call_info_text_view);
            callButton = itemView.findViewById(R.id.call_button);
        }
    }
}
