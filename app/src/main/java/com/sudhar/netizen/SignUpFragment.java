package com.sudhar.netizen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class SignUpFragment extends Fragment {

    private TextInputLayout mEmailFieldLayout, mEmailFieldLayoutTwo, mPasswordFieldLayout;
    private TextInputLayout mUsernameFieldLayout;
    private TextInputEditText mEmailField, mEmailFieldTwo, mPasswordFieldOne, mPasswordField;
    private TextInputEditText mUsernameField;
    private MaterialButton mSignUpBtn;
    private TextView mBackToLoginBtn;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSignUpFragmentInteraction(String email, String email2, String username, String password);
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

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();

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

        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mEmailField = view.findViewById(R.id.EmailField);
        mEmailFieldLayout = view.findViewById(R.id.EmailFieldLayout);

        mEmailFieldTwo = view.findViewById(R.id.EmailFieldTwo);
        mEmailFieldLayoutTwo = view.findViewById(R.id.EmailFieldLayoutTwo);

        mUsernameField = view.findViewById(R.id.UsernameField);
        mPasswordField = view.findViewById(R.id.PasswordField);


        mSignUpBtn = view.findViewById(R.id.SignUpBtn);
        mBackToLoginBtn = view.findViewById(R.id.BackToLoginBtn);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                String email2 = mEmailFieldTwo.getText().toString();
                String username = mUsernameField.getText().toString();
                String password = mPasswordField.getText().toString();
                mListener.onSignUpFragmentInteraction(email, email2, username, password);
            }
        });

        mBackToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment);
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