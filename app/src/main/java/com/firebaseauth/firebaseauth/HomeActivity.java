package com.firebaseauth.firebaseauth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button btnLogout;

    ImageView imageFB,imageGoogle;
    private ProgressDialog progress;
    LinearLayout linType,linUserId,linName,linEmail,linPhone;
    TextView txtName,txtEmail,txtPhone,txtType,txtUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId(getString(R.string.facebookId));
        FacebookSdk.sdkInitialize(HomeActivity.this);
        LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        initView();
        FirebaseUser user=mAuth.getCurrentUser();
        progress=new ProgressDialog(HomeActivity.this);
        progress.setMessage("Loading");
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                progress.show();
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if (user!=null)
        {
            List<? extends UserInfo> infos = user.getProviderData();
            for (UserInfo ui : infos) {
                if (ui.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)) {
                    txtName.setVisibility(View.VISIBLE);
                    txtUserId.setVisibility(View.VISIBLE);
                    imageGoogle.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(user.getPhotoUrl())
                            .into(imageGoogle);
                    txtType.setVisibility(View.VISIBLE);
                    linEmail.setVisibility(View.GONE);
                    linPhone.setVisibility(View.GONE);
                    imageFB.setVisibility(View.GONE);
                    txtName.setText(String.format("Display Name : %s", user.getDisplayName()));
                    txtType.setText(String.format(" Login Type : Google"));
                    txtUserId.setText(String.format("USER ID : %s", user.getUid()));
                    return;
                }
                if (ui.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)) {
                    txtType.setVisibility(View.VISIBLE);
                    txtUserId.setVisibility(View.VISIBLE);
                    txtEmail.setVisibility(View.VISIBLE);
                    imageGoogle.setVisibility(View.GONE);
                    txtType.setText(String.format("Login Type : Email"));
                    txtUserId.setText(String.format("USER ID : %s", user.getUid()));
                    txtEmail.setText(String.format("Email : %s", user.getEmail()));
                    linName.setVisibility(View.GONE);
                    linPhone.setVisibility(View.GONE);
                    imageFB.setVisibility(View.GONE);
                    return;
                }
                if (ui.getProviderId().equals(FacebookAuthProvider.PROVIDER_ID)) {
                    txtType.setVisibility(View.VISIBLE);
                    txtUserId.setVisibility(View.VISIBLE);
                    txtName.setVisibility(View.VISIBLE);
                    imageFB.setVisibility(View.VISIBLE);
                    imageGoogle.setVisibility(View.GONE);
                    linEmail.setVisibility(View.GONE);
                    linPhone.setVisibility(View.GONE);
                    txtType.setText(String.format("Login Type : Facebook"));
                    txtUserId.setText(String.format("USER ID : %s", user.getUid()));
                    txtName.setText(String.format("Display Name : %s", user.getDisplayName()));
                    Glide.with(this)
                            .load(user.getPhotoUrl())
                            .into(imageFB);
                    return;
                }
                if (ui.getProviderId().equals(PhoneAuthProvider.PROVIDER_ID)) {
                    txtType.setVisibility(View.VISIBLE);
                    txtUserId.setVisibility(View.VISIBLE);
                    txtPhone.setVisibility(View.VISIBLE);
                    imageGoogle.setVisibility(View.GONE);
                    linName.setVisibility(View.GONE);
                    linEmail.setVisibility(View.GONE);
                    imageFB.setVisibility(View.GONE);
                    txtType.setText(String.format("Login Type : Phone"));
                    txtUserId.setText(String.format("USER ID : %s", user.getUid()));
                    txtPhone.setText(String.format("Phone : %s", user.getPhoneNumber()));
                    return;
                }
                if(user.isAnonymous()) {
                    user.isAnonymous();
                    linName.setVisibility(View.GONE);
                    linPhone.setVisibility(View.GONE);
                    imageFB.setVisibility(View.GONE);
                    imageGoogle.setVisibility(View.GONE);
                    txtType.setVisibility(View.VISIBLE);
                    txtUserId.setVisibility(View.VISIBLE);
                    txtType.setText(String.format("Login Type : Anonymous"));
                    txtUserId.setText(String.format("USER ID : %s", user.getUid()));
                    return;
                }
            }
        }
    }
    private void initView() {
        btnLogout=findViewById(R.id.btnLogOut);
        txtName=findViewById(R.id.txtName);
        txtEmail=findViewById(R.id.txtEmail);
        txtPhone=findViewById(R.id.txtPhone);
        txtType=findViewById(R.id.txtType);
        imageFB=findViewById(R.id.imageFB);
        txtUserId=findViewById(R.id.txtUserId);
        linType=findViewById(R.id.linType);
        linUserId=findViewById(R.id.linUserId);
        linName=findViewById(R.id.linName);
        linEmail=findViewById(R.id.linEmail);
        imageGoogle=findViewById(R.id.imageGoogle);
        linPhone=findViewById(R.id.linPhone);
    }
}
