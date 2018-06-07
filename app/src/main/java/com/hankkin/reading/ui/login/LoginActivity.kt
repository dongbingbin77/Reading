package com.hankkin.reading.ui.login

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.hankkin.reading.R
import com.hankkin.reading.adapter.MainFragmentAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.ui.login.register.RegFragment

class LoginActivity : BaseActivity<LoginContract.IPresenter>() ,LoginContract.IView{
    override fun loginResult(userBean: UserBean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getCapcha(captchaBean: CaptchaBean) {
    }


    private val fragments = listOf<Fragment>(
            LoginFragment(),
            RegFragment()
    )
    val adapter = MainFragmentAdapter(supportFragmentManager,fragments)

    override fun createPresenter(): LoginContract.IPresenter {
        return LoginPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
    }

    override fun initViews(savedInstanceState: Bundle?) {
        val vp = findViewById<ViewPager>(R.id.vp_login)
        val tablayout = findViewById<TabLayout>(R.id.tablayout)
        vp.adapter = adapter
        tablayout.setupWithViewPager(vp)
    }

}
