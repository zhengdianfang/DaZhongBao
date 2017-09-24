package com.zhengdianfang.dazhongbao.views.basic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.just.library.AgentWeb
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.WechatUtils
import com.zhengdianfang.dazhongbao.views.components.Toolbar


/**
 * Created by dfgzheng on 26/08/2017.
 */
class WebActivity: BaseActivity() {


    companion object {
        fun startActivity(context: Context, title: String, link: String){
           context.startActivity(Intent(context, WebActivity::class.java)
                   .putExtra("link", link)
                   .putExtra("title", title))
        }
    }

    private var mAgentWeb: AgentWeb? = null
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val link by lazy { intent.getStringExtra("link") }
    private val title by lazy { intent.getStringExtra("title") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        toolBar.backListener = {
            onBackPressed()
        }
        toolBar.setTitle(title)
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.frameLayout), LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .setIndicatorColor(ContextCompat.getColor(this.applicationContext, R.color.colorPrimary))
                .createAgentWeb()//
                .ready()
                .go(link)
        mAgentWeb?.jsInterfaceHolder?.addJavaObject("dazongbao", JavaScript(this))
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb?.destroyAndKill()
    }

    private inner class JavaScript(private val context: Context){
        private val shareDialog by lazy {
            MaterialDialog.Builder(context)
                    .items(R.array.share_list)
                    .itemsCallback { dialog, itemView, position, text ->
                        if (position == 0){
                            WechatUtils(dialog.context).shareCirlce(shareLogo, shareTitle, shareDescription, shareUrl )
                        }else{
                            WechatUtils(dialog.context).shareFriend(shareLogo, shareTitle, shareDescription, shareUrl)
                        }
                    }
                    .build()
        }

        private var shareTitle = ""
        private var shareDescription = ""
        private var shareUrl  = ""
        private var shareLogo = ""

        @JavascriptInterface
        fun share(title: String, description: String, url: String, logo: String){
            this.shareDescription = description
            this.shareTitle = title
            this.shareUrl = url
            this.shareLogo = logo
            if (TextUtils.isEmpty(shareTitle).not() && TextUtils.isEmpty(shareUrl).not()){
                shareDialog.show()
            }
        }

    }
}