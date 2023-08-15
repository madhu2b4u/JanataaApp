package com.malkinfo.janataaapp.views.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.enums.ErrorMessageType
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.helpers.SafeClickListener
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.viewmodels.MyBaseViewModel
import com.google.android.material.snackbar.Snackbar

abstract class MyAppCompatActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
    protected val WAIT_TIME: Long = 500


    protected var animateFinish = true
    private var mBaseView: ViewGroup? = null
    private var mLoaderView: View? = null
    private var progressShown = false

    companion object {
        var mLanguageCode: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initProgress()
    }

    //Initializing the progress view
    private fun initProgress() {
        mBaseView = this.findViewById(android.R.id.content)
        mLoaderView = View.inflate(this, R.layout.loader, null)
    }

    //Get the instance of sharedPreferenceManager
    val sharedPrefManager: SharedPrefManager
        get() {
            return SharedPrefManager.getInstance(this)
        }

    //To hide Keyboard
    fun hideKeyboard() {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager;
        val view = this.currentFocus;
        if (view != null)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0);
    }

    //To show keyboard
    fun showKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val view = this.currentFocus
        if (view != null)
            inputManager.toggleSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
    }

    //To show snackbar
    protected fun showSnackbar(
        message: String?,
        errorMessageType: ErrorMessageType = ErrorMessageType.snackbar
    ) {
        val snackbarMessage = SpannableStringBuilder(message)
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), snackbarMessage,
            Snackbar.LENGTH_LONG
        )
        snackbar.setDuration(2000)
        val snackBarView = snackbar.view
        var snackbarBg = R.color.onPrimary
        var snackbarTextColor = R.color.onError
        if (errorMessageType == ErrorMessageType.snackbarError) {
            snackbarBg = R.color.error
            snackbarTextColor = R.color.onError
        }
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, snackbarBg))
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.maxLines = 4
        textView.setTextColor(ContextCompat.getColor(this, snackbarTextColor))
        snackbar.show()
    }

    //To show toast message
//    protected fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

    private val defaultDialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    }

    //To show alert dialog with 'ok' button alone
    protected fun showAlertDialogOk(
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener = defaultDialogClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok), listener)
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener({
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.secondary))
        })

        mAlertDialog.show()
    }

    //To show alert dialog with Positive and Negative button with positive button listener alone
    protected fun showConfirmation(
        negativeText: String,
        positiveText: String,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, listener)
        builder.setNegativeButton(negativeText) { dialog, _ -> dialog.dismiss() }
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener({
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.secondary))
            mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.green))
        })


        mAlertDialog.show()
    }

    //To show alert dialog with Positive and Negative button with positive and negative button listener
    protected fun showConfirmation(
        negativeText: String,
        positiveText: String,
        title: String?,
        message: String,
        listener: DialogInterface.OnClickListener,
        negativeListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        if (title != null)
            builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, listener)
        builder.setNegativeButton(negativeText, negativeListener)
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener({
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.secondary))
            mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.green))
        })

        mAlertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //To show loader progress
    open fun showProgress() {
        hideKeyboard()
        if (!progressShown) {
            mBaseView!!.addView(mLoaderView)
            progressShown = true
        }
    }

    //To hide loader progress
    open fun hideProgress() {
        if (progressShown) {
            mBaseView!!.removeView(mLoaderView)
            progressShown = false
        }
    }


    //Call this method while setting up Viewmodel to init progress
    protected fun setUpLoader(viewModel: MyBaseViewModel) {
        viewModel.isLoading.observe(this, Observer {
            if (it.equals(LoaderStatus.loading))
                showProgress()
            else
                hideProgress()
        })

        viewModel.errorMediatorLiveData.observe(this, Observer {
            it?.let {
                var updatedErrorMessage: String? = null
                if (it.contains("_")){
                    updatedErrorMessage = it.replace("_", " ")
                    showSnackbar(updatedErrorMessage.toLowerCase())
                }else{
                    updatedErrorMessage = it
                }


                onErrorCalled(updatedErrorMessage.toLowerCase())

            }
        })

        initObservers()

    }

    abstract fun initObservers()

    protected abstract fun onErrorCalled(it: String?)

    protected fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    /*fun getUser(): User? {
        if (sharedPrefManager.getContainsPreference(MyConstants.USER_DATA)) {
            if (user == null) {
                user = Gson().fromJson(SharedPrefManager(this).getPreference(MyConstants.USER_DATA), User::class.java)
            }
        }
        return user
    }

    fun clearUser() {
        user = null
    }

    protected fun updateUser(it: User) {
        user = it
    }*/

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}