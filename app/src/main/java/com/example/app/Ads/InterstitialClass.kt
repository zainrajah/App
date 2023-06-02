package com.example.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@SuppressLint("StaticFieldLeak")
object InterstitialClass : CoroutineScope by MainScope(), KoinComponent {
    var admobInterstitialAd: InterstitialAd? = null
    var fbInterstitialAd: com.facebook.ads.InterstitialAd? = null
    var mContext: Context? = null
    var logTag = "Ads_"
    var isRequestSent = false
    var isAlreadyLoaded = false
    var isAdmobLoaded = false
    var isFbLoaded = false
    var isInterstitialAdVisible = false
    val tinyDB: TinyDB by inject()
    var nowTime: Long = 0
    var mActionOnAdClosedListener: (() -> Unit)? = null
    var mActionOnAdLoadedListener: (() -> Unit)? = null
    var dialog_show_time = 1000 //
    var isShowDialog = true
    var isSplashAd = true


    // 2 seconds
    fun load_interstitial(context: Context?) {
        mContext = context
        isSplashAd =false
        nowTime = System.currentTimeMillis()
        if (tinyDB.getBoolean("isFirstAd", true)) {
            if (checkInternetConnected(mContext)) {
                if (!isAlreadyLoaded && !isRequestSent) {
                    if (!isAlreadyLoaded) { // if enabled(true) from firebase
                        load_admob_interstitial()
                    } else if (!isAlreadyLoaded) { // if enabled(true) from firebase
             //           load_fb_interstitial()
                    }
                }
            }
        } else {
            // if is not first time ad. then show after ad_delay time.
            if (nowTime >= tinyDB.getLong(
                    "previousAdTime",
                    0
                ) + tinyDB.getInt("ad_delay", 8) * 1000L
            ) {
                if (checkInternetConnected(mContext)) {
                    if (!isAlreadyLoaded && !isRequestSent) {
                        if (!isAlreadyLoaded) { // if enabled(true) from firebase
                            load_admob_interstitial()
                        } else if (!isAlreadyLoaded) { // if enabled(true) from firebase
                  //          load_fb_interstitial()
                        }
                    }
                }
            }
        }
    }

    fun load_interstitial_splash(context: Context?, actionOnAdLoadedListener: () -> Unit) {
        mContext = context
        isSplashAd =true
        mActionOnAdLoadedListener = actionOnAdLoadedListener
        nowTime = System.currentTimeMillis()
        if (checkInternetConnected(mContext)) {
            if (!isAlreadyLoaded && !isRequestSent) {
                if (!isAlreadyLoaded) { // if enabled(true) from firebase
                    load_admob_interstitial()
                } else if (!isAlreadyLoaded) { // if enabled(true) from firebase
             //       load_fb_interstitial()
                }
            }
        }
    }


    private fun load_admob_interstitial() {
        if (!isAlreadyLoaded && !isRequestSent) {
            isRequestSent = true
            //------------ Admob Starts ------------------
            val adRequest_interstitial = AdRequest.Builder().build()
            InterstitialAd.load(
                mContext!!, mContext!!.getString(R.string.interstitial), adRequest_interstitial,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        admobInterstitialAd = interstitialAd
                        Log.d(logTag, "Admob Interstitial Loaded.")
                        isAlreadyLoaded = true
                        isAdmobLoaded = true
                        if (isSplashAd){
                            mActionOnAdLoadedListener?.invoke()
                        }

                    }
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error
                        Log.d(
                            logTag,
                            "Admob Interstitial Failed to Load." + loadAdError.message
                        )
                        isAlreadyLoaded = false
                        isAdmobLoaded = false
                        isRequestSent = false
                //        load_fb_interstitial()
                    }
                })
            //------------ Admob Ends ------------------
        }
    }

