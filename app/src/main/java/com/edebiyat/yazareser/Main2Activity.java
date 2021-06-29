package com.edebiyat.yazareser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class Main2Activity extends AppCompatActivity implements RewardedVideoAdListener {
     FirebaseFirestore firebaseFirestore;
TextView textView;
TextView textView2;
ArrayList<String> writerList;
ArrayList<String> bookList;
Button button;
int sayac=0;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        MobileAds.initialize(this, "ca-app-pub-4667310831133829~3470892179");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4667310831133829/8960050248");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);
        writerList = new ArrayList<>();
        bookList = new ArrayList<>();
        button = findViewById(R.id.button);
        if(isNetworkAvailable()){
            Toast.makeText(Main2Activity.this,"Ekrana dokunarak değiştirebilirsiniz.", Toast.LENGTH_LONG).show();
            getDataFromFirestore();
        }



        else {

            Toast.makeText(Main2Activity.this,"İnternet Bağlantısı Yok", Toast.LENGTH_LONG).show();

        }


    }

    public void getDataFromFirestore() {

        CollectionReference collectionReference = firebaseFirestore.collection("literature");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!= null){
                    Toast.makeText(Main2Activity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

            if(queryDocumentSnapshots != null){


                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){

                    Map<String, Object> data = documentSnapshot.getData();

                    String writer = (String) data.get("writer");
                    String book = (String) data.get("book");
                    writerList.add(writer);
                    bookList.add(book);


                }

                ekranaBas();

            }



            }
        });

    }


    public void ekranaBas(){
        sayac++;
        if(sayac==25){
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.show();
            sayac=7;
        }
        int uzunluk = writerList.size();
        int rastgeleSayi = (int) (Math.random()*(uzunluk));

        String[] book;
        book = (bookList.get(rastgeleSayi) + "/ " + "/ " + "/ " + "/ ").split("/");

        textView.setText("- "+writerList.get(rastgeleSayi)+" -");
        textView2.setText(book[0] + "\n" +book[1] + "\n" +  book[2] + "\n" + book[3] + "\n" + book[4] );


    }



    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd("ca-app-pub-4667310831133829/1992877715", new AdRequest.Builder().build());
        }
    }


    public void button(View view){

        ekranaBas();

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Çıkış Yapmak istiyor musunuz?");
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                // Evet'e basılınca yapılacak işlemleri yazınız
                finish();
            }
        });
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                // Hayır'a baslınca yapılacak işmeleri yazınız
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}


