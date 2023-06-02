package com.example.app

import android.app.Dialog
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Response
import com.bumptech.glide.Glide
import com.example.app.databinding.ActivityEidWallpaperMainBinding
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EidWallpaperMain : AppCompatActivity() {
    val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    val rotateClose: Animation by lazy{ AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)}
    private var clicked=false
    lateinit var binding:ActivityEidWallpaperMainBinding
    lateinit var adapter: EidAdapter
    var EidImages=ArrayList<EidAlbum>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEidWallpaperMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        BannerB.bannerAdmob(binding.adViewE,binding.tvAdE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        adapter= EidAdapter {
            Glide.with(this).load(it.url).placeholder(R.drawable.ic_launcher_background).into(binding.imageEid)
            binding.Eidrecylerview.visibility= View.INVISIBLE
            binding.imageEid.visibility=View.VISIBLE
            binding.floatingButtonEid.visibility=View.VISIBLE
            binding.floatingButtonEid.setOnClickListener {
                onAddButtonClicked()
            }
            binding.floatingButtonShareEid.setOnClickListener {
                shareappEid()
            }
            binding.floatingButtonsetwallpaperEid.setOnClickListener {
                dialogEid()

            }
        }
        EidImages= ArrayList()
        getResponse()
    }
    private fun getResponse() {
        val responseListener: Response.Listener<*>
        responseListener = Response.Listener<JSONObject> { response ->
            val responseObj = response

            val pages: MutableList<EidAlbum> = ArrayList()
            val jsonArray = responseObj.getJSONArray("hd_wallpapers")
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val title = item.getString("title")
                val url = item.getString("url")
                val data = EidAlbum(title,url)
                pages.add(data)
            }
            Log.d("pagess",pages.toString())

            try {
                adapter.getdetailsEid(pages)
                binding.Eidrecylerview.layoutManager= GridLayoutManager(this,2)
                binding.Eidrecylerview.adapter=adapter

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
//    fun addPics()
//    {
//
//        val cache = DiskBasedCache(cacheDir, 1024 * 1024)
//        val network = BasicNetwork(HurlStack())
//        val queue = RequestQueue(cache, network)
//
////        val queue= Volley.newRequestQueue(this)
//        val url="https://solutionoftechnologies.com/islamicwallpaper/wallpaper.json"
//
//        val request= JsonObjectRequest(Request.Method.GET,url,null,{ response->
////            Log.e("Response", "getData ${response.toString()}")
//
//            var jsonobj = JSONObject()
//            val jsonArray= response.getJSONArray("hd_wallpapers")
//            Log.e("Response", "getData ${jsonArray.toString()}")
//            for(i in 0 until jsonArray.length()){
//
//                jsonobj=jsonArray.getJSONObject(i)
//                Log.e("ResponsQ", "getData ${jsonobj}")
//
//                var usereid=EidAlbum(
//                    jsonobj.getString("title"),
//                    jsonobj.getString("url")
//                )
//
//                EidImages.add(usereid)
//                adapter.getdetailsEid(EidImages)
//                binding.Eidrecylerview.layoutManager= GridLayoutManager(this,2)
//                binding.Eidrecylerview.adapter=adapter
//            }
//        },{error->
//
//        })
//        queue.start()
//        queue.add(request)
//
//    }
    fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }
    fun setVisibility(clicked: Boolean)
    {
        if(!clicked){
            binding.floatingButtonShareEid.visibility=View.VISIBLE
            binding.floatingButtonsetwallpaperEid.visibility=View.VISIBLE
        }
        else{
            binding.floatingButtonShareEid.visibility=View.GONE
            binding.floatingButtonsetwallpaperEid.visibility=View.GONE
        }
    }
    fun setAnimation(clicked: Boolean)
    {
        if(!clicked)
        {
            binding.floatingButtonEid.startAnimation(rotateOpen)
        }
        else
        {
            binding.floatingButtonEid.startAnimation(rotateClose)
        }

    }
    fun shareappEid()
    {
        val drawable: Drawable = binding.imageEid.getDrawable()


        val bitmap = (drawable as BitmapDrawable).bitmap
        val file = File(externalCacheDir, "image.jpg")
        try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    fos
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, "Share image"))

    }
     fun setWallpaperEid(view:View){
        val v = binding.imageEid.drawable.toBitmap()
        val wallpaperManager = WallpaperManager.getInstance(baseContext)
        wallpaperManager.setBitmap(v)
        Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
         this.finish()
    }
    fun setwallpaperLock(){
        val v =binding.imageEid.drawable.toBitmap()
        val wallpaperManager=WallpaperManager.getInstance(baseContext)
        wallpaperManager.setBitmap(v,null,true, WallpaperManager.FLAG_LOCK)
        Toast.makeText(this,"Wallpaper set!",Toast.LENGTH_SHORT).show()
        this.finish()
    }
    fun dialogEid()
    {
        val dialog=Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_setwallpaper)
        dialog.setCancelable(true)
        val homescreen:TextView=dialog.findViewById(R.id.HomescreenD)
        val lockscreen:TextView=dialog.findViewById(R.id.lockscreenD)
        val lockandhome:TextView=dialog.findViewById(R.id.homeandlockscreenD)

        homescreen.setOnClickListener {
            setWallpaperEid(binding.root)
        }
        lockscreen.setOnClickListener {
            setwallpaperLock()
        }
        lockandhome.setOnClickListener {
            setWallpaperEid(binding.root)
            setwallpaperLock()
        }
        dialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        androidx.appcompat.R.id.home
        this.finish()
        InterstitialClass.show_interstitial(this){}
        return true
    }
}