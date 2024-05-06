package com.example.servermonitor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitorFragment extends Fragment {
    private static final String TAG = "RRR";

    private static final String URL =
            "https://server-monitor-e6bb3-default-rtdb.europe-west1.firebasedatabase.app/";

    private TextView textView;

    public MonitorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        textView = view.findViewById(R.id.textView);

        displayStatus();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigationView(true);
    }

    private void displayStatus() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(URL).getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServerInfo serverStatus = snapshot.getValue(ServerInfo.class);
                if (serverStatus != null) {
                    String data = "Total Disk Space: " + serverStatus.getTotal_disk_space() + "\n" +
                            "Occupied Disk Space: " + serverStatus.getOccupied_disk_space() + "\n" +
                            "Free Disk Space Percentage: " + serverStatus.getFree_disk_space_percentage() + "\n" +
                            "CPU Load: " + serverStatus.getCpu_load() + "\n" +
                            "RAM Usage: " + serverStatus.getRam_usage() + "\n" +
                            "Uptime: " + serverStatus.getUptime() + "\n" +
                            "Logged In Users: " + serverStatus.getLogged_in_users().toString();
                    textView.setText(data);
                } else {
                    textView.setText("No data available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
                textView.setText(error.getMessage());
            }
        });

    }
}