package com.example.app

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.app.NewNativeAdClass.adNativeAd
import com.example.app.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        adNativeAd(
            this, false, binding.nativeAdContainerAd,
            false
        ) {
            binding.nativeAdContainerAd.setVisibility(View.GONE)
            null
        }

        binding.ramzanbox.setOnClickListener {
            ramzanImage()
        }

        binding.eidbox.setOnClickListener {
            eidImage()

        }
        binding.islamicWalImg.setOnClickListener {
            islImg()

        }

        val drawerLayout: DrawerLayout=binding.drawerLayoutMainActivity
        val navView:NavigationView=binding.navigation

        toggle= ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open,R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navhome->drawerLayout.closeDrawers()
                R.id.navRateUS->navRateUS()
                R.id.navShareApp->navShareApp()
                R.id.navMoreApps->MoreApps()
                R.id.navFeedback->FeedBack()
                R.id.navPrivacyPolicy->privacyPolicy()
                R.id.navExit->exitDialog()
            }
            true
        }

    }


    fun ramzanImage()
    {
        InterstitialClass.show_interstitial(this) {
            var intent = Intent(this@MainActivity, RamzanWallpaperMain::class.java)
            startActivity(intent)
        }
    }
    fun eidImage()
    {
        InterstitialClass.show_interstitial(this){
            var intent=Intent(this@MainActivity,EidWallpaperMain::class.java)
            startActivity(intent)
        }

    }
    fun islImg()
    {
        InterstitialClass.show_interstitial(this) {
            var intent = Intent(this@MainActivity, IslamicWalpaperMain::class.java)
            startActivity(intent)
        }

    }
    fun navRateUS()
    {
        Toast.makeText(this@MainActivity,"Rateus clicked",Toast.LENGTH_SHORT).show()
    }
    fun navShareApp()
    {
        Toast.makeText(this@MainActivity,"Share App Clicked",Toast.LENGTH_SHORT).show()
    }
    fun MoreApps()
    {
        Toast.makeText(this@MainActivity,"more Apps clicked",Toast.LENGTH_SHORT).show()
    }
    fun FeedBack()
    {
        Toast.makeText(this@MainActivity,"Feedback clicked",Toast.LENGTH_SHORT).show()
    }
    fun privacyPolicy()
    {
        Toast.makeText(this@MainActivity,"Privacy Policy clicked",Toast.LENGTH_SHORT).show()

    }
    fun exitDialog()
    {
        val dialog=Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_exit)
        dialog.setCancelable(false)

        val exitTxt:TextView=dialog.findViewById(R.id.ExitexitApp)
        val rateusTxt:TextView=dialog.findViewById(R.id.rateusExit)
        val cancelTxt:TextView=dialog.findViewById(R.id.cancelExit)

        exitTxt.setOnClickListener {
            finishAffinity()

        }
        rateusTxt.setOnClickListener {
            Toast.makeText(this,"Rate Us",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onBackPressed() {
        exitDialog()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}