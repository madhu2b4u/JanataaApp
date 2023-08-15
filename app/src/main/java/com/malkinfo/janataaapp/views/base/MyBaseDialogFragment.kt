package com.malkinfo.janataaapp.views.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.viewmodels.MyBaseViewModel
import com.google.android.material.snackbar.Snackbar

abstract class MyBaseDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // request a window without the title
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }


    protected val TAG = this.javaClass.simpleName
    private var mBaseView: ViewGroup? = null
    private var mLoaderView: View? = null
    private var progressShown = false
    private var futureShowProgress = false
    private var button: CircularProgressButton? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null


    override fun onResume() {
        super.onResume()
        dialog?.let {
            if (it.window != null)
                it.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
    }

    protected fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    protected fun setUpLoader(
        viewModel: MyBaseViewModel,
        button: CircularProgressButton? = null,
        swipeRefreshLayout: SwipeRefreshLayout? = null
    ) {

        this.button = button
        this.swipeRefreshLayout = swipeRefreshLayout

        val color = ContextCompat.getColor(requireActivity(), R.color.secondary)
        val doneBitmap = BitmapFactory.decodeResource(resources, R.drawable.checkmark_white)

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (swipeRefreshLayout != null) {
                if (swipeRefreshLayout.isRefreshing) {
                    if (it != LoaderStatus.loading) {
                        swipeRefreshLayout.isRefreshing = false;
                        hideProgress()
                    }
                } else {
                    if (it == LoaderStatus.loading)
                        showProgress()
                    else
                        hideProgress()
                }
            } else if (button == null) {
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


        viewModel.errorMediatorLiveData.observe(this, Observer
        {
            it?.let {
                var updatedErrorMessage: String? = null
                if (it.contains("_")){
                    updatedErrorMessage = it.replace("_", " ")
                    showSnackbar(updatedErrorMessage.toLowerCase())
                }else{
                    updatedErrorMessage = it
                    showSnackbar(updatedErrorMessage.toLowerCase())
                }


                onErrorCalled(updatedErrorMessage.toLowerCase())
            }
        })

        initObservers()


    }

    protected fun initProgress(view: View) {
        mBaseView = view as ViewGroup

        mLoaderView = View.inflate(activity, R.layout.loader, null)

        mBaseView?.let {
            if (futureShowProgress)
                showProgress()
        }
    }


    fun showSnackbar(text: String) {
        mBaseView?.let {
            val snackbar = Snackbar.make(it.rootView, text, Snackbar.LENGTH_LONG)
            snackbar.setDuration(2000)
            val snackbarView = snackbar.getView()
            activity?.let {
                snackbarView.setBackgroundColor(ContextCompat.getColor(it, R.color.secondary))

                val tv = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                tv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.onError))
                tv.gravity = Gravity.CENTER
                snackbar.show()
            }
        }

    }



    /*fun sharedPrefManager: SharedPrefManager {
        return sharedPrefManager
    }*/



    protected abstract fun onErrorCalled(it: String?)

    protected abstract fun initObservers()





    private fun showProgress() {
        hideKeyboard()
        if (mBaseView == null) {
            futureShowProgress = true
        } else if (!progressShown) {
            mBaseView!!.addView(mLoaderView)
            progressShown = true
            futureShowProgress = false
        } else {
            futureShowProgress = false
        }
    }

    private fun hideProgress() {
        futureShowProgress = false
        if (progressShown) {
            mBaseView?.removeView(mLoaderView)
            progressShown = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgress(view)
    }

    fun hideKeyboard() {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = it.getCurrentFocus()
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(it)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard() {
        activity?.let {
            val inputManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // check if no view has focus:
            val view = it.getCurrentFocus()
            view?.let {
                inputManager.toggleSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_FORCED, 0)
            }
        }
    }

    protected fun showAlertDialogOk(title: String, message: String, listener: DialogInterface.OnClickListener) {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK", listener)
            val mAlertDialog = builder.create()
            mAlertDialog.setCanceledOnTouchOutside(false)
            mAlertDialog.setCanceledOnTouchOutside(false)

            mAlertDialog.setOnShowListener({
                context?.let {
                    mAlertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(it, R.color.secondary))
                }
            })
            mAlertDialog.show()
        }
    }
}