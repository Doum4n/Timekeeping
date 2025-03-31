package com.example.timekeeping.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.timekeeping.ui.group.tabs.StaffmembersTab;

public class EmployeeTabAdapter extends FragmentStateAdapter {

    private String groupId;
    public EmployeeTabAdapter(@NonNull FragmentActivity fragmentActivity, String groupId) {
        super(fragmentActivity);
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return StaffmembersTab.newInstance(groupId, 1);
            case 1:
                return StaffmembersTab.newInstance(groupId, 0);
            case 2:
                return new Fragment();
            default:
                return StaffmembersTab.newInstance(groupId, 0);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng tab
    }
}
