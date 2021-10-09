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
import com.lecture_list.R
import com.lecture_list.databinding.FragmentLectureListBinding
import com.lecture_list.model.LectureListItem
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.start()
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
        }
    }

    private val errorDialogPositiveListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            binding.swipeContainer.isRefreshing = false
            dialogInterface.dismiss()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}