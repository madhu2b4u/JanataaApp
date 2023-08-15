package com.malkinfo.janataaapp.views.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.google.android.material.snackbar.Snackbar
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.viewmodels.MyBaseViewModel
import kotlinx.coroutines.Runnable


abstract class MyBaseFragment : Fragment() {
    protected val TAG = this.javaClass.simpleName
    private var mBaseView: ViewGroup? = null
    private var mLoaderView: View? = null
    private var progressShown = false
    private var futureShowProgress = false
    private var button: CircularProgressButton? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    var runnable: Runnable? = null
    var handler: Handler? = null
    var scrollPosition = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgress(view)
    }


    //Call this function to set up loaders
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
                if (it.contains("_")) {
                    updatedErrorMessage = it.replace("_", " ")
                    showSnackbar(updatedErrorMessage.toLowerCase())
                } else {
                    updatedErrorMessage = it
                    showSnackbar(updatedErrorMessage.toLowerCase())
                }


                onErrorCalled(updatedErrorMessage.toLowerCase())
            }
        })

        initObservers()

    }

    protected abstract fun onErrorCalled(it: String?)

    protected abstract fun initObservers()

    protected fun initProgress(view: View) {
        mBaseView = view as ViewGroup

        mLoaderView = View.inflate(activity, R.layout.loader, null)
        mBaseView?.let {
            if (futureShowProgress)
                showProgress()
        }
    }

    val sharedPrefManager: SharedPrefManager
        get() {
            return SharedPrefManager.getInstance(requireActivity())
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
            val inputManager =
                it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // check if no view has focus:
            val view = it.getCurrentFocus()
            view?.let {
                inputManager.toggleSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.SHOW_FORCED,
                    0
                )
            }
        }
    }

    fun showSnackbar(text: String) {
        mBaseView?.let {
            val snackbar = Snackbar.make(
                activity?.findViewById(android.R.id.content)!!,
                text,
                Snackbar.LENGTH_LONG
            )
            snackbar.setDuration(2000)
            val snackbarView = snackbar.getView()
            requireActivity().let {
                snackbarView.setBackgroundColor(ContextCompat.getColor(it, R.color.button_bg))

                val tv = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                tv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                tv.gravity = Gravity.CENTER
                snackbar.show()
            }
        }

    }

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

    protected fun showAlertDialog(negativeText: String, positiveText: String, title: String, message: String, listener: DialogInterface.OnClickListener) {
        val regularttf = Typeface.createFromAsset(requireActivity().getAssets(), "font/lexend_regular.ttf")
        val mediumttf = Typeface.createFromAsset(requireActivity().getAssets(), "font/lexend_medium.ttf")
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(positiveText, listener)
            builder.setNegativeButton(negativeText,
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            val mAlertDialog = builder.create()
            mAlertDialog.setCanceledOnTouchOutside(false)
            mAlertDialog.setCancelable(false)

            mAlertDialog.setOnShowListener {
                context?.let {
                    mAlertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(it, R.color.red))
                    mAlertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(it, R.color.textGrey))
                    mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTypeface(mediumttf)
                    mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTypeface(mediumttf)
                    mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false)
                    mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false)
                }
            }
            mAlertDialog.show()
            val alertMessage: TextView = mAlertDialog.window!!.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)
            alertMessage.typeface = mediumttf
        }
    }

    protected fun showConfirmation(negativeText: String, positiveText: String, title: String, message: String, listener: DialogInterface.OnClickListener) {
        val regularttf = Typeface.createFromAsset(requireActivity().getAssets(), "font/lexend_regular.ttf")
        val mediumttf = Typeface.createFromAsset(requireActivity().getAssets(), "font/lexend_medium.ttf")
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(positiveText, listener)
            builder.setNegativeButton(negativeText,
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            val mAlertDialog = builder.create()
            mAlertDialog.setCanceledOnTouchOutside(false)
            mAlertDialog.setCancelable(false)

            mAlertDialog.setOnShowListener {
                context?.let {
                    mAlertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(it, R.color.textBlue))
                    mAlertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(it, R.color.textGrey))
                    mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTypeface(mediumttf)
                    mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTypeface(mediumttf)
                    mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false)
                    mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false)
                }
            }
            mAlertDialog.show()
            val alertMessage: TextView = mAlertDialog.window!!.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)
            alertMessage.typeface = mediumttf
        }
    }

    protected fun showImageConfirmation(negativeText: String, positiveText: String, icon: Int, title: String, message: String, listener: DialogInterface.OnClickListener) {
        val regularttf = Typeface.createFromAsset(requireActivity().getAssets(), "font/lexend_regular.ttf")
        val mediumttf = Typeface.createFromAsset(requireActivity().getAssets(), "font/lexend_medium.ttf")
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setIcon(icon)
            builder.setMessage(message)
            builder.setPositiveButton(positiveText, listener)
            builder.setNegativeButton(negativeText,
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            val mAlertDialog = builder.create()
            mAlertDialog.setCanceledOnTouchOutside(false)
            mAlertDialog.setCancelable(false)

            mAlertDialog.setOnShowListener {
                context?.let {
                    mAlertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(it, R.color.textBlack))
                    mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTypeface(mediumttf)
                    mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTypeface(mediumttf)
                    mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false)
                    mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false)
                }
            }
            mAlertDialog.show()

            //val view: Window? = mAlertDialog.window
            val alertTitle: TextView = mAlertDialog.window!!.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)
            alertTitle.typeface = regularttf
        }
    }


    protected fun autoScroll(count: Int,partnerProfileImageVP:ViewPager) {
        var scrollPosition = 0
        handler = Handler()
        runnable = object :Runnable{
            override fun run() {
                partnerProfileImageVP.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        scrollPosition = position + 1
                    }

                    override fun onPageScrollStateChanged(state: Int) {}
                })
            }

        }

        handler!!.postDelayed(runnable!!, 3000)
    }



}