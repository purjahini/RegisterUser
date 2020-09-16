package net.bedev.registeruser

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_main.*
import net.bedev.registeruser.adapter.RvUserAdapter
import net.bedev.registeruser.database.UserEntity
import net.bedev.registeruser.helper.See
import java.io.*


class MainActivity : AppCompatActivity(), RvUserAdapter.RowClickListener {


    lateinit var userActivityViewModel: UserActivityViewModel

    private val IMAGE_CODE_SIM: Int = 1000
    private val IMAGE_CODE_PROFIL: Int = 98
    private val IMAGE_CODE_KTP: Int = 99
    private val CODE_PERMISSION: Int = 100

    var REQUEST_CODE = 11
    var image_uri: Uri? = null


    lateinit var urlTtd: String
    lateinit var urlKtp: String
    lateinit var urlSim: String
    lateinit var urlProfil: String

//    var nipEdit = 0
//    var simEdit: Int = 0
//    var ktpEdit: Int = 0
//    var telpEdit: Int = 0

    var lat: Double? = 0.0
    var lang: Double? = 0.0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userActivityViewModel = ViewModelProviders.of(this).get(UserActivityViewModel::class.java)

        val fotoprofil = intent.getStringExtra(getString(R.string.fotoprofil))
        val nama = intent.getStringExtra(getString(R.string.nama))
        val nip = intent.getStringExtra(getString(R.string.nip))
        val lat = intent.getStringExtra(getString(R.string.lat))
        val lang = intent.getStringExtra(getString(R.string.lang))
        val telp = intent.getStringExtra(getString(R.string.telp))
        val noktp = intent.getStringExtra(getString(R.string.noktp))
        val fotoktp = intent.getStringExtra(getString(R.string.fotoktp))
        val nosim = intent.getStringExtra(getString(R.string.nosim))
        val fotosim = intent.getStringExtra(getString(R.string.fotosim))
        val fotottd = intent.getStringExtra(getString(R.string.fotottd))
        val id = intent.getStringExtra(getString(R.string.id))
        val update = intent.getStringExtra(getString(R.string.update))



        See.log("result intent :$fotoprofil id : $id")

        if (id == null) {
            BtnUpdateUser.visibility = View.GONE
            TvlabelMainFotoTtd.visibility = View.VISIBLE
            RlTtd.visibility = View.VISIBLE
        } else {
            supportActionBar?.title = "Update Data User"
            supportActionBar?.subtitle = "hello $nama"
            BtnUpdateUser.visibility = View.VISIBLE
            BtnSaveUser.visibility = View.GONE
            TvlabelMainFotoTtd.visibility = View.VISIBLE
            IvMainFotoTtd.visibility = View.VISIBLE
            BtnListUser.visibility = View.GONE

            ETNamaUser.setText(nama)
            ETNipUser.setText(nip)
            ETNoTelpUser.setText(telp)
            ETKtpUser.setText(noktp)
            ETNoSim.setText(nosim)

            val folderTtd =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/SignaturePad/" + fotottd)
            val folderSim =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Foto_sim/" + fotosim)
            val folderKtp =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Foto_ktp/" + fotoktp)
            val folderProfil =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Foto_profil/" + fotoprofil)

            val imageUriTtd = Uri.fromFile(folderTtd)
            val imageUriSim = Uri.fromFile(folderSim)
            val imageUriKtp = Uri.fromFile(folderKtp)
            val imageUriProfil = Uri.fromFile(folderProfil)

            Glide.with(this)
                .load(imageUriProfil)
                .into(IvMainFotoProfil)

            Glide.with(this)
                .load(imageUriKtp)
                .into(IvMainFotoKtp)

            Glide.with(this)
                .load(imageUriSim)
                .into(IvMainFotoSim)
            Glide.with(this)
                .load(imageUriTtd)
                .into(IvMainFotoTtd)



        }


