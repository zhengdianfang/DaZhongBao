package com.zhengdianfang.dazhongbao.views.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.login.IUploadCard
import jp.wasabeef.glide.transformations.CropCircleTransformation

class PersonalInfoActivity : BaseActivity(), IUploadCard {

    private val headerImageView by lazy { findViewById<ImageView>(R.id.userHeaderView) }
    private val userPresenter = UserPresenter()
    private var takePhotoImagePath = ""
    private  val mediaDialog by lazy {
        MaterialDialog.Builder(this).theme(Theme.LIGHT)
                .items(R.array.media_list)
                .itemsCallback({ _, _, position, _ ->
                    if(position == 0){
                        takePhotoImagePath = DeviceUtils.takePhoto(this)
                    }else {
                        DeviceUtils.pickPhoto(this)
                    }
                }).build()
    }

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
        userPresenter.attachView(this)
        toolBar.backListener = {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
    }

    override fun onResume() {
        super.onResume()
        val loginUser = CApplication.INSTANCE.loginUser
        if (null != loginUser){
            findViewById<TextView>(R.id.userNameTextView)!!.text = loginUser.realname
            if (!TextUtils.isEmpty(loginUser.avatar)){
                Glide.with(this).load(loginUser.avatar).into(headerImageView)
            }
            findViewById<TextView>(R.id.companyNameTextView)!!.text = loginUser.companyName
            findViewById<TextView>(R.id.companyPositonTextView)!!.text = loginUser.position
            findViewById<TextView>(R.id.companyLocationTextView)!!.text = loginUser.companyAddress
            findViewById<TextView>(R.id.emailTextView)!!.text = loginUser.email
            findViewById<View>(R.id.headerViewGroup).setOnClickListener {
               mediaDialog.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == DeviceUtils.TAKE_PHOTO){
                val size = resources.getDimension(R.dimen.personal_header_size).toInt()
                val bitmap = FileUtils.decodeBitmapFromFile(takePhotoImagePath, size, size)
                if (null != bitmap) {
                    Glide.with(this).load(FileUtils.bitmapToByte(bitmap))
                            .bitmapTransform(CropCircleTransformation(this.applicationContext))
                            .placeholder(R.mipmap.fragment_personal_default_header_image).error(R.mipmap.fragment_personal_default_header_image)
                            .into(headerImageView)
                    headerImageView.postDelayed({
                        userPresenter.uploadUserAvatar(CApplication.INSTANCE.loginUser?.token!!, takePhotoImagePath)
                    }, 1000)
                }
            }else if (requestCode == DeviceUtils.PICK_PHOTO){
                if (data?.data != null){
                    takePhotoImagePath = FileUtils.getPathFromUri(this.applicationContext, data.data!!) ?: ""
                    Glide.with(this).load(takePhotoImagePath)
                            .bitmapTransform(CropCircleTransformation(this.applicationContext))
                            .placeholder(R.mipmap.fragment_personal_default_header_image).error(R.mipmap.fragment_personal_default_header_image)
                            .into(headerImageView)
                    headerImageView.postDelayed({
                        userPresenter.uploadUserAvatar(CApplication.INSTANCE.loginUser?.token!!, takePhotoImagePath)
                    }, 1000)
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun uploadSuccess(user: User) {
        CApplication.INSTANCE.loginUser = user
        toast(R.string.upload_business_card_success)
    }
}
