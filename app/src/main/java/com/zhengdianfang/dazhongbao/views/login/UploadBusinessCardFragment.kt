package com.zhengdianfang.dazhongbao.views.login


import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.TakePhotoFragment


/**
 * A simple [Fragment] subclass.
 */
class UploadBusinessCardFragment : TakePhotoFragment(), IUploadCard {

    val businessCardImageView by lazy { view?.findViewById<ImageView>(R.id.businessCardImageView)!! }
    private val mUserPresenter by lazy { UserPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_upload_business_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserPresenter.attachView(this)
        businessCardImageView.setOnClickListener {
           mediaDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUserPresenter.detachView()
    }

    override fun onBackPressed(): Boolean {
        toolbarBackButtonClick()
        return true
    }

    override fun toolbarConfirmButtonClick() {
        val token = CApplication.INSTANCE.loginUser?.token
        if (token != null){
            mUserPresenter.uploadBusinessCard(token, "", takePhotoImagePath)
        }
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun uploadSuccess(user: User) {
        CApplication.INSTANCE.loginUser = user
        toast(R.string.upload_business_card_success)
        replaceFragment(android.R.id.content, UploadContactCardFragment(), "login")
    }

    override fun getPhotoWidth(): Int {
        return businessCardImageView.width
    }

    override fun getPhotoHeight(): Int {
        return businessCardImageView.height
    }

    override fun takePhotoCallback(bitmap: Bitmap) {
        Glide.with(this).load(FileUtils.bitmapToByte(bitmap)).into(businessCardImageView)
    }

    override fun pickPhotoCallback(imagePath: String) {
        Glide.with(this).load(imagePath).into(businessCardImageView)
    }
}// Required empty public constructor
