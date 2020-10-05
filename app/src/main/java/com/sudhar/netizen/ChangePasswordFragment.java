package com.sudhar.netizen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;

import java.io.IOException;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment {
    private static final String TAG = "PasswordFragment";
    private TextInputLayout mCurrentPasswordFieldLayout;
    private TextInputEditText mCurrentPasswordField;
    private TextInputLayout mNewPasswordFieldLayoutOne, mNewPasswordFieldLayoutTwo;
    private TextInputEditText mNewPasswordFieldOne, mNewPasswordFieldTwo;

    private MaterialButton mCancelBtn, mChangeBtn;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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


        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        mCurrentPasswordFieldLayout = view.findViewById(R.id.CurrentPasswordFieldLayout);
        mCurrentPasswordField = view.findViewById(R.id.CurrentPasswordField);

        mNewPasswordFieldLayoutOne = view.findViewById(R.id.NewPasswordFieldLayoutOne);
        mNewPasswordFieldLayoutTwo = view.findViewById(R.id.NewPasswordFieldLayoutTwo);

        mNewPasswordFieldOne = view.findViewById(R.id.NewPasswordFieldOne);
        mNewPasswordFieldTwo = view.findViewById(R.id.NewPasswordFieldTwo);

        mCancelBtn = view.findViewById(R.id.CPCancelBtn);
        mChangeBtn = view.findViewById(R.id.CPChangeBtn);


        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mCurrentPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCurrentPasswordFieldLayout.isErrorEnabled()) {
                    mCurrentPasswordFieldLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });

        mNewPasswordFieldOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNewPasswordFieldLayoutOne.isErrorEnabled()) {
                    mNewPasswordFieldLayoutOne.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String oldPassword = mCurrentPasswordField.getText().toString();
                String newPassword = mNewPasswordFieldOne.getText().toString();
                String newPassword2 = mNewPasswordFieldTwo.getText().toString();
                Call<JsonObject> call = RetrofitClient.getClient(getContext()).create(RestInterface.class).changePassword(oldPassword, newPassword, newPassword2);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (!response.isSuccessful()) {

                            JsonObject jsonObject = null;
                            try {
                                jsonObject = JsonParser.parseString(response.errorBody().string()).getAsJsonObject();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Set<String> helll = jsonObject.keySet();
                            if (helll.contains("old_password")) {

                                mCurrentPasswordFieldLayout.setError(jsonObject.get("old_password").getAsString());
                            } else if (helll.contains("new_password")) {
                                mNewPasswordFieldLayoutOne.setError(jsonObject.get("new_password").getAsString());

                            }

                            return;
                        }
                        JsonObject jsonObject = response.body();
                        String res = jsonObject.get("response").getAsString();
                        mCurrentPasswordField.getText().clear();
                        mNewPasswordFieldOne.getText().clear();
                        mNewPasswordFieldTwo.getText().clear();
                        Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });
        return view;
    }


}