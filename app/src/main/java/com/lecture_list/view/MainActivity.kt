package com.lecture_list.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lecture_list.R
import com.lecture_list.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        changeFragment(LectureListFragment(), binding)
    }

    /**
     * @param fragment target fragment to transition to
     */
    private fun changeFragment(fragment: Fragment, binding: ActivityMainBinding) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(binding.container.id, fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}