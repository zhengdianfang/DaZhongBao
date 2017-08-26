package com.zhengdianfang.dazhongbao.views.basic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import com.just.library.AgentWeb
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.components.Toolbar


/**
 * Created by dfgzheng on 26/08/2017.
 */
class WebActivity: BaseActivity() {

    companion object {
        fun startActivity(context: Context, link: String){
           context.startActivity(Intent(context, WebActivity::class.java).putExtra("link", link))
        }
    }

    private var mAgentWeb: AgentWeb? = null
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val link by lazy { intent.getStringExtra("link") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        toolBar.backListener = {
            onBackPressed()
        }
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.frameLayout), LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .createAgentWeb()//
                .ready()
                .go(link)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb?.destroyAndKill()
    }
}