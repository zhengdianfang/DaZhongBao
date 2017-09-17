package com.zhengdianfang.dazhongbao.views.setting


import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PixelUtils
import com.zhengdianfang.dazhongbao.models.basic.Message
import com.zhengdianfang.dazhongbao.presenters.NotifyMessagePresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListFragment


/**
 * A simple [Fragment] subclass.
 */
class MessageListByTypeFragment : BaseListFragment<Message>(), NotifyMessagePresenter.IMessageList {

    private val notifyMessagePresenter = NotifyMessagePresenter()
    private val iconType by lazy { arguments.getInt("iconType") }
    private val title by lazy { arguments.getString("title") }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_message_list_by_type, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolBar?.setTitle(title)
        notifyMessagePresenter.attachView(this)
        toolBar?.backListener = {
            onBackPressed()
        }
        setupRecyclerView()
        recyclerView.refresh()
    }

    override fun requestList(pageNumber: Int) {
        notifyMessagePresenter.fetchMessagerListByType(CApplication.INSTANCE.loginUser?.token!!, iconType)
    }

    override fun createRecyclerView(): XRecyclerView {
        return view?.findViewById(R.id.messageRecyclerView)!!
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return MessageItemAdapter(datas)
    }
    override fun receiverList(messages: MutableList<Message>) {
        reponseProcessor(messages)
    }
    private fun setupRecyclerView() {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                if (parent?.getChildAdapterPosition(view) != 0)
                    if (outRect != null) {
                        outRect!!.bottom += PixelUtils.dp2px(view?.context!!, 8f).toInt()
                    }

            }
        })
    }
}
