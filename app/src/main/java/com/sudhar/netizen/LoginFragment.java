package com.sudhar.netizen;

import android.app.Fragment;
import android.os.Bundle;


import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class LoginFragment extends Fragment {

    private TextInputLayout mEmailFieldLayout, mPasswordFieldLayout;
    private TextInputEditText mEmailField, mPasswordField;
    private MaterialButton mLoginBtn;
    private TextView mCreateAccountBtn;
    private TextView mForgotPasswordBtn;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onLoginFragmentInteraction(String email, String password);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();

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

        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEmailField = view.findViewById(R.id.EmailField);
        mEmailFieldLayout = view.findViewById(R.id.EmailFieldLayout);
        mPasswordField = view.findViewById(R.id.PasswordField);
        mLoginBtn = view.findViewById(R.id.LoginBtn);
        mCreateAccountBtn = view.findViewById(R.id.CreateAccountTextView);
        mForgotPasswordBtn = view.findViewById(R.id.ForgotPasswordBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                mListener.onLoginFragmentInteraction(email, password);

            }
        });

        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });

        mForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString().trim();
                Boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
                if (validateEmail()) {

                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
                }


            }
        });


        return view;
    }

    private Boolean validateEmail() {
        String email = mEmailField.getText().toString().trim();
        Boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (email.isEmpty() || !isValid) {
            mEmailFieldLayout.setError("Enter a valid Email address");
            mEmailField.requestFocus();

        } else {
            mEmailFieldLayout.setErrorEnabled(false);

        }
        return isValid;
    }
}