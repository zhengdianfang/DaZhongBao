package com.zhengdianfang.dazhongbao.views.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.AppUtils
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
open class UploadContactCardFragment : BaseFragment() , IUploadCard, BasePresenter.ICheckUserIntegrityView{

    private val mUserPresenter by lazy { UserPresenter() }
    private val idCardFrontEndImageView by lazy { view?.findViewById<ImageView>(R.id.idCardFrontEndImageView)!! }
    private val idCardBackEndImageView by lazy { view?.findViewById<ImageView>(R.id.idCardBackEndImageView)!! }
    private val frontTextView1 by lazy { view?.findViewById<TextView>(R.id.frontTextView1)!! }
    private val frontTextView2 by lazy { view?.findViewById<TextView>(R.id.frontTextView2)!! }
    private val backendTextView1 by lazy { view?.findViewById<TextView>(R.id.backendTextView1)!! }
    private val backendTextView2 by lazy { view?.findViewById<TextView>(R.id.backendTextView2)!! }
    private var type = 0
    private var idCardFrontEndImagePath = ""
    private var idCardBackEndImagePath = ""
    private val mediaDialog1 by lazy {
        MaterialDialog.Builder(this.getParentActivity()).theme(Theme.LIGHT)
                .items(R.array.media_list) .itemsCallback({ dialog, itemView, position, text ->
                    dialog.dismiss()
                    if(position == 0){
                        when(type){
                            0 -> { idCardFrontEndImagePath = DeviceUtils.takePhoto(this) }
                            1 -> { idCardBackEndImagePath = DeviceUtils.takePhoto(this)}
                        }
                    }else {
                        DeviceUtils.pickPhoto(this)
                    }
                }).build()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_upload_contact_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserPresenter.attachView(this)
        idCardFrontEndImageView.setOnClickListener {
            type = 0
            mediaDialog1.show()
        }

        idCardBackEndImageView.setOnClickListener {
            type = 1
            mediaDialog1.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUserPresenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == DeviceUtils.TAKE_PHOTO){
                when(type){
                    0 -> {
                        val bitmap = FileUtils.decodeBitmapFromFile(idCardFrontEndImagePath, idCardFrontEndImageView.width, idCardFrontEndImageView.height)
                        if (null != bitmap){
                            Glide.with(this).load(FileUtils.bitmapToByte(bitmap)).into(idCardFrontEndImageView)
                            frontTextView1.visibility = View.INVISIBLE
                            frontTextView2.visibility = View.INVISIBLE
                        }
                    }
                    1-> {
                        val bitmap = FileUtils.decodeBitmapFromFile(idCardBackEndImagePath, idCardBackEndImageView.width, idCardBackEndImageView.height)
                        if (null != bitmap){
                            Glide.with(this).load(FileUtils.bitmapToByte(bitmap)).into(idCardBackEndImageView)
                            backendTextView1.visibility = View.INVISIBLE
                            backendTextView2.visibility = View.INVISIBLE
                        }
                    }
                }
            }else if (requestCode == DeviceUtils.PICK_PHOTO){
                if (data?.data != null){
                    when(type){
                        0 -> {
                            FileUtils.getPathFromUri(activity, data.data!!, {path ->
                                this.idCardFrontEndImagePath = path ?: ""
                                Glide.with(this).load(idCardFrontEndImagePath).into(idCardFrontEndImageView)
                                frontTextView1.visibility = View.INVISIBLE
                                frontTextView2.visibility = View.INVISIBLE
                            })
                        }
                        1 -> {
                            FileUtils.getPathFromUri(activity, data.data!!, {path ->
                                this.idCardBackEndImagePath  = path ?: ""
                                Glide.with(this).load(idCardBackEndImagePath).into(idCardBackEndImageView)
                            })
                            backendTextView1.visibility = View.INVISIBLE
                            backendTextView2.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun toolbarConfirmButtonClick() {
        val token = CApplication.INSTANCE.loginUser?.token
        if (token != null) {
            mUserPresenter.uploadContactCard(token, idCardFrontEndImagePath, idCardBackEndImagePath)
        }
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        toast(errorMsgResId)
    }

    override fun uploadSuccess(user: User) {
        CApplication.INSTANCE.loginUser = user
        toast(R.string.upload_card_contact_success)
        toolbarBackButtonClick()
    }

    override fun toolbarBackButtonClick() {
        getParentActivity().finish()
    }
    override fun notIntegrity(type: Int) {
        AppUtils.interityUserInfo(getParentActivity(), type)
    }

}// Required empty public constructor
