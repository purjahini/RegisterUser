package net.bedev.registeruser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.item_user.*
import net.bedev.registeruser.adapter.RvUserAdapter
import net.bedev.registeruser.database.UserEntity
import net.bedev.registeruser.helper.See

class UserActivity : AppCompatActivity(), RvUserAdapter.RowClickListener {
    lateinit var recyclerViewAdapter: RvUserAdapter
    lateinit var viewModel: UserActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        RvUser.apply {
            layoutManager= LinearLayoutManager(this@UserActivity)
            recyclerViewAdapter = RvUserAdapter(this@UserActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }
        viewModel = ViewModelProviders.of(this).get(UserActivityViewModel::class.java)
        viewModel.getAllUsersObservers().observe(this, Observer {
            See.log("respon user : $it")
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

    }

    override fun onDeleteUserClickListener(user: UserEntity) {
       viewModel.deleteUserInfo(user)
    }

    override fun onItemClickListener(user: UserEntity) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(getString(R.string.fotoprofil),  user.fotoprofil )
        intent.putExtra(getString(R.string.nama), user.nama)
        intent.putExtra(getString(R.string.nip), "${user.nip}")
        intent.putExtra(getString(R.string.lat), "${user.lat}")
        intent.putExtra(getString(R.string.lang), "${user.lang}")
        intent.putExtra(getString(R.string.telp),"${user.telp}")
        intent.putExtra(getString(R.string.noktp),"${user.noktp}")
        intent.putExtra(getString(R.string.fotoktp),user.fotoktp)
        intent.putExtra(getString(R.string.nosim),"${user.nosim}")
        intent.putExtra(getString(R.string.fotosim),user.fotosim)
        intent.putExtra(getString(R.string.fotottd),user.fotottd)
        intent.putExtra(getString(R.string.id), "${user.id}")
        intent.putExtra(getString(R.string.update), R.string.update)
        startActivity(intent)
        See.log("id :${user.id}, foto ${user.fotoprofil}")



    }
}