package com.comp3350.webudget.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.R;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.objects.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class AccountFragment extends Fragment implements View.OnClickListener {


    private TextView uname,fname,lname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View accountView = inflater.inflate(R.layout.fragment_account , container, false);
        accountView.findViewById(R.id.wallet_icon).setOnClickListener(this);
        uname = (TextView)accountView.findViewById(R.id.acct_username_data);
        fname = (TextView)accountView.findViewById(R.id.acct_fname_data);
        lname = (TextView)accountView.findViewById(R.id.acct_lname_data);
        populateFields();
        return accountView;
    }

    private void populateFields(){
        Account user = null;
        try{
            user = Services.userLogic().getAccount(Services.userLogic().getCurrentUser());
        }catch( AccountException a ){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "An error occured loading user data", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        uname.setText(Services.userLogic().getCurrentUser());
        fname.setText(user.getFirstName());
        lname.setText(user.getLastName());

    }



    @Override
    public void onClick(View v) {

        switch ( v.getId() ){
            //listen for clicks
            case R.id.wallet_icon:
                WalletFragment frag = new WalletFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, frag);
                transaction.addToBackStack(null);
                transaction.commit();
        }

    }
}
