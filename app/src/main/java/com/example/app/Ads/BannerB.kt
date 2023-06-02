package com.example.app

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.gms.ads.*

object BannerB {
    fun bannerAdmob(mAdView: AdView, txtView: TextView) {

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {}
            override fun onAdClosed() {}
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("BannerFailed", "Admob")
                txtView.visibility = View.VISIBLE
                mAdView.visibility = View.GONE

            }

            override fun onAdImpression() {}

            override fun onAdLoaded() {
                Log.d("BannerLoaded", "Admob")
                txtView.visibility = View.GONE
                mAdView.visibility = View.VISIBLE


            }

            override fun onAdOpened() {}
        }
        fun startnewfragment(context: Context, navDirections: NavDirections, navController: NavController)
        {
            InterstitialClass.show_interstitial(context){
                Handler().postDelayed({
                    navController.navigate(navDirections)
                }, 50)
            }

        }
    }
}