package com.hankkin.reading.mvp.presenter

import com.hankkin.library.utils.LogUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.EApplication
import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.mvp.contract.IBaseViewContract
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Created by huanghaijie on 2018/5/16.
 */
abstract class RxLifePresenter<out V : IBaseViewContract> : IBasePresenter<V>, IPresenterContract {

    private lateinit var mMVPView: V

    @Suppress("UNCHECKED_CAST")
    override fun registerMvpView(mvpViewContract: IBaseViewContract) {
        mMVPView = mvpViewContract as V
    }
    override fun getMvpView() = mMVPView

    enum class RxLife {
        ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY
    }

    private val mRxLifeMap = HashMap<RxLife, ArrayList<Disposable>>()


    override fun onCreate() {
        destroyRxLife(RxLife.ON_CREATE)
    }

    override fun onStart() {
        destroyRxLife(RxLife.ON_START)
    }

    override fun onResume() {
        destroyRxLife(RxLife.ON_RESUME)
    }

    override fun onPause() {
        destroyRxLife(RxLife.ON_PAUSE)
    }

    override fun onStop() {
        destroyRxLife(RxLife.ON_STOP)
    }

    override fun onDestroy() {
        destroyRxLife(RxLife.ON_DESTROY)
    }

    private fun destroyRxLife(rxLife: RxLife) {
        mRxLifeMap[rxLife]?.map {
            RxUtils.dispose(it)
        }
        mRxLifeMap[rxLife]?.clear()
    }

    /**
     * 扩展方法：用于管理RxJava生命周期
     * */
    fun Disposable.bindRxLifeEx(lifeLv: RxLife): Disposable {
        if (mRxLifeMap[lifeLv] != null) {
            mRxLifeMap[lifeLv]!!.add(this)
        } else {
            val rxList = ArrayList<Disposable>()
            rxList.add(this)
            mRxLifeMap[lifeLv] = rxList
        }
        return this
    }

    /**
     * 扩展方法：用于处理订阅事件发生时的公共代码
     * */
    fun <T> Observable<BaseResponse<T>>.subscribeEx(onNext: (data: T) -> Unit = {}, onError: (e: Throwable) -> Unit = {}, onComplete: () -> Unit = {}): Disposable {
        return this.subscribe({
            //编写订阅触发时的公共代码

            if(it.errorCode != 0){
                ToastUtils.showError(EApplication.instance(),it.errorMsg)
                onError.invoke(kotlin.Throwable())
            }
            else{
                onNext.invoke(it.data)
            }

        }, {
            //编写订阅失败的公共代码
            LogUtils.e(it)
            onError.invoke(it)
        }, {
            //编写订阅完成后的公共代码
            onComplete.invoke()
        })
    }

    fun <T> Observable<T>.subscribeNx(onNext: (data: T) -> Unit = {},onError: (e: Throwable) -> Unit = {},onComplete: () -> Unit = {}): Disposable{
        return this.subscribe({
            //编写订阅触发时的公共代码
            onNext.invoke(it)
        }, {
            //编写订阅失败的公共代码
            LogUtils.e(it)
            onError.invoke(it)
        }, {
            //编写订阅完成后的公共代码
            onComplete.invoke()
        })
    }

    fun addParams(key: String, value: Any): Map<String, Any> {
        val map = HashMap<String, Any>()
        map.put(key,value)
        return map
    }

}