package com.comp3350.webudget.presentation;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.comp3350.webudget.R;
import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.GroupException;
import com.comp3350.webudget.application.Services;

import java.util.ArrayList;

public class CreateGroupFragment extends Fragment implements View.OnClickListener {

    private EditText group_name_field, description_field;
    private Button create_group_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group , container, false);

        // Fields
        group_name_field = (EditText)view.findViewById(R.id.group_name);
        description_field = (EditText)view.findViewById(R.id.group_description);

        // Buttons
        create_group_button = (Button)view.findViewById(R.id.create_group_create_group_button);
        create_group_button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ){
            case R.id.create_group_create_group_button:
                String[] inputValues = getInputValues();
                ArrayList<String> members = new ArrayList<String>();
                members.add(Services.userLogic().getCurrentUser()); // Add self to group
                try {
                    Services.groupLogic().createGroupWithUsers(inputValues[0], members);
                    load_fragment(new GroupFragment());
                } catch(GroupException | AccountException e) {
                    Toast toast= Toast.makeText(getActivity().getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                break;
        }
    }

    private String[] getInputValues(){
        String[] values = new String[2];
        values[0] = group_name_field.getText().toString();
        values[1] = description_field.getText().toString();
        return values;
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
}
