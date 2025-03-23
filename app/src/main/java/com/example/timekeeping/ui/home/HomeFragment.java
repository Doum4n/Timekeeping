package com.example.timekeeping.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.timekeeping.ui.group.AddShiftActivity;
import com.example.timekeeping.R;
import com.example.timekeeping.ui.scanner.ScannerActivity;
import com.example.timekeeping.adapters.GroupAdapter;
import com.example.timekeeping.databinding.FragmentHomeBinding;
import com.example.timekeeping.models.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.bind(view);
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        binding.btnTimekeeping.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            intent.putExtra("type","Timekeeping");
            startActivity(intent);
        });

        binding.btnJoinWorkGroup.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            intent.putExtra("type","JoinWorkGroup");
            startActivity(intent);
        });

        binding.fbtnAddGroup.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddGroupActivity.class);
            startActivity(intent);
        });

        initList();
        addMenuProvider();

        return view;
    }

    private void addMenuProvider(){
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_add) {
                    Intent intent = new Intent(getActivity(), AddShiftActivity.class);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private FragmentHomeBinding binding;
    private void getGroupsDetails(List<String> groupIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Group> groups = new ArrayList<>(); // Hiển thị các nhóm mà người dùng tham gia vào
        ArrayList<Group> CreatedGroups = new ArrayList<>(); // Hiển thị các nhóm mà người dùng tạo ra

        db.collection("groups").whereIn(FieldPath.documentId(), groupIds).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Group group = document.toObject(Group.class);
                        assert group != null;
                        group.setId(document.getId());
                        groups.add(group);
                    }

                    binding.rcwJoinedGroups.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    RecyclerView.Adapter<GroupAdapter.viewHolder> adapter = new GroupAdapter(groups);
                    binding.rcwJoinedGroups.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Lỗi khi lấy thông tin nhóm!", e));

        db.collection("groups").where(Filter.equalTo("creatorId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Group group = document.toObject(Group.class);
                        assert group != null;
                        group.setId(document.getId());
                        CreatedGroups.add(group);
                    }

                    binding.rcwCreatedGroups.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    RecyclerView.Adapter<GroupAdapter.viewHolder> adapter = new GroupAdapter(CreatedGroups);
                    binding.rcwCreatedGroups.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Lỗi khi lấy thông tin nhóm!", e));
    }

    private void initList() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentEmployeeId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("employees").document(currentEmployeeId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> groupIds = (List<String>) documentSnapshot.get("groups");
                        if (groupIds != null && !groupIds.isEmpty()) {
                            getGroupsDetails(groupIds); // Truy vấn chi tiết nhóm
                        }
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Lỗi khi lấy danh sách nhóm!", e));
    }

    @Override
    public void onResume() {
        super.onResume();

        initList();
    }
}