package com.example.formlayout

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
create by hmSong on 2021/6/23
 */
class ScrollHelper {
    private val observerList = HashSet<RecyclerView>()
    private var firstPos = -1
    private var firstOffset = -1

    @SuppressLint("ClickableViewAccessibility")
    fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (firstPos > 0 && firstOffset > 0) {
            layoutManager.scrollToPositionWithOffset(
                this@ScrollHelper.firstPos + 1,
                this@ScrollHelper.firstOffset
            )
        }
        observerList.add(recyclerView)
        recyclerView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> for (rv in observerList) {
                    rv.stopScroll()
                }
            }
            false
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstPos: Int = linearLayoutManager.findFirstVisibleItemPosition()
                val firstVisibleItem: View? = linearLayoutManager.getChildAt(0)
                if (firstVisibleItem != null) {
                    val firstRight: Int = linearLayoutManager.getDecoratedRight(firstVisibleItem)
                    for (rv in observerList) {
                        if (recyclerView !== rv) {
                            val manager = rv.layoutManager as LinearLayoutManager
                            this@ScrollHelper.firstPos = firstPos
                            this@ScrollHelper.firstOffset = firstRight
                            manager.scrollToPositionWithOffset(firstPos + 1, firstRight)
                        }
                    }
                }
            }
        })
    }
}