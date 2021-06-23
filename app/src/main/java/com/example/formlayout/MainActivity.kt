package com.example.formlayout

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val rowNum  = 20
    private val colNum = 30
    private lateinit var helper : ScrollHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helper = ScrollHelper()
        form_title.text = "标题"
        rv_top.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv_top.adapter = RowAdapter(getRowData("德玛"))

        rv_content.layoutManager = LinearLayoutManager(this)
        rv_content.adapter = ContentAdapter(getContentData())

        helper.initRecyclerView(rv_top)
    }

    //头部适配器
    inner class RowContentAdapter(data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_header, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.item_title, item)
        }
    }


    inner class RowAdapter(data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_header, data) {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.item_title, item)
            holder.setTextColor(R.id.item_title,context.getColor(R.color.teal_700))
        }
    }

    //form适配器
    inner class ContentAdapter(data: MutableList<ContentBean>) : BaseQuickAdapter<ContentBean, BaseViewHolder>(R.layout.item_content,data) {
        override fun convert(holder: BaseViewHolder, item: ContentBean) {
            val rv : RecyclerView = holder.getView(R.id.rv_item_content)
            val title : TextView = holder.getView(R.id.tv_item_title)
            title.text = item.title
            rv.adapter = RowContentAdapter(item.childData)
            rv.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            helper.initRecyclerView(rv)
        }
    }

    //横向数据
    private fun getRowData(content : String): MutableList<String> {
        val data = mutableListOf<String>()
        for (i in 1..rowNum) {
            data.add("$content$i")
        }
        return data
    }
    //form数据
    private fun getContentData(): MutableList<ContentBean> {
        val data = mutableListOf<ContentBean>()
        for (i in 1..colNum){
            data.add(ContentBean("亚索$i",getRowData("寒冰")))
        }
        return data
    }
    //bean类
    data class ContentBean(val title: String, val childData: MutableList<String>)



}