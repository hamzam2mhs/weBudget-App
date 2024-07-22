package com.comp3350.webudget.presentation;

import android.app.DatePickerDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.comp3350.webudget.Exceptions.AccountException;
import com.comp3350.webudget.Exceptions.TransactionException;
import com.comp3350.webudget.Exceptions.WalletException;
import com.comp3350.webudget.R;
import com.comp3350.webudget.application.Services;
import com.comp3350.webudget.business.TransactionLogic;
import com.comp3350.webudget.objects.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class CalendarFragment extends Fragment implements View.OnClickListener {

    private CalendarView calendar;
    private ArrayList<Transaction> recievedTransactions;
    private ArrayList<Transaction> sentTransactions;
    private TextView to,toTitle,from,fromTitle,amt,amtTitle,noData;
    private ListView transaction_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View calView = inflater.inflate(R.layout.fragment_calendar , container, false);
        calendar = (CalendarView)calView.findViewById(R.id.calendar);
        recievedTransactions = null;
        sentTransactions = null;
        to = (TextView)calView.findViewById(R.id.sentto_data);
        toTitle = (TextView)calView.findViewById(R.id.sent_to);
        from = (TextView)calView.findViewById(R.id.sentby_data);
        fromTitle = (TextView)calView.findViewById(R.id.sent_by);
        amt = (TextView)calView.findViewById(R.id.amount_data);
        amtTitle = (TextView)calView.findViewById(R.id.transaction_amt);
        noData = (TextView)calView.findViewById(R.id.no_transactions);
        transaction_list = (ListView)calView.findViewById(R.id.transaction_list);
        try{
            recievedTransactions = Services.transactionLogic().getTransactionsIn(Services.userLogic().getCurrentUser());
            sentTransactions = Services.transactionLogic().getTransactionsOut(Services.userLogic().getCurrentUser());

        }catch ( WalletException w ){

        }catch ( AccountException a ){

        }catch ( TransactionException t ){

        }
        transaction_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Transaction entry = (Transaction) adapterView.getAdapter().getItem(i);
                toTitle.setText("Sent To:");
                fromTitle.setText("Sent By:");
                amtTitle.setText("Amount:");
                to.setText(""+entry.getToWalletid());
                from.setText(""+entry.getFromWalletid());
                amt.setText("$"+entry.getAmount());

            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                to.setText("");
                from.setText("");
                amt.setText("");
                toTitle.setText("");
                fromTitle.setText("");
                amtTitle.setText("");
                String month;
                if ( (i1 + 1) < 10 ){
                    month = "0"+(i1+1);
                }else{
                    month = ""+(i1+1);
                }
                String date = i+ "/"+month+"/"+i2;
                System.out.println(date);
                getTransactionsOnDay(date);

            }
        });

        return calView;
    }

    private void getTransactionsOnDay(String date){
        ArrayList<Transaction> matches = new ArrayList<>();
        for( int i = 0; i < sentTransactions.size(); i++ ){
            if ( sentTransactions.get(i).getDate().equals(date) ){
                matches.add(sentTransactions.get(i));
            }
        }
        for ( int i = 0; i < recievedTransactions.size(); i++ ){
            if ( recievedTransactions.get(i).getDate().equals(date) ){
                matches.add(recievedTransactions.get(i));
            }
        }
        if ( matches.size() == 0 ){
            noData.setVisibility(View.VISIBLE);
        }else{
            noData.setVisibility(View.INVISIBLE);
        }
        ArrayAdapter<Transaction> listViewAdapter = new ArrayAdapter<Transaction>(getActivity(), android.R.layout.simple_list_item_1, matches);
        transaction_list.setAdapter(listViewAdapter);

        System.out.println(matches.size());
    }

    @Override
    public void onClick(View v) {

        switch ( v.getId() ){
            //listen for clicks

        }
    }
}
