package com.firebaseauth.firebaseauth.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebaseauth.firebaseauth.HomeActivity;
import com.firebaseauth.firebaseauth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneFragment extends Fragment {

    EditText edtPhone, edtVerify;
    private static final String TAG = "";
    Button btnVerify, btnSubmit,btnChange;
    String codeSend;
    TextInputLayout textVetify;
    private ProgressDialog progress;
    TextView txtResendOTP;
    boolean value = false;
    private FirebaseAuth mAuth;
    public phoneFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null)
        {
            value=true;
            Intent in=new Intent(getActivity(), HomeActivity.class);
            startActivity(in);
            getActivity().finish();
        }
        View view = inflater.inflate(R.layout.phone_fragment, container, false);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtVerify = view.findViewById(R.id.edtVerify);
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading");
        txtResendOTP=view.findViewById(R.id.txtResendOTP);
        btnChange=view.findViewById(R.id.btnChange);
        textVetify=view.findViewById(R.id.textVetify);
        btnVerify = view.findViewById(R.id.btnVerify);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Login");
                alertDialog.setMessage("OTP Send");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progress.dismiss();
                            }
                        });
                alertDialog.show();

                sendVerificationCode();
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation()) {
                        return;
                    } else {
                        sendVerificationCode();
                            if (edtPhone != null) {
                                value = true;
                                edtPhone.setFocusable(false);
                                btnVerify.setVisibility(View.GONE);
                                btnChange.setVisibility(View.VISIBLE);

                                textVetify.setVisibility(View.VISIBLE);
                                edtVerify.setVisibility(View.VISIBLE);
                                txtResendOTP.setVisibility(View.VISIBLE);
                                btnSubmit.setVisibility(View.VISIBLE);

                            }
                }
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVerify.setVisibility(View.VISIBLE);
                btnChange.setVisibility(View.GONE);
                edtPhone.setFocusable(true);
                edtPhone.setFocusableInTouchMode(true);
                textVetify.setVisibility(View.INVISIBLE);
                edtVerify.setVisibility(View.INVISIBLE);
                txtResendOTP.setVisibility(View.INVISIBLE);
                edtVerify.setText("");
                btnSubmit.setVisibility(View.INVISIBLE);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationCode()){
                    return;
                }
                else {
                    String code = edtVerify.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSend, code);
                    signInWithPhoneAuthCredential(credential);
                    if (code == credential.getProvider()) {
                        value = true;
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                    if (code != credential.getProvider() ) {
                        progress.dismiss();
                    }
                }
                progress.show();
            }
        });
        return view;
    }
    private boolean validation()
    {
        boolean value=true;
        String phoneNumber=edtPhone.getText().toString();
        if (phoneNumber.isEmpty()) {
            edtPhone.setError("Enter a Phone Number!");
            edtPhone.requestFocus();
            return false;
        }
        if (phoneNumber.length() <=10){
            edtPhone.setError("Please Enter a valid Number!");
            edtPhone.requestFocus();
            return false;
        }
        return value;
    }
    private boolean validationCode()
    {
        String code = edtVerify.getText().toString();
        if (code.isEmpty())
        {
            edtVerify.setError("Please enter Code!");
            edtVerify.requestFocus();
            return false;
        }
        return true;
    }

    private void sendVerificationCode() {
        String phoneNumber=edtPhone.getText().toString();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSend=s;
            }

        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallbacks);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null)
                            {
                                value=true;
                                Intent in=new Intent(getActivity(),HomeActivity.class);
                                startActivity(in);
                                getActivity().finish();
                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Phone");
                alertDialog.setMessage(e.getLocalizedMessage());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progress.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}
