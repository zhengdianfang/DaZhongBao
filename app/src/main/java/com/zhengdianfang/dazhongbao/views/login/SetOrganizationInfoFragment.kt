package com.zhengdianfang.dazhongbao.views.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DeviceUtils
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.mock.mockToken
import com.zhengdianfang.dazhongbao.presenters.PresenterFactory
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.components.Toolbar


/**
 * A simple [Fragment] subclass.
 */
class SetOrganizationInfoFragment : BaseFragment<LoginActivity>() {

    private val toolbar by lazy { view?.findViewById<Toolbar>(R.id.toolbar)!! }
    private val organizationNameEditText by lazy { view?.findViewById<EditText>(R.id.organizationNameEditText)!! }
    private val organizationContactEditText by lazy { view?.findViewById<EditText>(R.id.organizationContactEditText)!! }
    private val licenceCardImageView by lazy { view?.findViewById<ImageView>(R.id.licenceCardImageView)!! }
    private val submitButton by lazy { view?.findViewById<Button>(R.id.submitButton)!! }
    private var takePhotoImagePath = ""
    private val mediaDialog by lazy {
        MaterialDialog.Builder(this.getParentActivity()).theme(Theme.LIGHT)
                .items(R.array.media_list)
                .itemsCallback({ dialog, itemView, position, text ->
                    if(position == 0){
                        takePhotoImagePath = DeviceUtils.takePhoto(this)
                    }else {
                        DeviceUtils.pickPhoto(this)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_set_organization_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PresenterFactory.mLoginPresenter.attachView(this)
        toolbar.backListener = {
            getParentActivity().supportFragmentManager.popBackStack()
        }

        licenceCardImageView.setOnClickListener {
            mediaDialog.show()
        }

        submitButton.setOnClickListener {
            PresenterFactory.mLoginPresenter.uploadBusinessLicenceCard(
                    mockToken,
                    organizationContactEditText.text.toString(),
                    organizationNameEditText.text.toString(),
                    takePhotoImagePath
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PresenterFactory.mLoginPresenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
           if (requestCode == DeviceUtils.TAKE_PHOTO){
               val bitmap = FileUtils.decodeBitmapFromFile(takePhotoImagePath, licenceCardImageView.width, licenceCardImageView.height)
               if (null != bitmap) {
                   Glide.with(this).asBitmap().load(FileUtils.bitmapToByte(bitmap)).into(licenceCardImageView)
               }
           }else if (requestCode == DeviceUtils.PICK_PHOTO){
               if (data?.data != null){
                   takePhotoImagePath = FileUtils.getAbsolutePathByUri(this.context.applicationContext, data.data!!) ?: ""
                   val bitmap = FileUtils.decodeBitmapFromFile(takePhotoImagePath, licenceCardImageView.width, licenceCardImageView.height)
                   if (null != bitmap) {
                       Glide.with(this).asBitmap().load(FileUtils.bitmapToByte(bitmap)).into(licenceCardImageView)
                   }
               }
           }
        }
    }

}
