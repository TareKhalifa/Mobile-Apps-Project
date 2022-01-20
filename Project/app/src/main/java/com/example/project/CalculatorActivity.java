package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class CalculatorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DatabaseReference mDatabase;
    ListCheckBoxAdapter m_adapter;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Bundle bundle = getIntent().getExtras();
        String groupID = bundle.getString("groupID");
        spinner = (Spinner) findViewById(R.id.paidspinner);
        EditText editText = (EditText) findViewById(R.id.editTextNumber);
        EditText editText2 = (EditText) findViewById(R.id.editTextName);
        ArrayList<DropDownData> a = new ArrayList<DropDownData>();
        ArrayList<String> a2 = new ArrayList<String>();
        a.add(new DropDownData(MainActivity.currentUser.getDisplayName(), MainActivity.currentUser.getPhotoUrl().toString()));
        a2.add(MainActivity.currentUser.getUid());
        for (int i = 0; i < LoggedInUser.groups.length; i++) {
            if (groupID.equals(LoggedInUser.groups[i].getId())) {
                for (String memberID: LoggedInUser.groups[i].getMembersIDs()) {
                    if (!memberID.equals(MainActivity.currentUser.getUid())) {
                        a.add(new DropDownData(LoggedInUser.groups[i].names.get(memberID), LoggedInUser.groups[i].photos.get(memberID)));
                        a2.add(memberID);
                    }
                }
            }
        }
        ListView listView = (ListView) findViewById(R.id.members_inv_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view11, int position, long id) {
                CheckBox cb = (CheckBox)view11.findViewById(R.id.list_view_item_checkbox);
                cb.setChecked(!cb.isChecked());
            }
        });
        m_adapter = new ListCheckBoxAdapter(this,R.layout.members_involved_row, a.toArray(new DropDownData[a.size()]));
        listView.setAdapter(m_adapter);
        spinner.setAdapter(new DropDownAdapter(CalculatorActivity.this, R.layout.paid_dropdown, a));
        Button btn = (Button) findViewById(R.id.confirm_expense);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for the value of the spinner
                String whopaid =  a2.get(spinner.getSelectedItemPosition());
                //check for the value of the checkbox
                ArrayList<String> membersInvolved = new ArrayList<String>();
                for (int i = 0; i < m_adapter.getCount(); i++) {
                    if (m_adapter.mCheckStates.get(i)) {
                        membersInvolved.add(a2.get(i));
                    }
                }
                int value = 0;
                String TransactionName = "Expense";
                if(!editText.getText().toString().equals(""))
                    value = Integer.parseInt(editText.getText().toString());
                if(!editText2.getText().toString().equals(""))
                    TransactionName = editText2.getText().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("groups").child(groupID).child("Transactions").push().setValue(new Transaction(TransactionName,whopaid,membersInvolved ,java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMM HH:mm")),String.valueOf(value))).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                        finish();
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
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

}