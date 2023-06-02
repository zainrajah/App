package com.example.app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.google.ads.AdSize.BANNER
import com.google.android.gms.ads.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.component.KoinComponent


@SuppressLint("StaticFieldLeak")
object bannerAds : CoroutineScope by MainScope(), KoinComponent {
    var bannnerAdView: Any? = null
    var mPuraContainer: RelativeLayout? = null
    var adView: AdView? = null
    var fbAdView: com.facebook.ads.AdView? = null
    var isRequestset = false

    private var mContext: Context? = null
    fun initializeAdds(
        adlayout: RelativeLayout,
        mContext: Context,
        bannerLayout: LinearLayout,
        tvAdLabel: TextView
    ) {
        bannerAds.mContext = mContext
        showBanner(bannerLayout,mContext,tvAdLabel,adlayout)
    }


    fun showBanner(
        bannerLayout: LinearLayout,
        context: Context,
        tvAdLabel: TextView,
        adlayout: RelativeLayout
    ) {
        mContext = context
//               Handler().postDelayed({
        loadAdMobBanner(bannerLayout, tvAdLabel)
        Log.d("Ads_", "condition 1 called")
//               }, 13000)
    }

    private fun loadAdMobBanner(bannerLayout: LinearLayout, tvAdLabel: TextView) {

        if (!isRequestset) {
            isRequestset = true
            adView = AdView(mContext!!)
            adView!!.adUnitId = mContext!!.getString(R.string.admob_banner)

            val requestConfiguration = RequestConfiguration.Builder()
                .build()

            MobileAds.setRequestConfiguration(requestConfiguration)

            //  adView!!.adSize = adaptiveAds.adSize
            adView!!.setAdSize(com.google.android.gms.ads.AdSize.BANNER)
            adView!!.loadAd(AdRequest.Builder().build())
            adView!!.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.d("Ads_", "Keyboard Banner Loaded ")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("Ads_", "Keyboard Failed to Load $adError")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    bannnerAdView = null
                }
            }

        }


    }


}





