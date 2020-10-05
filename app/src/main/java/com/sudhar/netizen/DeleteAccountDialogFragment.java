package com.sudhar.netizen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class DeleteAccountDialogFragment extends DialogFragment {
    private TextInputLayout mPasswordFieldLayout;
    private TextInputEditText mPasswordField;

    private MaterialButton mCancelBtn, mConfirmBtn;

    public DeleteAccountDialogFragment() {
        // Required empty public constructor
    }


    public static DeleteAccountDialogFragment newInstance() {
        DeleteAccountDialogFragment fragment = new DeleteAccountDialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_account_dialog, container, false);

        mPasswordFieldLayout = view.findViewById(R.id.PasswordFieldLayout);
        mPasswordField = view.findViewById(R.id.PasswordField);

//        mUsernameField.setText("Username");

        mCancelBtn = view.findViewById(R.id.DACancelBtn);
        mConfirmBtn = view.findViewById(R.id.DAConfirmBtn);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                dismiss();
                Intent intent = new Intent(getContext(), AuthActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}