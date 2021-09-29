package com.lecture_list.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lecture_list.R
import com.lecture_list.databinding.ActivityMainBinding
import com.lecture_list.databinding.DialogueAddBinding
import com.lecture_list.data.ShoppingListItem
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
    @Inject
    lateinit var viewModel: MainViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
        initView()
    }

    private fun initView() {
        binding.viewModel = viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.finish()
        compositeDisposable.clear()
    }
}