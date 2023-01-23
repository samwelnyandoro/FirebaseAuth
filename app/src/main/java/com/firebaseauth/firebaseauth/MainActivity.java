package com.firebaseauth.firebaseauth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.firebaseauth.firebaseauth.Fragment.anonymouslyFragment;
import com.firebaseauth.firebaseauth.Fragment.facebookFragment;
import com.firebaseauth.firebaseauth.Fragment.gmailFragment;
import com.firebaseauth.firebaseauth.Fragment.mailFragment;
import com.firebaseauth.firebaseauth.Fragment.phoneFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth auth;
    private int[] tabIcons = {
            R.drawable.mail,
            R.drawable.search,
            R.drawable.facebook,
            R.drawable.phone,
            R.drawable.incognito,

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null)
        {
            //value=true;
            Intent in=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(in);
            finish();
        }
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        getSupportActionBar().hide();
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdepter adapter = new PagerAdepter(getSupportFragmentManager());
        adapter.addFragment(new mailFragment(),"mail");
        adapter.addFragment(new gmailFragment(),"google");
        adapter.addFragment(new facebookFragment(),"Facebook");
        adapter.addFragment(new phoneFragment(),"Phone");
        adapter.addFragment(new anonymouslyFragment(),"Incognito");

        viewPager.setAdapter(adapter);
    }
}
