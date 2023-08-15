package com.malkinfo.janataaapp.views.base

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.viewmodels.MyBaseViewModel

abstract class ViewModelCompatActivity : MyAppCompatActivity() {

    private var mBaseView: ViewGroup? = null
    private var mLoaderView: View? = null
    private var progressShown = false
    private var button: CircularProgressButton? = null
    private var viewModel: MyBaseViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgress()
    }

    //Call this method while setting up Viewmodel to init progress
    fun setUpLoader(viewModel: MyBaseViewModel, button: CircularProgressButton? = null) {
        this.viewModel = viewModel
        this.button = button

        val color = ContextCompat.getColor(this, R.color.secondary)
        val doneBitmap = BitmapFactory.decodeResource(resources, R.drawable.checkmark_white)

        viewModel.isLoading.observe(this, Observer {
            if (button == null) {
                if (it == LoaderStatus.loading)
                    showProgress()
                else
                    hideProgress()
            } else {
                if (it == LoaderStatus.loading)
                    button.startAnimation()
                else if (it == LoaderStatus.success)
                    button.doneLoadingAnimation(color, doneBitmap)
                else if (it == LoaderStatus.failed)
                    button.revertAnimation()
            }

        })

        viewModel.errorMediatorLiveData.observe(this, Observer {
            it?.let {
                showSnackbar(it)
                onError(it)
            }
        })

        initObservers()
    }

    //    abstract fun initObservers()
    abstract fun onError(message: String)

    //Initializing the progress view
    private fun initProgress() {
        mBaseView = this.findViewById(android.R.id.content)
        mLoaderView = View.inflate(this, R.layout.loader, null)
    }

    override fun showProgress() {
        hideKeyboard()
        if (!progressShown) {
            mBaseView!!.addView(mLoaderView)
            progressShown = true
        }
    }

    //To hide loader progress
    override fun hideProgress() {
        if (progressShown) {
            mBaseView!!.removeView(mLoaderView)
            progressShown = false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        button?.let {
            button!!.dispose()
        }
    }
}