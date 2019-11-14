package edu.uga.cs.letsjitter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;
            case 3:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Contacts";
            case 1:
                return "Chat";
            case 2:
                return "Groups";
            case 3:
                return "Settings";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
