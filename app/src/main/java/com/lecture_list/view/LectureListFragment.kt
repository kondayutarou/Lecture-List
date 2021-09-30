package com.lecture_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecture_list.databinding.FragmentLectureListBinding
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

    override fun onStart() {
        super.onStart()
        viewModel.start()
    }

    private fun initViews() {
        binding.recycler.apply {
            adapter = LectureListRecyclerAdapter(parentActivity, viewModel)
            layoutManager = LinearLayoutManager(parentActivity)
        }
    }

    private fun initRx() {
        viewModel.shouldUpdateView.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.recycler.adapter?.notifyDataSetChanged()
            }
            .addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.finish()
        compositeDisposable.clear()
    }
}