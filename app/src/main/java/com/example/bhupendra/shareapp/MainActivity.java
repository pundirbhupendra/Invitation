package com.example.bhupendra.shareapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

        public static final String TAG = MainActivity.class.getSimpleName();

        public static final int  REQUEST_INVITE =0;

        private GoogleApiClient mGoogleApiClient;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            findViewById(R.id.invite_button).setOnClickListener(this);
            findViewById(R.id.custom_invite_button).setOnClickListener(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(AppInvite.API)
                    .enableAutoManage(this,this)
                    .build();

            boolean autoLaunchDeepLink = true;

            AppInvite.AppInviteApi.getInvitation(mGoogleApiClient,this,autoLaunchDeepLink)
                    .setResultCallback(
                            new ResultCallback<AppInviteInvitationResult>() {
                                @Override
                                public void onResult(@NonNull AppInviteInvitationResult appInviteInvitationResult) {
                                    Log.d(TAG,"getInviteOnResuilt:" +appInviteInvitationResult.getStatus());
                                }
                            }
                    );

        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


            Log.d(TAG,"onconnectionFailed" +connectionResult);
            showMessage("Google Play Service Error");
    }

    private void onInviteClicked(){

        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage("This app is terrific ,give it a try and get $5 off!")
                .setDeepLink(Uri.parse("http://example.co/offer/five_doller_offer"))
                .setCustomImage(Uri.parse("http://www.google.com/image/branding/googlelogo/2x/go.."))
                .setCallToActionText("Install")
                .build();
        startActivityForResult(intent,REQUEST_INVITE);

    }

    private void onCustomInviteClicked(){

            Intent intent = new AppInviteInvitation.IntentBuilder("Send App Invite Quickstart Invitation")
                    .setMessage("This app is terrific ,give it a try and get $5 off !")
                    .setDeepLink(Uri.parse("http:// example.com/offer/five_doller_offer !/"))
                    .setEmailHtmlContent("<html><body>"+
                    "<h1>App Invites</h1>"
                    + "<a href =\"%% APPINVITE_LINK_PLACEHOLDER %%\">Instaal Now !</a>"+
                    "<body></html>").setEmailSubject("Try this great app 1")
                    .build();
            startActivityForResult(intent,REQUEST_INVITE);

            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG,"onActivityResult: requestcode " +requestCode + ",resultcode" + resultCode);
        if(requestCode == REQUEST_INVITE){

            if(resultCode == RESULT_OK){

                String[] ids = AppInviteInvitation.getInvitationIds(requestCode,data);
                Log.d(TAG,"send (ids.length) invitation");

            } else {

                showMessage("Sending invitation failed");
            }
        }
    }

    private void showMessage(String msg) {

        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);

        Snackbar.make(container,msg,Snackbar.LENGTH_SHORT).show();

        }

    @Override
    public void onClick(View v) {

            switch (v.getId()){

                case R.id.invite_button:
                    onInviteClicked();
                    break;

                    case  R.id.custom_invite_button:

                        onCustomInviteClicked();
                        break;

                    }

                    }

}