        BtnUpdateUser.setOnClickListener {
          val  nipEdit = Integer.parseInt(ETNipUser.text.toString())
            val  telpEdit = Integer.parseInt(ETNoTelpUser.text.toString())
            val   ktpEdit = Integer.parseInt(ETKtpUser.text.toString())
            val  simEdit = Integer.parseInt(ETNoSim.text.toString())
            val user = UserEntity(
                id.toInt(),
                fotoprofil,
                ETNamaUser.text.toString(),
                nipEdit,
                lat,
                lang,
                telpEdit,
                ktpEdit,
                fotoktp,
                simEdit,
                fotosim,
                fotottd
            )
            userActivityViewModel.updateUserInfo(user)

            See.toast(this, "Sukses Update")
            See.log("nip : $nipEdit ,telp :$telpEdit , ktp:$ktpEdit , sim :$simEdit")
            val intent = Intent(this, UserActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }






        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (

                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED

            ) {
                // butuh permisiion code m
                val permission = arrayOf(


                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                requestPermissions(permission, CODE_PERMISSION)
            } else {
                // done permision
                GetLocationMap()
            }

        } else {

            //version code < marshmellow
            GetLocationMap()
        }


        IvMainFotoKtp.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    // butuh permisiion code m
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, IMAGE_CODE_KTP)
                } else {
                    // done permision
                    openCameraKtp()
                }

            } else {
                //version code < marshmellow
                openCameraKtp()
            }

        }

        IvMainFotoProfil.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    // butuh permisiion code m
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, IMAGE_CODE_PROFIL)
                } else {
                    // done permision
                    openCameraProfil()
                }

            } else {
                //version code < marshmellow
                openCameraProfil()
            }
        }
        IvMainFotoSim.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    // butuh permisiion code m
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, IMAGE_CODE_SIM)
                } else {
                    // done permision
                    openCameraSim()
                }

            } else {
                //version code < marshmellow
                openCameraSim()
            }
        }

        signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                See.toast(this@MainActivity, "Tanda tangan dimulai")
            }

            override fun onSigned() {
                save_button.isEnabled = true
                clear_button.isEnabled = true
            }

            override fun onClear() {
                save_button.isEnabled = false
                clear_button.isEnabled = false
            }

        })

        clear_button.setOnClickListener {
            signature_pad.clear()
        }

        save_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    // butuh permisiion code m
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, IMAGE_CODE_SIM)
                } else {
                    // done permision
                    val signatureBitmap: Bitmap = signature_pad.signatureBitmap
                    if (addJpgSignatureToGallery(signatureBitmap)) {
                        Toast.makeText(this, "Signature saved into the Gallery", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Unable to store the signature", Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (addSvgSignatureToGallery(signature_pad.signatureSvg)) {
                        See.toast(this, "tanda tangan SVG tersimpan di Gallery")

                    } else {
                        See.toast(this, "Gagal simpan tanda tangan")
                    }
                }

            } else {
                //version code < marshmellow
                val signatureBitmap: Bitmap = signature_pad.signatureBitmap
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(this, "Signature saved into the Gallery", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Unable to store the signature", Toast.LENGTH_SHORT).show()
                }
                if (addSvgSignatureToGallery(signature_pad.signatureSvg)) {
                    See.toast(this, "tanda tangan SVG tersimpan di Gallery")

                } else {
                    See.toast(this, "Gagal simpan tanda tangan")
                }
            }


        }

        BtnSaveUser.setOnClickListener {
            ProsesSaveUser()

        }

        BtnListUser.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }


    }

    //    @SuppressLint("MissingPermission")
