package com.firebaseauth.firebaseauth.Fragment;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebaseauth.firebaseauth.HomeActivity;
import com.firebaseauth.firebaseauth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class anonymouslyFragment extends Fragment {

    FirebaseAuth mAuth;
    Button btnLogIn;
    private ProgressDialog progress;
    private WebView mWebView;
    public anonymouslyFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String googleIdToken=getString(R.string.googleIdToken);
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
       View view=inflater.inflate(R.layout.incognito_fragment, container, false);
       mAuth=FirebaseAuth.getInstance();
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading");
       btnLogIn=view.findViewById(R.id.btnAnonymously);

       btnLogIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               progress.show();
               Task<AuthResult> resultTask=mAuth.signInAnonymously();
               resultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       mAuth.signInAnonymously()
                               .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                       if (task.isSuccessful()) {
                                           Log.d(TAG, "signInAnonymously:success");
                                           FirebaseUser user = mAuth.getCurrentUser();
                                           if (user!=null)
                                           {
                                               Intent in=new Intent(getActivity(), HomeActivity.class);
                                               startActivity(in);
                                               getActivity().finish();
                                           }
                                       } else {
                                           Log.w(TAG, "signInAnonymously:failure", task.getException());

                                       }
                                   }
                               });
                   }
               });
           }
       });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
