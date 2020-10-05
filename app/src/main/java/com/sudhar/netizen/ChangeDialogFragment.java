package com.sudhar.netizen;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangeDialogFragment extends DialogFragment {
    private TextInputLayout TextFieldLayout;
    private TextInputEditText TextField;

    private MaterialButton mCancelBtn, mDoneBtn;
    private static final String TAG = "ChaneDialogFragment";
    public static final String TypeUsername = "username";
    public static final String TypeAbout = "about";
    private String mValue;
    private String type;

    private RestInterface restInterface;
    private OnChangeListener mOnChangeListener;

    public void setUsername(String mValue) {
        this.mValue = mValue;
        this.type = TypeUsername;

    }

    public void setAbout(String mValue) {
        this.mValue = mValue;
        this.type = TypeAbout;

    }

    public ChangeDialogFragment() {
        // Required empty public constructor
        mValue = "";
    }


    public interface OnChangeListener {
        void onChanged(String username, String about);

    }

    public void setOnChangeListener(OnChangeListener mOnChangeListener) {
        this.mOnChangeListener = mOnChangeListener;
    }

    public static ChangeDialogFragment newInstance() {
        ChangeDialogFragment fragment = new ChangeDialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_user_detail_change_dialog, container, false);
        TextFieldLayout = view.findViewById(R.id.UsernameFieldLayout);
        TextField = view.findViewById(R.id.UsernameField);


        if (type.equals(TypeUsername)) {
            TextFieldLayout.setHint(TypeUsername);
            TextField.setInputType(InputType.TYPE_CLASS_TEXT);

        } else if (type.equals(TypeAbout)) {
            TextFieldLayout.setHint(TypeAbout);
            TextField.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        }


        TextField.setText(mValue);


        mCancelBtn = view.findViewById(R.id.CUCancelBtn);
        mDoneBtn = view.findViewById(R.id.CUDoneBtn);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                update(TextField.getText().toString());

                dismiss();
            }
        });


        return view;
    }

    private void update(String text) {
        JsonObject body = new JsonObject();

        body.addProperty(type, text);

        Call<JsonObject> call = restInterface.patchUserInfo(body);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");

                    return;
                }

                JsonObject jsonObject = response.body();
                Toast.makeText(getContext(), type + " Changed", Toast.LENGTH_SHORT).show();
                String username = jsonObject.get("username").getAsString();
                String about = jsonObject.get("about").getAsString();
                mOnChangeListener.onChanged(username, about);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}