package com.lecture_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lecture_list.databinding.FragmentLectureListBinding

class LectureListFragment: Fragment() {
    private lateinit var binding: FragmentLectureListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLectureListBinding.inflate(inflater, container, false)
        return binding.root
    }
}