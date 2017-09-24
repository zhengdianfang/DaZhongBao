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
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.zhengdianfang.dazhongbao.CApplication

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.login.UserCount
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.basic.WebActivity
import com.zhengdianfang.dazhongbao.views.login.UploadContactCardFragment
import com.zhengdianfang.dazhongbao.views.setting.SettingActivity
import com.zhengdianfang.dazhongbao.views.user.*
import jp.wasabeef.glide.transformations.CropCircleTransformation


/**
 * A simple [Fragment] subclass.
 */
class PersonalFragment : BaseFragment(), UserPresenter.IUserInfo{

    private val markImageView by lazy { view?.findViewById<ImageView>(R.id.markImageView)!! }
    private val avatarImageView by lazy { view?.findViewById<ImageView>(R.id.userHeaderView)!! }
    private val userRealNameTextView by lazy { view?.findViewById<TextView>(R.id.userNameTextview)!! }
    private val userPhoneNumberTextView by lazy { view?.findViewById<TextView>(R.id.phoneNumberTextView)!! }
    private val levelTextView by lazy { view?.findViewById<TextView>(R.id.levelTextView)!! }
    private val upgradeVIPView by lazy { view?.findViewById<TextView>(R.id.upgradeVIPView)!! }
    private val myAttentionProductCountTextView by lazy { view?.findViewById<TextView>(R.id.myAttentionProductCountTextView)!! }
    private val myProductCountTextView by lazy { view?.findViewById<TextView>(R.id.myProductCountTextView)!! }
    private val myAuctionProductCountTextView by lazy { view?.findViewById<TextView>(R.id.myAuctionProductCountTextView)!! }
    private val bondCountView by lazy { view?.findViewById<TextView>(R.id.bondCountView)!! }
    private val settingViewGroup by lazy { view?.findViewById<ViewGroup>(R.id.settingViewGroup)!! }
    private val partnerViewGroup by lazy { view?.findViewById<ViewGroup>(R.id.partnerViewGroup)!! }
    private val myDepositViewGroup by lazy { view?.findViewById<ViewGroup>(R.id.myDepositViewGroup)!! }
    private val refreshLayout by lazy { view?.findViewById<SmartRefreshLayout>(R.id.refreshLayout)!! }
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
           WebActivity.startActivity(context, getString(R.string.partner_plan_title), String.format(Constants.PARTNER_URL, CApplication.INSTANCE.loginUser?.token))
        }
        refreshLayout.setOnRefreshListener {
            userPersenter.fetchUserInfo(CApplication.INSTANCE.loginUser?.token!!)
        }
        refreshLayout.autoRefresh()

    }

    private fun setupUserInfo(loginUser: User, userCount: UserCount) {
        if(null != loginUser){
            Logger.d("userId : ${loginUser.id}, token : ${loginUser.token}, avatar : ${loginUser.avatar}")
            Glide.with(this).load(loginUser.avatar).
                    bitmapTransform(CropCircleTransformation(this.context)).placeholder(R.mipmap.fragment_personal_default_header_image).error(R.mipmap.fragment_personal_default_header_image)
                    .into(avatarImageView)

            when(loginUser.type){
                1-> markImageView.setImageResource(R.drawable.fragment_personal_people_icon)
                2-> markImageView.setImageResource(R.drawable.fragment_personal_company_icon)
            }
            userRealNameTextView.text = if(TextUtils.isEmpty(loginUser.realname)) getString(R.string.fragment_personal_anonymous_user) else loginUser.realname
            userPhoneNumberTextView.text = loginUser.phonenumber?.replaceRange(3, 7,"****")
            levelTextView.text = if (loginUser.integrity == 0) getString(R.string.fragment_personal_certified_member) else getString(R.string.fragment_personal_normal_member)
            upgradeVIPView.visibility = if (loginUser.integrity!! == 0) View.GONE else View.VISIBLE
            upgradeVIPView.setOnClickListener {
                val fragment = UploadContactCardFragment()
                startFragment(android.R.id.content,fragment , "personal")
            }
            userPhoneNumberTextView.setOnClickListener {
                val fragment = UploadContactCardFragment()
                startFragment(android.R.id.content,fragment , "personal")
            }
        }

        myAttentionProductCountTextView.text = userCount.myAttention.toString()
        myProductCountTextView.text = userCount.myDazhongbao.toString()
        myAuctionProductCountTextView.text = userCount.myAuction.toString()
        bondCountView.text = userCount.myDeposit.toString()

        view?.findViewById<View>(R.id.linearLayout7)!!.setOnClickListener {
           startActivity(Intent(getParentActivity(), MyProductListActivity::class.java))
        }
        view?.findViewById<View>(R.id.linearLayout5)!!.setOnClickListener {
            startActivity(Intent(getParentActivity(), MyAttentionActivity::class.java))
        }
        view?.findViewById<View>(R.id.linearLayout6)!!.setOnClickListener {
            startActivity(Intent(getParentActivity(), MyAuctionListActivity::class.java))
        }

        avatarImageView.setOnClickListener {
            context.startActivity(Intent(context, PersonalInfoActivity::class.java))
        }

        myDepositViewGroup.setOnClickListener {
            context.startActivity(Intent(context, MyDepositListActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userPersenter.detachView()
    }

    override fun receiverUserInfo(user: User, userCount: UserCount) {
        refreshLayout.finishRefresh()
        setupUserInfo(user, userCount)
    }

    override fun onBackPressed(): Boolean {
        getParentActivity().finish()
        return true
    }
}// Required empty public constructor
