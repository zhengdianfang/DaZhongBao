package com.zhengdianfang.dazhongbao.viewsimport android.content.Intentimport android.os.Bundleimport android.view.Viewimport com.zhengdianfang.dazhongbao.CApplicationimport com.zhengdianfang.dazhongbao.Rimport com.zhengdianfang.dazhongbao.helpers.IMUtilsimport com.zhengdianfang.dazhongbao.views.basic.BaseActivityimport com.zhengdianfang.dazhongbao.views.home.MainActivityimport com.zhengdianfang.dazhongbao.views.login.LoginActivity/** * Created by dfgzheng on 25/07/2017. */class StartupActivity: BaseActivity() {    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_startup)        findViewById<View>(R.id.logoImage).systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION        if (CApplication.INSTANCE.isLogin()){            IMUtils.login(CApplication.INSTANCE.loginUser!!).subscribe {  }            startActivity(Intent(this, MainActivity::class.java))        }else {            startActivity(Intent(this, LoginActivity::class.java))        }        finish()    }    override fun onWindowFocusChanged(hasFocus: Boolean) {        super.onWindowFocusChanged(hasFocus)        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar                or View.SYSTEM_UI_FLAG_IMMERSIVE)    }}