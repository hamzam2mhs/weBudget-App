package com.comp3350.webudget.persistence;

import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.objects.Group;

import java.util.ArrayList;

public interface IGroupDatabase {
     public int insertGroup(String groupName) throws GroupException;
     public Group getGroup(int id) throws GroupException;
     public ArrayList<Group> getAllGroups() throws GroupException;
     public ArrayList<Group> getGroups(String username) throws GroupException;
}
