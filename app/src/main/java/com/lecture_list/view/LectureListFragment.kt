package com.lecture_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecture_list.databinding.FragmentLectureListBinding
import com.lecture_list.extension.getDialog
import com.orhanobut.logger.Logger
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
        viewModel.start()
        return binding.root
    }

    private fun initViews() {
        recyclerAdapter = LectureListRecyclerAdapter(parentActivity)
        binding.recycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(parentActivity)
        }

        binding.swipeContainer.setOnRefreshListener {
            viewModel.loadApi()
        }
    }

    private fun initRx() {
        viewModel.lectureListForView
            .filter { it != null }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Logger.d(it)
                recyclerAdapter.clear()
                recyclerAdapter.addItems(it)
                recyclerAdapter.notifyDataSetChanged()
                binding.swipeContainer.isRefreshing = false
            }
            .addTo(compositeDisposable)

        viewModel.errorRelay.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                parentActivity.getDialog("Api Error. Please refresh the app", "")
            }
            .addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.finish()
        compositeDisposable.clear()
    }
}