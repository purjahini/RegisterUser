package net.bedev.registeruser.adapter

import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import net.bedev.registeruser.R
import net.bedev.registeruser.database.UserEntity


class RvUserAdapter(val listener: RowClickListener) :
    RecyclerView.Adapter<RvUserAdapter.MyViewHolder>() {

    var items = ArrayList<UserEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return MyViewHolder(inflater, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(items[position])
        }
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setListData(data: ArrayList<UserEntity>) {
        this.items = data
    }

    interface RowClickListener {
        fun onDeleteUserClickListener(user: UserEntity)
        fun onItemClickListener(user: UserEntity)
    }

    class MyViewHolder(view: View, val listener: RowClickListener) : RecyclerView.ViewHolder(view) {


        val nip = view.TvNipUser
        val nama = view.TvNameUser
        val noTelp = view.TvNoTelpUser
        val noKtp = view.TvNoKtpUser
        val noSim = view.TvNoSimUser
        val alamat = view.TvALamatUser
        val IvFotoProfil = view.IvFotoProfilUser
        val IvKtp = view.IvFotoKtp
        val IvSim = view.IvFotoSim
        val IvTtd = view.IvFotoTtd
        val BtnHapusUser = view.BtnHapusUser
        val id = view.iduser
        val context = view.context



        fun bind(data: UserEntity) {

            nip.text = "No Nip = \n"+data.nip.toString()
            nama.text = "Nama = \n"+data.nama
            noTelp.text = "Nomor Telp = \n"+data.telp.toString()
            noKtp.text = "Nomor Ktp = \n"+data.noktp.toString()
            noSim.text = "Nomor Sim = \n"+data.nosim.toString()
            alamat.text = "Langitude =\n ${data.lang}  \n latitude = \n ${data.lat}"
            id.text = data.id.toString()


            val folderTtd =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/SignaturePad/" + data.fotottd)
            val folderSim =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Foto_sim/" + data.fotosim)
            val folderKtp =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Foto_ktp/" + data.fotoktp)
            val folderProfil =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Foto_profil/" + data.fotoprofil)

            val imageUriTtd = Uri.fromFile(folderTtd)
            val imageUriSim = Uri.fromFile(folderSim)
            val imageUriKtp = Uri.fromFile(folderKtp)
            val imageUriProfil = Uri.fromFile(folderProfil)

            Glide.with(context)
                .load(imageUriProfil)
                .into(IvFotoProfil)

            Glide.with(context)
                .load(imageUriKtp)
                .into(IvKtp)

            Glide.with(context)
                .load(imageUriSim)
                .into(IvSim)

            Glide.with(context)
                .load(imageUriTtd)
                .into(IvTtd)

            BtnHapusUser.setOnClickListener {
                listener.onDeleteUserClickListener(data)
            }

        }
    }

}
