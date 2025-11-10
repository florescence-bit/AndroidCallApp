package com.adivya.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment implements CallLogAdapter.OnItemClickListener {

    private static final int READ_CALL_LOG_PERMISSION_REQUEST_CODE = 1001;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1002;

    private RecyclerView callLogRecyclerView;
    private CallLogAdapter callLogAdapter;
    private CallLogEntry entryToCall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calls, container, false);

        callLogRecyclerView = view.findViewById(R.id.call_log_recycler_view);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG_PERMISSION_REQUEST_CODE);
        } else {
            loadCallLog();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CALL_LOG_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCallLog();
            } else {
                Toast.makeText(requireContext(), "Read Call Log permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(entryToCall);
            } else {
                Toast.makeText(requireContext(), "Call Phone permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadCallLog() {
        List<CallLogEntry> callLogList = new ArrayList<>();
        Cursor cursor = requireActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

        if (cursor != null) {
            int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int photoUriIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI);

            while (cursor.moveToNext()) {
                String number = cursor.getString(numberIndex);
                String name = cursor.getString(nameIndex);
                long date = cursor.getLong(dateIndex);
                int type = cursor.getInt(typeIndex);
                String photoUri = cursor.getString(photoUriIndex);
                callLogList.add(new CallLogEntry(name != null ? name : "Unknown", number, date, type, photoUri));
            }
            cursor.close();
        }

        callLogRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        callLogAdapter = new CallLogAdapter(callLogList, this);
        callLogRecyclerView.setAdapter(callLogAdapter);
    }

    @Override
    public void onItemClick(CallLogEntry item) {
        entryToCall = item;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST_CODE);
        } else {
            makeCall(item);
        }
    }

    private void makeCall(CallLogEntry entry) {
        Intent intent = new Intent(requireActivity(), CallActivity.class);
        intent.putExtra("NAME", entry.getName());
        intent.putExtra("NUMBER", entry.getNumber());
        startActivity(intent);
    }
}
