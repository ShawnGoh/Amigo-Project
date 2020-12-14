package com.example.infosys1d_amigoproject.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.signinsignup.SignIn;
import com.example.infosys1d_amigoproject.signinsignup.SignUp;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

//Onboarding tablayout container activity
public class Onboarding extends AppCompatActivity {

    private ViewPager2 onboardingscroller;
    Button SignupBtn, SigninBtn;
    OnboardingViewPagerAdapter onboardingViewPagerAdapter;
    TabLayout tabindicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_onboarding);

        //Initialize other screen UI elements
        SigninBtn = findViewById(R.id.OnboardingSigninButton);
        SignupBtn = findViewById(R.id.OnboardingSignupButton);
        tabindicator = findViewById(R.id.OnboardingPageIndicator);

        try //to remove top title bar
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}


        //fill onboardingList items
        ArrayList<OnboardingItem> onboardingList = new ArrayList<>();
        onboardingList.add(new OnboardingItem("DISCOVER", "Discover other projects that can help you improve on your skills and interests!\n", R.drawable.onboarding_discover));
        onboardingList.add(new OnboardingItem("CREATE", "Create projects that you are looking to initiate", R.drawable.onboarding_create));
        onboardingList.add(new OnboardingItem("CONNECT", "Meet new people to connect with and collaborate!", R.drawable.onboarding_connect));
        onboardingList.add(new OnboardingItem("What are you waiting for?", "Sign up and start creating now!", R.drawable.onboarding_wait));


        //Initialize onboarding scroller
        onboardingscroller =  findViewById(R.id.OnboardingScroller);
        onboardingViewPagerAdapter = new OnboardingViewPagerAdapter(this, onboardingList);
        onboardingscroller.setAdapter(onboardingViewPagerAdapter);

        new TabLayoutMediator(tabindicator, onboardingscroller,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText("     " );
                    }
                }
        ).attach();


        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Onboarding.this, SignIn.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                saveprefstate();
                finish();
            }
        });

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                saveprefstate();
                finish();
            }
        });
        }

    private boolean restorePrefData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        Boolean isuserintroed = pref.getBoolean("isIntroed", false);
        return isuserintroed;
    }

    public void saveprefstate(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroed", true);
        editor.commit();
    }

    }