//    private fun setLocation() {
//        fusedLocationClient.lastLocation
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful && task.result != null) {
//                    if (task.result.hasAccuracy()) {
//                        toast(task.result.accuracy.toString()).show()
//                    }
////                        edKoordinat.setText("dsdad")
//                    textKoordinat.setText(task.result.latitude.toString() + "," + task.result.longitude.toString())
//                } else {
//                    toast("No Location detected")
//                }
//            }
//    }
    fun getAlbumStorageDir(albumName: String?): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created $albumName")
        }
        return file
    }

    private fun addJpgSignatureToGallery(signatureBitmap: Bitmap): Boolean {
        var result = false
        try {
            val filename =
                String.format(String.format("Signature_%d.jpg", System.currentTimeMillis()))
            val photo = File(getAlbumStorageDir("SignaturePad"), filename)
            saveBitmapToJPG(signatureBitmap, photo)
            Log.d("Main ", "lokasi foto jpg : ${filename}")
//            scanMediaFile(photo)

            urlTtd = filename
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun addSvgSignatureToGallery(signatureSvg: String?): Boolean {
        var result = false
        try {
            val filename = String.format(
                String.format(
                    "Signature_%d.jpg",
                    System.currentTimeMillis()
                )
            )
            val svgFile = File(getAlbumStorageDir("SignaturePad"), filename)
            val stream: OutputStream = FileOutputStream(svgFile)
            val writer = OutputStreamWriter(stream)
            writer.write(signatureSvg)
            writer.close()
            stream.flush()
            stream.close()
//            scanMediaFile(svgFile)
            Log.d("Main ", "lokasi foto svg : ${svgFile}")
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun openCameraProfil() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "foto")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Dari Kamera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(camera, IMAGE_CODE_PROFIL)
    }

    private fun openCameraSim() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "foto")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Dari Kamera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(camera, IMAGE_CODE_SIM)
    }

    private fun openCameraKtp() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "foto")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Dari Kamera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(camera, IMAGE_CODE_KTP)

    }

    @SuppressLint("MissingPermission")
    private fun GetLocationMap() {
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation.addOnSuccessListener(
            this,
            object : OnSuccessListener<Location> {
                override fun onSuccess(location: Location?) {
                    // Do it all with location
                    Log.d(
                        "My Current location",
                        "Lat : ${location?.latitude} Long : ${location?.longitude}"
                    )
                    lang = location?.longitude
                    lat = location?.latitude
                    // Display in Toast
                    Toast.makeText(
                        this@MainActivity,
                        "Lat : ${location?.latitude} Long : ${location?.longitude}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CODE_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetLocationMap()
                } else {
                    See.toast(this, "Butuh izin lokasi aplikasi")
                }
            }
            IMAGE_CODE_KTP -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permision grade
                    openCameraKtp()
                } else {
                    See.toast(this, "Butuh izin kamera aplikasi")
                }

            }
            IMAGE_CODE_SIM -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permision grade
                    openCameraSim()
                } else {
                    See.toast(this, "Butuh izin kamera aplikasi")
                }
            }
            IMAGE_CODE_PROFIL -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permision grade
                    openCameraProfil()
                } else {
                    See.toast(this, "Butuh izin kamera aplikasi")
                }
            }
        }
    }


    private fun ProsesSaveUser() {
        Pbregister.visibility = View.VISIBLE
        val nip = ETNipUser.text.toString()
        val nama = ETNamaUser.text.toString()
        val noTelp = ETNoTelpUser.text.toString()
        val noKtp = ETKtpUser.text.toString()
        val noSim = ETNoSim.text.toString()

        when {
            nip.isEmpty() -> {
                ETNipUser.error = "Harus di isi"
                return
            }
            nama.isEmpty() -> {
                ETNamaUser.error = "Harus di isi"
                return
            }
            noTelp.isEmpty() -> {
                ETNoTelpUser.error = "Harus di isi"
                return
            }
            noKtp.isEmpty() -> {
                ETKtpUser.error = "Harus di isi"
                return
            }
            noSim.isEmpty() -> {
                ETNoSim.error = "Harus di isi"
                return
            }

            urlKtp == null -> {
                See.toast(this, "Silahkan foto ktp anda")
                return
            }
            urlProfil == null -> {
                See.toast(this, "Silahkan berselfi dulu")
                return
            }
            urlSim == null -> {
                See.toast(this, "Silahkan foto sim anda")
                return
            }
            lat.toString() == null -> {
                See.toast(this, "Silahkan  aktifkan internet anda")
                return
            }
            lang.toString() == null -> {
                See.toast(this, "Silahkan  aktifkan internet anda")
                return
            }

        }

        val user = UserEntity(
            0,
            urlProfil,
            nama,
            nip.toInt(),
            lat.toString(),
            lang.toString(),
            noTelp.toInt(),
            noKtp.toInt(),
            urlKtp,
            noSim.toInt(),
            urlSim,
            urlTtd
        )
        userActivityViewModel.insertUserInfo(user)
        See.log("send user : $lat dan lang : $lang")
        Pbregister.visibility = View.GONE
        BtnListUser.visibility = View.VISIBLE


    }


    override fun onDeleteUserClickListener(user: UserEntity) {

    }

    override fun onItemClickListener(user: UserEntity) {

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_CODE_KTP -> {
                    IvMainFotoKtp.setImageURI(image_uri)
                    See.log("ktp : $image_uri")

                    val filename =
                        String.format(String.format("KTP_%d.jpg", System.currentTimeMillis()))
                    val photo = File(getAlbumStorageDir("Foto_ktp"), filename)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)
                    saveBitmapToJPG(bitmap, photo)
                    urlKtp = filename


                }
                IMAGE_CODE_PROFIL -> {
                    IvMainFotoProfil.setImageURI(image_uri)
                    See.log("profil :$image_uri")

                    val filename =
                        String.format(String.format("PROFIL_%d.jpg", System.currentTimeMillis()))
                    val photo = File(getAlbumStorageDir("Foto_profil"), filename)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)
                    saveBitmapToJPG(bitmap, photo)
                    urlProfil = filename
                }
                IMAGE_CODE_SIM -> {
                    IvMainFotoSim.setImageURI(image_uri)
                    See.log("sim :$image_uri")

                    val filename =
                        String.format(String.format("SIM_%d.jpg", System.currentTimeMillis()))
                    val photo = File(getAlbumStorageDir("Foto_sim"), filename)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)
                    saveBitmapToJPG(bitmap, photo)
                    urlSim = filename
                }
            }
        }


    }

    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0.toFloat(), 0.toFloat(), null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        stream.close()
    }
}