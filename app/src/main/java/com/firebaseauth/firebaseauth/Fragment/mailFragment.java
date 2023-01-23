package com.firebaseauth.firebaseauth.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebaseauth.firebaseauth.HomeActivity;
import com.firebaseauth.firebaseauth.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class mailFragment extends Fragment {

    private static final String TAG ="" ;
    TabLayout tabLayoutMail;
    TextInputLayout txtName,txtEmail,txtEmailSignUp,txtTabPassword,txtPasswordSignUp;;
    private ProgressDialog progress;
    EditText edtName,edtEmail,edtPassword,edtEmailSignUp,edtPasswordSignUp;
    Button btnSignIn,btnLogIn;

    private FirebaseAuth mAuth;
    public mailFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.mail_fragment, container, false);
        tabLayoutMail=view.findViewById(R.id.tabLayoutmail);
        txtName=view.findViewById(R.id.txtName);
        edtEmail=view.findViewById(R.id.edtEmail);

        txtEmailSignUp=view.findViewById(R.id.txtEmailSignUp);
        edtEmailSignUp=view.findViewById(R.id.edtEmailSignUp);
        txtTabPassword=view.findViewById(R.id.txtTabPassword);
        edtPasswordSignUp=view.findViewById(R.id.edtPasswordSignUp);
        txtPasswordSignUp=view.findViewById(R.id.txtPasswordSignUp);
        txtEmail=view.findViewById(R.id.txtEmail);
        edtPassword=view.findViewById(R.id.edtPassword);
        btnSignIn=view.findViewById(R.id.btnSignIn);
        edtName=view.findViewById(R.id.edtname);
        btnLogIn=view.findViewById(R.id.btnLogIn);
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validate())
                {
                    return;
                }
                else {
                    progress.show();
                    progress.setCancelable(false);
                    createAccount();
                }

            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validation())
                {
                        return;
                }
                else {
                    signInEmail();
                }
                progress.show();
                progress.setCancelable(false);
            }
        });
        tabLayoutMail.addTab(tabLayoutMail.newTab().setText("Log In"));
        tabLayoutMail.addTab(tabLayoutMail.newTab().setText("Sign Up"));
        tabLayoutMail.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabTapped(tab.getPosition());
                if (tab.getPosition() == 0)
                {
                   txtName.setVisibility(View.GONE);
                   edtName.setVisibility(View.GONE);
                   edtEmail.setVisibility(View.VISIBLE);
                   txtEmail.setVisibility(View.VISIBLE);
                    btnLogIn.setVisibility(View.VISIBLE);
                    txtEmailSignUp.setVisibility(View.GONE);
                    edtEmailSignUp.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.GONE);
                    edtPassword.setVisibility(View.VISIBLE);
                    edtPasswordSignUp.setVisibility(View.INVISIBLE);
                    txtPasswordSignUp.setVisibility(View.INVISIBLE);
                    txtTabPassword.setVisibility(View.VISIBLE);
                }if (tab.getPosition() ==1) {
                   txtName.setVisibility(View.VISIBLE);
                   edtName.setVisibility(View.VISIBLE);
                   txtEmailSignUp.setVisibility(View.VISIBLE);
                   edtEmailSignUp.setVisibility(View.VISIBLE);
                    txtEmail.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
                    btnLogIn.setVisibility(View.GONE);
                    edtEmail.setVisibility(View.GONE);
                    edtPassword.setVisibility(View.GONE);
                    txtTabPassword.setVisibility(View.GONE);
                    txtPasswordSignUp.setVisibility(View.VISIBLE);
                    edtPasswordSignUp.setVisibility(View.VISIBLE);
                    edtEmail.setText("");
                    edtPassword.setText("");
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }
    private void onTabTapped(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            default:
        }
    }

    private boolean validation()
    {
        boolean valid=true;
        String email=edtEmail.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            edtEmail.setError("enter Email");
            edtEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edtEmail.setError("enter valid  Email");
            edtEmail.requestFocus();
            return false;

        }
        String password=edtPassword.getText().toString();
        if (TextUtils.isEmpty(password))
        {
            edtPassword.setError("enter password");
            edtPassword.requestFocus();
            return false;
        }
        if (password.length()>=8 && password.length()>=17)
        {
            edtPassword.setError("enter Maximum 16 character password");
            edtPassword.requestFocus();
            return false;
        }
        return valid;
    }
    private boolean validate()
    {
        boolean valid=true;
        String name=edtName.getText().toString();
        if (TextUtils.isEmpty(name))
        {
            edtName.setError("Enter name");
            edtName.requestFocus();
            return false;
        }
        String email=edtEmailSignUp.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            edtEmailSignUp.setError("enter  Email");
            edtEmailSignUp.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edtEmailSignUp.setError("enter valid  Email");

            edtEmailSignUp.requestFocus();
            return false;

        }
        String pass=edtPasswordSignUp.getText().toString();
        if (TextUtils.isEmpty(pass))
        {
            edtPasswordSignUp.setError("enter  Password");
            edtPasswordSignUp.requestFocus();
            return false;
        }
        if (pass.length()>=8 && pass.length()>=16)
        {
            edtPassword.setError("enter Maximum 16 character password");
            edtPassword.requestFocus();
            return false;
        }
        return valid;

    }
    public void createAccount()
    {
        String email=edtEmailSignUp.getText().toString();
        String password=edtPasswordSignUp.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null) {
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Login");
                        alertDialog.setMessage(e.getLocalizedMessage()+"");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });

    }
    public  void signInEmail()
    {
        String email=edtEmail.getText().toString();
        String password=edtPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null) {
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } else {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Login");
                        alertDialog.setMessage(e.getLocalizedMessage()+"");
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
