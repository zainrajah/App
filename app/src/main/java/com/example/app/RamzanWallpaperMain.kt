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
import com.example.app.databinding.ActivityRamzanWallpaperMainBinding
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class RamzanWallpaperMain : AppCompatActivity() {
    val rotateOpen:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    val rotateClose:Animation by lazy{ AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)}
    val fromBottom:Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.from_bottom)}
    val toBottom:Animation by lazy{AnimationUtils.loadAnimation(this,R.anim.to_bottom)}
    private var clicked=false
    lateinit var binding: ActivityRamzanWallpaperMainBinding
    lateinit var adapter: RamzanAdapter
    var ramzanImages= ArrayList<RamzanAlbum>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRamzanWallpaperMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        BannerB.bannerAdmob(binding.adViewR,binding.tvAdR)

        adapter=RamzanAdapter{
//            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            Glide.with(this).load(it.url).placeholder(R.drawable.icon2).error(R.drawable.icon).fallback(R.drawable.icon2).into(binding.imageone)
            binding.ramzanRecylerView.visibility= View.INVISIBLE
            binding.imageone.visibility=View.VISIBLE
            binding.floatingButton.visibility=View.VISIBLE
            binding.floatingButton.setOnClickListener {
                onAddButtonClicked()

            }
            binding.floatingButtonShare.setOnClickListener {
                shareapp()

            }

            binding.floatingButtonsetwallpaper.setOnClickListener {
                dialogshow()
            }

        }
        ramzanImages=ArrayList()
//        addPics()
        getResponse()
    }
//    fun addPics()
//    {
//        val queue= Volley.newRequestQueue(this)
//        val url="https://solutionoftechnologies.com/islamicwallpaper/wallpaper.json"
//        val request=JsonObjectRequest(Request.Method.GET,url,null,{response->
////            Log.e("Response", "getData ${response.toString()}")
//            var jsonobj = JSONObject()
//            val jsonArray= response.getJSONArray("hd_wallpapers")
//            Log.e("Response", "getData ${jsonArray.toString()}")
//            for(i in 0 until jsonArray.length()){
//                 jsonobj=jsonArray.getJSONObject(i)
//                Log.e("ResponsQ", "getData ${jsonobj}")
//
//                var user=RamzanAlbum(
//                    jsonobj.getString("title"),
//                    jsonobj.getString("url")
//                )
//                ramzanImages.add(user)
//                adapter.getDetails(ramzanImages)
//                binding.ramzanRecylerView.layoutManager= GridLayoutManager(this,2)
//                binding.ramzanRecylerView.adapter=adapter
////                ramzanImages.add(RamzanAlbum(jsonobj.url))
//            }
//        },{error->
//
//        })
//        queue.add(request)
//    }

    private fun getResponse() {
        val responseListener: Response.Listener<*>
        responseListener = Response.Listener<JSONObject> { response ->
            val responseObj = response
            val images: MutableList<RamzanAlbum> = ArrayList()
            val jsonArray = responseObj.getJSONArray("hd_wallpapers")
            for (i in 0 until jsonArray.length()) {
                val RamzanImages = jsonArray.getJSONObject(i)
                val title = RamzanImages.getString("title")
                val url = RamzanImages.getString("url")
                val dataRamzan = RamzanAlbum(title, url)
                images.add(dataRamzan)
            }
            Log.d("images",images.toString())

            try {
                adapter.getDetails(images as ArrayList<RamzanAlbum>)
                binding.ramzanRecylerView.layoutManager= GridLayoutManager(this,2)
                binding.ramzanRecylerView.adapter=adapter

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

    fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }
    fun setVisibility(clicked: Boolean)
    {
        if(!clicked){
            binding.floatingButtonShare.visibility=View.VISIBLE
            binding.floatingButtonsetwallpaper.visibility=View.VISIBLE
        }
        else{
            binding.floatingButtonShare.visibility=View.INVISIBLE
            binding.floatingButtonsetwallpaper.visibility=View.INVISIBLE
        }

    }
    fun setAnimation(clicked: Boolean)
    {
        if(!clicked)
        {
//            binding.floatingButtonShare.startAnimation(fromBottom)
//            binding.floatingButtonsetwallpaper.startAnimation(fromBottom)
            binding.floatingButton.startAnimation(rotateOpen)
        }
        else
        {
//            binding.floatingButtonShare.startAnimation(toBottom)
//            binding.floatingButtonsetwallpaper.startAnimation(toBottom)
            binding.floatingButton.startAnimation(rotateClose)
        }

    }
    fun shareapp()
    {
       val drawable:Drawable= binding.imageone.getDrawable()
        val bitmap=(drawable as BitmapDrawable).bitmap
        val file=File(externalCacheDir,"image.jpg")
        try{
            FileOutputStream(file).use{fos->
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
    fun setWallpaper(view:View){
        val v = binding.imageone.drawable.toBitmap()
    //    val bitmap:Bitmap= BitmapFactory.
        val wallpaperManager = WallpaperManager.getInstance(baseContext)
        wallpaperManager.setBitmap(v)
        Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
        var intent=Intent(this@RamzanWallpaperMain, MainActivity::class.java)
//        wallpaperManager.setBitmap(v, null, true, WallpaperManager.FLAG_LOCK)
        this.finish()
        startActivity(intent)

    }
    fun setWallpaperLockScreen(){
        val v = binding.imageone.drawable.toBitmap()
        //    val bitmap:Bitmap= BitmapFactory.
        val wallpaperManager = WallpaperManager.getInstance(baseContext)
        wallpaperManager.setBitmap(v)
        Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
        var intent=Intent(this@RamzanWallpaperMain, MainActivity::class.java)
        wallpaperManager.setBitmap(v, null, true, WallpaperManager.FLAG_LOCK)
        this.finish()
        startActivity(intent)
    }
    fun dialogshow()
    {
        val dialog=Dialog(this)
        dialog. requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_setwallpaper)
        dialog.setCancelable(true)
        val homeScreen:TextView=dialog.findViewById(R.id.HomescreenD)
        val lockScreen:TextView=dialog.findViewById(R.id.lockscreenD)
        val lockhomeScreen:TextView=dialog.findViewById(R.id.homeandlockscreenD)

        homeScreen.setOnClickListener{
            setWallpaper(binding.root)
        }
        lockScreen.setOnClickListener {
            setWallpaperLockScreen()
        }
        lockhomeScreen.setOnClickListener {
            setWallpaper(binding.root)
            setWallpaperLockScreen()
        }
        dialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem):Boolean {
       androidx.appcompat.R.id.home
        InterstitialClass.show_interstitial(this){}
        this.finish()
        return true
    }

}





