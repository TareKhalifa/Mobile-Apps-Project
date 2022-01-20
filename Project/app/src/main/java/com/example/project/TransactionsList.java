package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import kotlin.Pair;

public class TransactionsList extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ListView lv2;
    DebtAdapter adapter2;
    ListView lv;
    TransactionsListAdapter adapter;
    String groupName;
    String groupID;
    Bundle bundle;
    ArrayList<String> members;
    ArrayList<DebtData> debtsRows;
    ArrayList<Pair<String,String>> rowsID;
    public void doo(){
        Bundle bundle = getIntent().getExtras();
        FloatingActionButton fab = findViewById(R.id.fab_transactions);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionsList.this, CalculatorActivity.class);
                intent.putExtra("groupID", bundle.getString("groupID"));
                startActivity(intent);
            }
        });
        groupName = bundle.getString("groupName");
        groupID   = bundle.getString("groupID");
        TextView groupNameTextView = findViewById(R.id.grpName);
        groupNameTextView.setText(groupName);
        lv = (ListView) findViewById(R.id.list111);
        ArrayList<TransactionsRowData> transactionsRows = new ArrayList<>();
        HashMap<String, Transaction> transactions = new HashMap<>();
        Group group = new Group(groupID, groupName);
        for(int i=0; i< LoggedInUser.groups.length; i++){
            if(LoggedInUser.groups[i].getId().equals(groupID)){
                transactions = LoggedInUser.groups[i].transactions;
                group = LoggedInUser.groups[i];
            }
        }
        int idx = 0;
        Transaction[] transactionsArray = new Transaction[transactions.size()];
        for (String key : transactions.keySet()) {
            Transaction transaction = transactions.get(key);
            transactionsArray[idx++] = transaction;

            String line  = group.names.get(transaction.getPayerID()) + " paid " + transaction.getAmount() + " for " +transaction.getName();
            if(transaction.getName().equals("Settlement")){
                line = group.names.get(transaction.getPayerID()) + " settled the debt of " + transaction.getAmount() + " to " +  group.names.get(transaction.getPayeeIDs().get(0));
            }
            transactionsRows.add(new TransactionsRowData(group.photos.get(transaction.getPayerID()),line));
        }



        float[]debts = new float[group.names.size()];
        members = new ArrayList<>(Arrays.asList(group.getMembersIDs()));
        for(int i=0; i<transactions.size(); i++){
            Transaction transaction2 = transactionsArray[i];
            int payerIdx = members.indexOf(transaction2.getPayerID());
            debts[payerIdx] += Float.parseFloat(transaction2.getAmount());
            for(int j=0; j<transaction2.getPayeeIDs().size(); j++){
                int payeeIdx = members.indexOf(transaction2.getPayeeIDs().get(j));
                debts[payeeIdx] -= (Float.parseFloat(transaction2.getAmount())/transaction2.getPayeeIDs().size());
            }
        }
        HashMap<Pair<Integer, Integer>, Float> debtsMap = Logic.start(debts);
        debtsRows = new ArrayList<>();
        rowsID = new ArrayList<>();
        for(Pair<Integer, Integer> key : debtsMap.keySet()){

            debtsRows.add(new DebtData(group.names.get(members.get(key.getFirst())), group.photos.get(members.get(key.getFirst())), String.valueOf(debtsMap.get(key)),group.names.get(members.get(key.getSecond())), group.photos.get(members.get(key.getSecond()))));
            rowsID.add(new Pair<>(members.get(key.getFirst()), members.get(key.getSecond())));
        }
        adapter = new TransactionsListAdapter(this, transactionsRows);
        adapter2 = new DebtAdapter(this, debtsRows);
        adapter2.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions_with_fab);
        doo();
        lv.setAdapter(adapter);
        lv2 = (ListView) findViewById(R.id.list222);
        lv2.setAdapter(adapter2);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                new AlertDialog.Builder(context)
                        .setTitle("Settle Debt")
                        .setMessage("Do you want to settle this debt?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                ArrayList<String> payees = new ArrayList<>();
                                payees.add(rowsID.get(position).getSecond());
                                mDatabase.child("groups").child(groupID).child("Transactions").push().setValue(new Transaction("Settlement",rowsID.get(position).getFirst(),payees  ,java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMM HH:mm")),debtsRows.get(position).value)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mDatabase.child("groups").child(groupID).child("Transactions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task2) {
                                                    if(task2.isSuccessful()){
                                                        DataSnapshot dataSnapshot = task2.getResult();
                                                        int idxx = 0;
                                                        for(int jj = 0; jj<LoggedInUser.groups.length; jj++){
                                                            if(LoggedInUser.groups[jj].getId().equals(groupID)){
                                                                idxx = jj;
                                                            }
                                                        }
                                                        LoggedInUser.groups[idxx].transactions.clear();
                                                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                                                            Transaction t = ds.getValue(Transaction.class);
                                                            LoggedInUser.groups[idxx].transactions.put(ds.getKey(),t);
                                                        }
                                                        doo();
                                                        adapter.notifyDataSetChanged();
                                                        onResume();
                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            Log.e("Error", task.getException().getMessage());
                                        }
                                    }
                                });
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        doo();
        lv.setAdapter(adapter);
        lv2 = (ListView) findViewById(R.id.list222);
        lv2.setAdapter(adapter2);
    }
}