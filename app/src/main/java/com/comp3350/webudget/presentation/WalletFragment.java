package com.comp3350.webudget.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.TransactionException;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.R;
import com.comp3350.webudget.Exceptions.WalletException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class WalletFragment extends Fragment implements View.OnClickListener {


    private TextView balance,owner;
    private EditText username,transfer_amount,deposit_amount;
    private String current_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View walletView = inflater.inflate(R.layout.fragment_wallet , container, false);
        walletView.findViewById(R.id.send_button).setOnClickListener(this);
        walletView.findViewById(R.id.deposit_button).setOnClickListener(this);
        current_user = Services.userLogic().getCurrentUser();

        //get the required ui elements
        username = (EditText)walletView.findViewById(R.id.recipient_input);
        transfer_amount = (EditText)walletView.findViewById(R.id.amount_input);
        deposit_amount = (EditText)walletView.findViewById(R.id.depost_amt_input);
        owner = (TextView) walletView.findViewById(R.id.owner);
        balance = (TextView) walletView.findViewById(R.id.balance);

        //set the values dynamically
        owner.setText(current_user);
        updateBalance();


        return walletView;
    }

    public String[] getTransferInputValues(){
        String[] values = new String[2];

        values[0] = username.getText().toString();
        values[1] = transfer_amount.getText().toString();

        return values;
    }

    public String getDepositInputValue(){
        return deposit_amount.getText().toString();
    }

    private void updateBalance(){
        try {
            balance.setText(String.valueOf(Services.userWalletLogic().getAmount(current_user)));
        } catch (AccountException accountException) {
            accountException.printStackTrace();
        } catch (WalletException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch ( v.getId() ){
            //listen for click
            case R.id.send_button:
                String[] vals = getTransferInputValues();
                try {
                    Services.transactionLogic().userToUserTransaction(Services.userLogic().getCurrentUser(),vals[0],vals[1]);
                }catch( WalletException w ){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), w.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }catch ( AccountException a ){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), a.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }catch ( TransactionException t ){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Transfer Successful!", Toast.LENGTH_SHORT);
                toast.show();

                updateBalance();
                transfer_amount.getText().clear();
                username.getText().clear();
                break;

            case R.id.deposit_button:
                String deposit_val = getDepositInputValue();
                try {
                    Services.userWalletLogic().deposit(Services.userLogic().getCurrentUser(), deposit_val);
                }catch( WalletException w ){
                    Toast deposit_toast = Toast.makeText(getActivity().getApplicationContext(), w.getMessage(), Toast.LENGTH_SHORT);
                    deposit_toast.show();
                }catch ( AccountException a ){
                    Toast deposit_toast = Toast.makeText(getActivity().getApplicationContext(), a.getMessage(), Toast.LENGTH_SHORT);
                    deposit_toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast success_toast = Toast.makeText(getActivity().getApplicationContext(), "Deposit Successful!", Toast.LENGTH_SHORT);
                success_toast.show();

                updateBalance();

                deposit_amount.getText().clear();
                break;
        }

    }
}
