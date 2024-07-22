package com.comp3350.webudget.presentation;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.comp3350.webudget.Exceptions.MembershipException;
import com.comp3350.webudget.R;
import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class GroupFragment extends Fragment implements View.OnClickListener {

    private Button create_group_button, join_group_btn;
    private EditText entered_groupname;
    private ArrayList<Group> users_groups;
    private ArrayList<String> users_groups_names;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group , container, false);

        // Buttons
        create_group_button = (Button)view.findViewById(R.id.group_create_group_button);
        create_group_button.setOnClickListener(this);
        join_group_btn = (Button)view.findViewById(R.id.join_group);
        join_group_btn.setOnClickListener(this);

        //Edittext
        entered_groupname = (EditText)view.findViewById(R.id.group_name_field);

        // List View
        users_groups_names = new ArrayList<String>();
        try {
            users_groups = Services.groupLogic().getUserGroups(Services.userLogic().getCurrentUser());
            users_groups_names = groups_to_strings(users_groups);
        } catch (AccountException | GroupException e) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        ListView group_list = (ListView) view.findViewById(R.id.group_list);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, users_groups_names);
        group_list.setAdapter((listViewAdapter));
        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                // index is the position in the listview of the clicked item
                // index will match with the users_groups arraylist to get the correct group to display
                load_fragment(new GroupWalletFragment(users_groups.get(index)));
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() ){
            case R.id.group_create_group_button:
                load_fragment(new CreateGroupFragment());
                break;
            case R.id.join_group:
                try{
                    Services.groupLogic().addUserToGroup(Services.userLogic().getCurrentUser(), entered_groupname.getText().toString());
                }catch ( GroupException g ){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), g.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }catch( AccountException a ){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), a.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }catch ( MembershipException m ){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), m.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Successfully added to group!", Toast.LENGTH_SHORT);
                toast.show();
                load_fragment(new GroupFragment());
                break;
        }
    }

    private boolean load_fragment(Fragment frag) {
        boolean load_success = false;
        // switch to a try catch?
        if(frag != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, frag);
            transaction.addToBackStack(null);
            transaction.commit();
            load_success = true;
        }
        return load_success;
    }

    // Creates a list of group names as strings for listView to display
    // the arrayList will be in alphabetical sorted order
    private ArrayList<String> groups_to_strings(ArrayList<Group> groups) {
        ArrayList<String> groups_as_strings = new ArrayList<String>();
        if(groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                groups_as_strings.add(groups.get(i).toString());
            }
            Collections.sort(groups_as_strings);
        } else {
            groups_as_strings.add("nothing");
            groups_as_strings.add("appears");
            groups_as_strings.add("to");
            groups_as_strings.add("be");
            groups_as_strings.add("here");
        }
        return groups_as_strings;
    }
}
