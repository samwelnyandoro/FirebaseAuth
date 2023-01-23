package com.firebaseauth.firebaseauth.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebaseauth.firebaseauth.HomeActivity;
import com.firebaseauth.firebaseauth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class facebookFragment extends Fragment {

    private static final String TAG ="" ;
    LoginButton connectWithFbButton;
    CallbackManager mCallbackManager;
    FirebaseAuth mAuth;
    private ProgressDialog progress;
    public facebookFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.setApplicationId(getString(R.string.facebookId));
        FacebookSdk.sdkInitialize(getActivity());
        mAuth=FirebaseAuth.getInstance();
        View view=inflater.inflate(R.layout.facebook_fragment, container, false);
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loding");
        connectWithFbButton=view.findViewById(R.id.loginbutton);
        connectWithFbButton.setFragment(this);
        mCallbackManager = CallbackManager.Factory.create();
        connectWithFbButton.setReadPermissions("email", "public_profile");
        connectWithFbButton.setClickable(true);
        connectWithFbButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                progress.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        connectWithFbButton.setVisibility(View.INVISIBLE);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.show();
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null) {

                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                               getActivity().finish();
                            }
                            }
                        }
                });
    }

}
