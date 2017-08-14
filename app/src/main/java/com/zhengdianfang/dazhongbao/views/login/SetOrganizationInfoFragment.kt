package com.zhengdianfang.dazhongbao.views.login


import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.mock.mockToken
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.TakePhotoFragment


/**
 * A simple [Fragment] subclass.
 */
class SetOrganizationInfoFragment : TakePhotoFragment() , IUploadCard {

    private val organizationNameEditText by lazy { view?.findViewById<EditText>(R.id.organizationNameEditText)!! }
    private val organizationContactEditText by lazy { view?.findViewById<EditText>(R.id.organizationContactEditText)!! }
    private val licenceCardImageView by lazy { view?.findViewById<ImageView>(R.id.licenceCardImageView)!! }
    private val mUserPresenter by lazy { UserPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_set_organization_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserPresenter.attachView(this)
        licenceCardImageView.setOnClickListener {
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

    override fun toolbarBackButtonClick() {
       getParentActivity().finish()
    }

    override fun toolbarConfirmButtonClick() {
        mUserPresenter.uploadBusinessLicenceCard(
                mockToken,
                organizationContactEditText.text.toString(),
                organizationNameEditText.text.toString(),
                takePhotoImagePath)
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun uploadSuccess(user: User) {
        CApplication.INSTANCE.loginUser = user
        replaceFragment(android.R.id.content, UploadBusinessCardFragment(), "login")
        toast(R.string.upload_business_card_success)
    }

    override fun getPhotoWidth(): Int {
        return licenceCardImageView.width
    }

    override fun getPhotoHeight(): Int {
        return licenceCardImageView.height
    }

    override fun takePhotoCallback(bitmap: Bitmap) {
        Glide.with(this).asBitmap().load(FileUtils.bitmapToByte(bitmap)).into(licenceCardImageView)
    }

    override fun pickPhotoCallback(imagePath: String) {
        Glide.with(this).asBitmap().load(imagePath).into(licenceCardImageView)
    }
}
