package edu.uga.cs.letsjitter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param position gets the position of the fragment and references that fragment to it's specific category
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;
            case 1:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;
            case 2:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return null;
        }
    }

    /**
     * Gives a title to the fragments that contain each category
     * @param position identifies the fragment position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Contacts";
            case 1:
                return "Groups";
            case 2:
                return "Settings";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
