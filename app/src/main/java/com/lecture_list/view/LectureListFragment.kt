package com.lecture_list.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxrelay3.PublishRelay
import com.lecture_list.R
import com.lecture_list.databinding.FragmentLectureListBinding
import com.lecture_list.extension.isInternetAvailable
import com.lecture_list.model.LectureListItem
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class LectureListFragment : Fragment() {
    private lateinit var binding: FragmentLectureListBinding
    private lateinit var parentActivity: MainActivity
    val compositeDisposable = CompositeDisposable()
    private lateinit var recyclerAdapter: LectureListRecyclerAdapter

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLectureListBinding.inflate(inflater, container, false)
        initViews()
        initRx()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            val isOnline = parentActivity.isInternetAvailable()
            viewModel.start(isOnline)
        }
    }

    private fun initViews() {
        recyclerAdapter =
            LectureListRecyclerAdapter(parentActivity, ArrayList<LectureListItem>(), viewModel)
        binding.recycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(parentActivity)

            val itemDecoration =
                DividerItemDecoration(parentActivity, DividerItemDecoration.VERTICAL)
            val drawable =
                AppCompatResources.getDrawable(parentActivity, R.drawable.layout_vertical_spacing)
            drawable?.let {
                itemDecoration.setDrawable(it)
                addItemDecoration(itemDecoration)
            }
        }

        binding.swipeContainer.setOnRefreshListener {
            viewModel.getList()
            viewModel.loading.accept(true)
        }
    }

    private fun initRx() {
        viewModel.loading.observeOn(AndroidSchedulers.mainThread()).subscribe {
            binding.swipeContainer.isRefreshing = it
        }.addTo(compositeDisposable)

        viewModel.lectureList.observeOn(AndroidSchedulers.mainThread()).subscribe {
            recyclerAdapter.clear()
            recyclerAdapter.addItems(it)
        }.addTo(compositeDisposable)
    }

    private val errorDialogPositiveListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}