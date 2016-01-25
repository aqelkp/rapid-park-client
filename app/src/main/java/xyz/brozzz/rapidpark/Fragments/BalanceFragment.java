package xyz.brozzz.rapidpark.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

import in.aqel.quickparksdk.Objects.User;
import in.aqel.quickparksdk.Utils.AppConstants;
import xyz.brozzz.rapidpark.Activity.MainActivity;
import xyz.brozzz.rapidpark.R;

public class BalanceFragment extends Fragment {

    User user;
    TextView tvBalance, b10, b50, b100;
    Firebase ref;
    private static String LOG_TAG = "BalanceFragment";

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        user = ((MainActivity) getActivity()).getUser();

        ref = new Firebase(AppConstants.SERVER);

        tvBalance = (TextView) view.findViewById(R.id.tvBalance);
        b10 = (TextView) view.findViewById(R.id.b10);
        b50 = (TextView) view.findViewById(R.id.b50);
        b100 = (TextView) view.findViewById(R.id.b100);


        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "10 rupess");
                payAmount(10);
            }
        });

        b50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payAmount(50);
                Log.d(LOG_TAG, "50 rupess");

            }
        });


        b100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payAmount(100);
            }
        });

        try {
            tvBalance.setText("₹ " + user.getBalance());
        } catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private void payAmount(final int amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Confirm to recharge ₹" + amount + " to your account?" );
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Firebase countRef = ref.child("users").child(ref.getAuth().getUid())
                        .child("balance");
                countRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData currentData) {
                        if(currentData.getValue() == null) {
                            currentData.setValue(amount);
                        } else {
                            currentData.setValue( (Double) currentData.getValue() + amount);
                        }

                        return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                        //This method will be called once with the results of the transaction.
                        Log.d(LOG_TAG, "Current data is:" + currentData.getValue().toString());
                        if (firebaseError != null){
                            Snackbar
                                    .make(tvBalance, firebaseError.getMessage(), Snackbar.LENGTH_SHORT)
                                    .show();
                            Log.d(LOG_TAG, firebaseError.getMessage());
                        } else {
                            Snackbar
                                    .make(tvBalance, "Added ₹"+ amount + " to your account",
                                            Snackbar.LENGTH_SHORT)
                                    .show();
                            user = ((MainActivity) getActivity()).getUser();
                            tvBalance.setText("₹ " + user.getBalance());


                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
