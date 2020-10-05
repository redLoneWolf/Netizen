package com.sudhar.netizen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.TemplateDetailModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportDialogFragment extends DialogFragment {
    private static final String TAG = "ReportDialogFragment";

    private MemeDetailModel memeData;
    private TemplateDetailModel templateDetailModel;
    private List<String> values;
    private ContentType contentType;
    private String contentTypeString;

    private SharedPreferenceHelper sharedPreferenceHelper;
    private RestInterface restInterface;
    private TextInputEditText mDescription;
    private MaterialButton mCancelBtn, mReportBtn;
    private String objectId;
    private int anotherUserId;

    public void setAnotherUserId(int anotherUserId) {
        this.anotherUserId = anotherUserId;
        contentType = ContentType.USER;
    }

    public void setMemeId(String id) {
        objectId = id;
        contentType = ContentType.MEME;
        contentTypeString = "meme";


    }

    public void setTemplateId(String id) {
        objectId = id;
        contentType = ContentType.TEMPLATE;
        contentTypeString = "template";
//        values = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.contentReportMenu)));
    }


    public static ReportDialogFragment newInstance() {
        ReportDialogFragment fragment = new ReportDialogFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_option_dialog, container, false);
        mDescription = view.findViewById(R.id.DescriptionField);
        mCancelBtn = view.findViewById(R.id.CancelBtn);
        mReportBtn = view.findViewById(R.id.ReportBtn);

        mDescription.setVisibility(View.GONE);
        mReportBtn.setEnabled(false);
        sharedPreferenceHelper = new SharedPreferenceHelper(getContext());
        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);


        ListView listView = view.findViewById(R.id.PostOptionList);


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (contentType != ContentType.USER) {
            values = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.contentReportMenu)));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, values);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    if (values.get(position).trim().equals("Other")) {
                        mDescription.setVisibility(View.VISIBLE);
                        mDescription.getText().clear();
                        mDescription.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String commentInput = mDescription.getText().toString();
                                mReportBtn.setEnabled(!commentInput.isEmpty());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        mReportBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("content_type", contentTypeString);
                                map.put("object_id", objectId);
                                map.put("subject", values.get(position).trim());
                                map.put("description", mDescription.getText().toString());
                                reportContent(map);
                            }
                        });
                        mReportBtn.setEnabled(false);
                    } else {
                        mDescription.setVisibility(View.GONE);
                        mReportBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("content_type", contentTypeString);
                                map.put("object_id", objectId);
                                map.put("subject", values.get(position).trim());

                                reportContent(map);
                            }
                        });
                        mReportBtn.setEnabled(true);
                    }


                }
            });
        } else {
            values = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.userReportMenu)));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, values);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    if (values.get(position).trim().equals("Other")) {
                        mDescription.setVisibility(View.VISIBLE);
                        mDescription.getText().clear();
                        mDescription.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String commentInput = mDescription.getText().toString();
                                mReportBtn.setEnabled(!commentInput.isEmpty());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        mReportBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("another_user_id", "14");
                                map.put("subject", values.get(position).trim());
                                map.put("description", mDescription.getText().toString());
                                reportUser(map);
                            }
                        });
                        mReportBtn.setEnabled(false);
                    } else {
                        mDescription.setVisibility(View.GONE);
                        mReportBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("another_user_id", String.valueOf(anotherUserId));
                                map.put("subject", values.get(position).trim());

                                reportUser(map);
                            }
                        });
                        mReportBtn.setEnabled(true);
                    }


                }
            });
        }


        return view;
    }


    private void reportContent(HashMap<String, String> data) {
        Call<JsonObject> call = restInterface.reportContent(data);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: error" + response.code());
                    return;
                }
                dismiss();
                Toast.makeText(getContext(), "Reported Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void reportUser(HashMap<String, String> data) {
        Call<JsonObject> call = restInterface.reportUser(data);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: error" + response.code());
                    return;
                }
                dismiss();
                Toast.makeText(getContext(), "Reported Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}
