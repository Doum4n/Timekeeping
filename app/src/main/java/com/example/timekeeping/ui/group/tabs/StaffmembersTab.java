package com.example.timekeeping.ui.group.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timekeeping.R;
import com.example.timekeeping.adapters.EmployeeAdapter;
import com.example.timekeeping.databinding.FragmentStaffmembersTabBinding;
import com.example.timekeeping.models.Employee;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffmembersTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffmembersTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GROUP_ID = "groupId"; // Thêm hằng số cho Group ID
    private static final String ARG_TAB = "tab";
    private int tab;
    private String groupId;
    private FirebaseFirestore db;
    private FragmentStaffmembersTabBinding binding;

    public StaffmembersTab() {
        // Required empty public constructor
    }

    public static StaffmembersTab newInstance(String groupId, int tab) {
        StaffmembersTab fragment = new StaffmembersTab();
        Bundle args = new Bundle();
        args.putString(ARG_GROUP_ID, groupId);
        args.putInt(ARG_TAB, tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getString(ARG_GROUP_ID); // Lấy groupId từ Bundle
            tab = getArguments().getInt(ARG_TAB);
        }
        db = FirebaseFirestore.getInstance();
    }

    private void initList() {
        List<Employee> employees = new ArrayList<>();

        db.collection("employees").whereArrayContains("groups", groupId).get()
                .addOnSuccessListener(v -> {
                    for (DocumentSnapshot document : v.getDocuments()) {
                        Employee employee = document.toObject(Employee.class);
                        employees.add(employee);
                    }

                    binding.rswEmployeeList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    RecyclerView.Adapter<EmployeeAdapter.viewHolder> adapter = new EmployeeAdapter(employees, tab);
                    binding.rswEmployeeList.setAdapter(adapter);
                }).addOnFailureListener(e -> Log.w("Firestore", "Lỗi khi lấy danh sách nhân viên!", e));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStaffmembersTabBinding.inflate(getLayoutInflater());

        initList();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            initList();
        }
    }
}