package com.zhengdianfang.dazhongbao.views.home


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zhengdianfang.dazhongbao.CApplication

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.login.UserCount
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.basic.WebActivity
import com.zhengdianfang.dazhongbao.views.setting.SettingActivity
import com.zhengdianfang.dazhongbao.views.user.MyAttentionActivity
import com.zhengdianfang.dazhongbao.views.user.MyAuctionListActivity
import com.zhengdianfang.dazhongbao.views.user.MyProductListActivity
import com.zhengdianfang.dazhongbao.views.user.PersonalInfoActivity
import jp.wasabeef.glide.transformations.CropCircleTransformation


/**
 * A simple [Fragment] subclass.
 */
class PersonalFragment : BaseFragment(), UserPresenter.IUserInfo{

    private val avatarImageView by lazy { view?.findViewById<ImageView>(R.id.userHeaderView)!! }
    private val userRealNameTextView by lazy { view?.findViewById<TextView>(R.id.userNameTextview)!! }
    private val userPhoneNumberTextView by lazy { view?.findViewById<TextView>(R.id.phoneNumberTextView)!! }
    private val levelTextView by lazy { view?.findViewById<TextView>(R.id.levelTextView)!! }
    private val upgradeVIPView by lazy { view?.findViewById<TextView>(R.id.upgradeVIPView)!! }
    private val myStartProductCountTextView by lazy { view?.findViewById<TextView>(R.id.myStartProductCountTextView)!! }
    private val myProductCountTextView by lazy { view?.findViewById<TextView>(R.id.myProductCountTextView)!! }
    private val myAucationProductCountTextView by lazy { view?.findViewById<TextView>(R.id.myAucationProductCountTextView)!! }
    private val bondCountView by lazy { view?.findViewById<TextView>(R.id.bondCountView)!! }
    private val settingViewGroup by lazy { view?.findViewById<ViewGroup>(R.id.settingViewGroup)!! }
    private val partnerViewGroup by lazy { view?.findViewById<ViewGroup>(R.id.partnerViewGroup)!! }
    private val userPersenter = UserPresenter()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userPersenter.attachView(this)
        settingViewGroup.setOnClickListener {
            startActivity(Intent(getParentActivity(), SettingActivity::class.java))
        }
        partnerViewGroup.setOnClickListener {
           WebActivity.startActivity(context, String.format(Constants.PARTNER_URL, CApplication.INSTANCE.loginUser?.token))
        }
        userPersenter.fetchUserInfo(CApplication.INSTANCE.loginUser?.token!!)
    }

    private fun setupUserInfo(loginUser: User, userCount: UserCount) {
        if(null != loginUser){
            Glide.with(this).load(loginUser.avatar).
                    apply(RequestOptions.bitmapTransform(CropCircleTransformation(this.context)).placeholder(R.mipmap.fragment_personal_default_header_image).error(R.mipmap.fragment_personal_default_header_image))
                    .into(avatarImageView)
            userRealNameTextView.text = if(TextUtils.isEmpty(loginUser.realname)) getString(R.string.fragment_personal_anonymous_user) else loginUser.realname
            userPhoneNumberTextView.text = loginUser.phonenumber
            levelTextView.text = if (loginUser.integrity == 0) getString(R.string.fragment_personal_certified_member) else getString(R.string.fragment_personal_normal_member)
            upgradeVIPView.visibility = if (loginUser.integrity!! == 0) View.GONE else View.VISIBLE
        }

        myStartProductCountTextView.text = userCount.myAttention.toString()
        myProductCountTextView.text = userCount.myDazhongbao.toString()
        myAucationProductCountTextView.text = userCount.myAuction.toString()
        bondCountView.text = userCount.myDeposit.toString()

        view?.findViewById<TextView>(R.id.textView9)!!.setOnClickListener {
           startActivity(Intent(getParentActivity(), MyProductListActivity::class.java))
        }
        view?.findViewById<TextView>(R.id.textView10)!!.setOnClickListener {
            startActivity(Intent(getParentActivity(), MyAttentionActivity::class.java))
        }
        view?.findViewById<TextView>(R.id.textView11)!!.setOnClickListener {
            startActivity(Intent(getParentActivity(), MyAuctionListActivity::class.java))
        }

        avatarImageView.setOnClickListener {
            context.startActivity(Intent(context, PersonalInfoActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userPersenter.detachView()
    }

    override fun receiverUserInfo(user: User, userCount: UserCount) {
        setupUserInfo(user, userCount)
    }
}// Required empty public constructor
