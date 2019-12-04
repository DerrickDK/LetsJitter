package edu.uga.cs.letsjitter;

/**
 * This class creates the structure of the group object
 */
public class Group  {
    private String groupName;
    public Group() {
    }
    public Group(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                '}';
    }
}
