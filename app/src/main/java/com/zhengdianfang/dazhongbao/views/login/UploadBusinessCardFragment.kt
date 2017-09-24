package com.zhengdianfang.dazhongbao.views.login


import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.AppUtils
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.TakePhotoFragment


/**
 * A simple [Fragment] subclass.
 */
class UploadBusinessCardFragment : TakePhotoFragment(), IUploadCard, BasePresenter.ICheckUserIntegrityView {

    private val businessCardImageView by lazy { view?.findViewById<ImageView>(R.id.businessCardImageView)!! }
    private val maskTextView by lazy { view?.findViewById<TextView>(R.id.maskTextView)!! }
    private val uploadCardView by lazy { view?.findViewById<TextView>(R.id.uploadCardView)!! }
    private val scanTextView by lazy { view?.findViewById<TextView>(R.id.scanTextView)!! }
    private val mUserPresenter by lazy { UserPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_upload_business_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserPresenter.attachView(this)
        uploadCardView.setOnClickListener {
            pickPhoto()
        }
        scanTextView.setOnClickListener {
            takePhoto()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUserPresenter.detachView()
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
        if (mUserPresenter.checkUserInterity()){
            toolbarBackButtonClick()
        }
    }

    override fun toolbarBackButtonClick() {
        getParentActivity().finish()
    }

    override fun getPhotoWidth(): Int {
        return businessCardImageView.width
    }

    override fun getPhotoHeight(): Int {
        return businessCardImageView.height
    }

    override fun takePhotoCallback(bitmap: Bitmap) {
        Glide.with(this).load(FileUtils.bitmapToByte(bitmap)).into(businessCardImageView)
        maskTextView.visibility = View.INVISIBLE
    }

    override fun pickPhotoCallback(imagePath: String) {
        Glide.with(this).load(imagePath).into(businessCardImageView)
        maskTextView.visibility = View.INVISIBLE
    }
    override fun notIntegrity(type: Int) {
        AppUtils.interityUserInfo(getParentActivity(), type)
    }
}// Required empty public constructor
