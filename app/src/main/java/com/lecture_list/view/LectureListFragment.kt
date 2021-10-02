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
import com.lecture_list.extension.getDialog
import com.lecture_list.model.LectureListItem
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
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
        recyclerAdapter = LectureListRecyclerAdapter(parentActivity, ArrayList<LectureListItem>())
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
            viewModel.loadApi()
        }
    }

    private fun initRx() {
        val sharedLectureList = viewModel.lectureListForView
            .filter { it != null }.share()

        sharedLectureList.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Logger.d(it)
                recyclerAdapter.clear()
                recyclerAdapter.addItems(it)
                recyclerAdapter.notifyDataSetChanged()
                binding.swipeContainer.isRefreshing = false
            }
            .addTo(compositeDisposable)

        sharedLectureList
            // Do not save empty list
            .filter { it.isNotEmpty() }
            .observeOn(Schedulers.io())
            .subscribe {
                viewModel.saveData()
            }
            .addTo(compositeDisposable)

        viewModel.errorRelay.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.loadData()
                parentActivity.getDialog(
                    parentActivity.getString(R.string.dialog_api_error), "",
                    errorDialogPositiveListener
                )
                    .show()
            }
            .addTo(compositeDisposable)
    }

    private val errorDialogPositiveListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            binding.swipeContainer.isRefreshing = false
            dialogInterface.dismiss()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.finish()
        compositeDisposable.clear()
    }
}