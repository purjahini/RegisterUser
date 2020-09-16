package net.bedev.registeruser.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userinfo")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "fotoprofil") val fotoprofil: String,
    @ColumnInfo(name = "nama") val nama: String,
    @ColumnInfo(name = "nip") val nip: Int,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lang") val lang: String,
    @ColumnInfo(name = "telp") val telp: Int,
    @ColumnInfo(name = "noktp") val noktp: Int,
    @ColumnInfo(name = "fotoktp") val fotoktp: String,
    @ColumnInfo(name = "nosim") val nosim: Int,
    @ColumnInfo(name = "fotosim") val fotosim: String,
    @ColumnInfo(name = "fotottd") val fotottd: String?
)