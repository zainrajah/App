package com.example.app

import android.app.Dialog
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.app.databinding.ActivityIslamicWalpaperMainBinding
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class IslamicWalpaperMain : AppCompatActivity() {
    val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    val rotateClose: Animation by lazy{ AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)}
    val fromBottom: Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.from_bottom)}
    val toBottom: Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.to_bottom)}
    private var clicked=false
    lateinit var binding:ActivityIslamicWalpaperMainBinding
    lateinit var adapter: IslAdapter

    var islamImages=ArrayList<IslAlbum>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityIslamicWalpaperMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        BannerB.bannerAdmob(binding.adViewI,binding.tvAdI)
        adapter = IslAdapter {
            Glide.with(this).load(it.url).placeholder(R.drawable.icon).into(binding.imageislam)

            binding.islamRecycle.visibility = View.INVISIBLE
            binding.imageislam.visibility = View.VISIBLE
            binding.floatingButtonI.visibility = View.VISIBLE
            binding.floatingButtonI.setOnClickListener {

                onAddButtonClicked()

            }
            binding.floatingButtonShareI.setOnClickListener {
                shareappI()

            }
            binding.floatingButtonsetwallpaperI.setOnClickListener {
                dialogI()

            }

        }
        islamImages = ArrayList()
//        getDataI()
        getResponse()




    }
    private fun getResponse() {
        val responseListener: Response.Listener<*>
        responseListener = Response.Listener<JSONObject> { response ->
            val responseObj = response
            val images: MutableList<IslAlbum> = ArrayList()
            val jsonArray = responseObj.getJSONArray("hd_wallpapers")
            for (i in 0 until jsonArray.length()) {
                val itemIslAlbum = jsonArray.getJSONObject(i)
                val title = itemIslAlbum.getString("title")
                val url = itemIslAlbum.getString("url")
                val dataI = IslAlbum(title, url)
                images.add(dataI)
            }
            Log.d("images",images.toString())

            try {
                adapter.getDetailsIsl(images)
                binding.islamRecycle.layoutManager= GridLayoutManager(this,2)
                binding.islamRecycle.adapter=adapter

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val errorListener: Response.ErrorListener = Response.ErrorListener { }
        ApiCall.getApiData(
            this,
            "https://solutionoftechnologies.com/islamicwallpaper/wallpaper.json",
            responseListener,
            errorListener
        )
    }


    private fun getDataI() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://solutionoftechnologies.com/islamicwallpaper/wallpaper.json"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            var jsonobj = JSONObject()
            val jsonArray = response.getJSONArray("hd_wallpapers")
            Log.e("Response", "getData ${jsonArray.toString()}")
            for (i in 0 until jsonArray.length()) {
                jsonobj = jsonArray.getJSONObject(i)
                Log.e("ResponsQ", "getData ${jsonobj}")

                var user = IslAlbum(
                    jsonobj.getString("title"),
                    jsonobj.getString("url")
                )
                islamImages.add(user)
                adapter.getDetailsIsl(islamImages)
                binding.islamRecycle.layoutManager = GridLayoutManager(this, 2)
                binding.islamRecycle.adapter = adapter
            }

        },
            { error ->

            })

        queue.add(request)

    }
    fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }
    fun setVisibility(clicked: Boolean)
    {
        if(!clicked){
            binding.floatingButtonShareI.visibility=View.VISIBLE
            binding.floatingButtonsetwallpaperI.visibility=View.VISIBLE
        }
        else{
            binding.floatingButtonShareI.visibility=View.INVISIBLE
            binding.floatingButtonsetwallpaperI.visibility=View.INVISIBLE
        }

    }
    fun setAnimation(clicked: Boolean)
    {
        if(!clicked)
        {
//            binding.floatingButtonShare.startAnimation(fromBottom)
//            binding.floatingButtonsetwallpaper.startAnimation(fromBottom)
            binding.floatingButtonI.startAnimation(rotateOpen)
        }
        else
        {
//            binding.floatingButtonShare.startAnimation(toBottom)
//            binding.floatingButtonsetwallpaper.startAnimation(toBottom)
            binding.floatingButtonI.startAnimation(rotateClose)
        }

    }

    fun shareappI()
    {
       val drawable:Drawable=binding.imageislam.getDrawable()
        val bitmap=(drawable as BitmapDrawable).bitmap
        val file =File(externalCacheDir,"image.jpg")
        try{
            FileOutputStream(file).use{ fos->
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="image/*"
        val uri=FileProvider.getUriForFile(this,"$packageName.provider",file)
        intent.putExtra(Intent.EXTRA_STREAM,uri)
        startActivity(Intent.createChooser(intent,"Share Image"))
    }
    fun setWallpaperI(view:View){
        val v = binding.imageislam.drawable.toBitmap()
        //    val bitmap:Bitmap= BitmapFactory.
        val wallpaperManager = WallpaperManager.getInstance(baseContext)
        wallpaperManager.setBitmap(v)
        Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
        this.finish()
    }

    fun setwallpaperLock(){
        val v =binding.imageislam.drawable.toBitmap()
        val wallpaperManager= WallpaperManager.getInstance(baseContext)
        wallpaperManager.setBitmap(v,null,true, WallpaperManager.FLAG_LOCK)
        Toast.makeText(this,"Wallpaper set!",Toast.LENGTH_SHORT).show()
        this.finish()
    }
    fun dialogI()
    {
        val dialog= Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_setwallpaper)
        dialog.setCancelable(true)

        val homescreen: TextView =dialog.findViewById(R.id.HomescreenD)
        val lockscreen: TextView =dialog.findViewById(R.id.lockscreenD)
        val lockandhome: TextView =dialog.findViewById(R.id.homeandlockscreenD)

        homescreen.setOnClickListener {
            setWallpaperI(binding.root)
        }
        lockscreen.setOnClickListener {
            setwallpaperLock()
        }
        lockandhome.setOnClickListener {
            setWallpaperI(binding.root)
            setwallpaperLock()
        }
        dialog.show()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        androidx.appcompat.R.id.home
        InterstitialClass.show_interstitial(this){}
        finish()
        return true
    }
}