/*
    private fun load_fb_interstitial() {
        if (!isAlreadyLoaded && !isRequestSent) {
            isRequestSent = true

            //------------ FB Starts -------------------
            fbInterstitialAd = com.facebook.ads.InterstitialAd(
                mContext,
                mContext!!.getString(R.string.fb_interstitial)
            )
            val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
                override fun onInterstitialDisplayed(ad: Ad) {
                    // Interstitial ad displayed callback
                    Log.e(logTag, "FB Interstitial ad displayed.")
                }

                override fun onInterstitialDismissed(ad: Ad) {
                    // Interstitial dismissed callback
                    Log.e(logTag, "FB Interstitial ad dismissed.")
                    tinyDB.putBoolean("isFirstAd", false)
                    tinyDB.putLong("previousAdTime", System.currentTimeMillis())
                    isAlreadyLoaded = false
                    isFbLoaded = false
                    isRequestSent = false
                    isInterstitialAdVisible = false
                    load_interstitial(mContext)
                    mActionOnAdClosedListener?.invoke()

                }

                override fun onError(ad: Ad, adError: AdError) {
                    // Ad error callback
                    Log.e(logTag, "FB Interstitial ad failed to load: " + adError.errorMessage)
                    isAlreadyLoaded = false
                    isFbLoaded = false
                    isRequestSent = false
                    if (isSplashAd){
                        mActionOnAdLoadedListener?.invoke()
                    }
                }

                override fun onAdLoaded(ad: Ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(logTag, "FB Insterstitial Loaded.")
                    isAlreadyLoaded = true
                    isFbLoaded = true
                    if (isSplashAd){
                        mActionOnAdLoadedListener?.invoke()
                    }
                }

                override fun onAdClicked(ad: Ad) {
                    // Ad clicked callback
                    Log.d(logTag, "FB Interstitial ad clicked!")
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Ad impression logged callback
                    Log.d(logTag, "FB Interstitial ad impression logged!")
                    isInterstitialAdVisible = true
                }
            }

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            fbInterstitialAd!!.loadAd(
                fbInterstitialAd!!.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                    .build()
            )
            //------------ FB Ends -------------------
        }
    }
*/


    fun show_interstitial(
        context: Context?,
        actionOnAdClosedListener: () -> Unit
    ) {
        isShowDialog = true
        mContext = context
        mActionOnAdClosedListener = actionOnAdClosedListener

        nowTime = System.currentTimeMillis()
        if (isAdmobLoaded) {
            show_admob_intestitial()
        } else if (isFbLoaded) {
            show_fb_interstitial()
        } else {
            load_interstitial(mContext)
            mActionOnAdClosedListener?.invoke()
        }
    }

    fun show_interstitial_backpress(
        context: Context?,
        actionOnAdClosedListener: () -> Unit
    ) {
        isShowDialog = true
        mContext = context
        mActionOnAdClosedListener = actionOnAdClosedListener

        nowTime = System.currentTimeMillis()
        if (isAdmobLoaded) {
            show_admob_intestitial()
        } else if (isFbLoaded) {
            show_fb_interstitial()
        } else {
            mActionOnAdClosedListener?.invoke()
        }
    }


    private fun show_admob_intestitial() {
        showWaitDialog()
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                dismissWaitDialog()
                if (admobInterstitialAd != null) {
                    admobInterstitialAd!!.show(mContext as Activity)
                    isInterstitialAdVisible = true
                    admobInterstitialAd!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            tinyDB.putBoolean("isFirstAd", false)
                            tinyDB.putLong("previousAdTime", System.currentTimeMillis())
                            Log.d(logTag, "Admob Interstitial Closed.")
                            isInterstitialAdVisible = false
                            load_interstitial(mContext)
                            mActionOnAdClosedListener?.invoke()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Log.d(logTag, "Admob Interstitial Failed to Show.")
                            isInterstitialAdVisible = false
                            mActionOnAdClosedListener?.invoke()
                        }
                    })
                } else {
                    mActionOnAdClosedListener?.invoke()
                }
                isAlreadyLoaded = false
                isAdmobLoaded = false
                isRequestSent = false
            }, dialog_show_time.toLong())
        } catch (e: Exception) {
            Log.d(logTag, "Exception: " + e.message)
            dismissWaitDialog()
        }
    }

    private fun show_fb_interstitial() {
        showWaitDialog()
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                dismissWaitDialog()
                if (fbInterstitialAd != null) {
                    fbInterstitialAd!!.show()
                    isInterstitialAdVisible = true
                } else {
                    mActionOnAdClosedListener?.invoke()
                }
                isAlreadyLoaded = false
                isFbLoaded = false
                isRequestSent = false
            }, dialog_show_time.toLong())
        } catch (e: Exception) {
            Log.d(logTag, "Exception: " + e.message)
            dismissWaitDialog()
        }
    }


    private fun showWaitDialog() {
        if (isShowDialog) {
            val view = (mContext as Activity)?.layoutInflater?.inflate(
                R.layout.ad_show_dialog,
                null,
                false
            )
            CustomDialog.getInstance(mContext as Activity)?.setContentView(view, false, 0.7f)?.showDialog()
        }
    }

    fun dismissWaitDialog() {
        CustomDialog.getInstance(mContext as android.app.Activity)?.dismissDialog()
    }

    fun checkInternetConnected(context: Context?): Boolean {
        val connMgr: ConnectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connMgr != null) {
            val activeNetworkInfo: NetworkInfo? = connMgr.getActiveNetworkInfo()
            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                return if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    true
                } else activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE
            }
        }
        return false
    }


}