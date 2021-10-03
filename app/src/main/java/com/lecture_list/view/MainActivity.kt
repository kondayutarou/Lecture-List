package com.lecture_list.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lecture_list.R
import com.lecture_list.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        val LECTURE_LIST_FRAGMENT = "lectureListFragment"
    }

    private lateinit var lectureListFragment: Fragment

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        // Initialize fragment at first activity creation
        lectureListFragment = if (savedInstanceState == null) {
            LectureListFragment()
        } else {
            supportFragmentManager.findFragmentByTag(LECTURE_LIST_FRAGMENT) ?: LectureListFragment()
        }

        changeFragment(lectureListFragment, binding, LECTURE_LIST_FRAGMENT)
    }

    /**
     * @param fragment target fragment to transition to
     * @param tag tag to distinguish target fragment on view (re)creation
     */
    private fun changeFragment(fragment: Fragment, binding: ActivityMainBinding, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(binding.container.id, fragment, tag)
        transaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}