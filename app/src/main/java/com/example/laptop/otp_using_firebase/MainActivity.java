package com.example.laptop.otp_using_firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //FirebaseApp.initializeApp(this);//run krke dekho ayega
    private FirebaseAuth Auth;
    EditText Phone, otp;
    String verificationCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Phone = (EditText) findViewById(R.id.editText1);
        otp = (EditText) findViewById(R.id.editText2);
        Auth = FirebaseAuth.getInstance();

        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("saurabh", phoneAuthCredential.toString());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("saurabh2", e.toString());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Log.d("saurabh","Code sent to number");
                Toast.makeText(getApplicationContext(), "Code sent to number", Toast.LENGTH_SHORT).show();
            }

        };
    }

    public void Send_sms(View v) {
        String number = Phone.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, this, mcallback);
    }

    public void signInwithPhone(PhoneAuthCredential credential) {
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "signed In successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void verify(View v) {
        String inputcode = otp.getText().toString();
        if (verificationCode.equals(""))
            veryfycode(verificationCode, inputcode);
    }

    public void veryfycode(String verificationCode, String inputCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, inputCode);
        signInwithPhone(credential);
    }
}