package com.malkinfo.janataaapp.views.main.bloodreq

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.text.InputType
import android.text.TextUtils
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.viewmodels.BloodGroupViewModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class RequestForm(val mActivity: Activity,
                  var bloodGroupRequestViewModel:BloodGroupViewModel
) {
    private lateinit var isdialog: Dialog
    private lateinit var patientNamesTI: TextInputEditText
    private lateinit var patient_ageTI: TextInputEditText
    private lateinit var donateLocationsTI: TextInputEditText
    private lateinit var need_dates: TextInputEditText
    private lateinit var concetNum: TextInputEditText
    private lateinit var attendNamesTI: TextInputEditText
    private lateinit var requsetCauseTI: TextInputEditText
    private lateinit var desribeTI: TextInputEditText
    private lateinit var bloodRestTV:TextView
    private lateinit var cancelBtn:Button
    private lateinit var sendRequestBtn :Button

    var cal = Calendar.getInstance()

    fun showBloodRequest(loaction :String,bloodGroupName :String) {
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.form_boold_reqst, null)
        patientNamesTI = dialogView.findViewById(R.id.patientNamesTI)
        patient_ageTI = dialogView.findViewById(R.id.patient_ageTI)
        donateLocationsTI = dialogView.findViewById(R.id.donateLocationsTI)
        need_dates = dialogView.findViewById(R.id.need_dates)
        concetNum = dialogView.findViewById(R.id.concetNum)
        requsetCauseTI = dialogView.findViewById(R.id.requsetCauseTI)
        desribeTI = dialogView.findViewById(R.id.desribeTI)
        bloodRestTV = dialogView.findViewById(R.id.bloodRestTV)
        cancelBtn = dialogView.findViewById(R.id.cancelBtn)
        attendNamesTI = dialogView.findViewById(R.id.attendNamesTI)
        sendRequestBtn = dialogView.findViewById(R.id.sendRequestBtn)
        need_dates.setInputType(InputType.TYPE_NULL);
        bloodRestTV.text = "Request For $bloodGroupName Blood Group"

        donateLocationsTI.setText(loaction)
        getNeedDateBlood()

        /**set Dialog*/

        isdialog =  Dialog(mActivity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        isdialog.setContentView(dialogView)
        isdialog.create()
        cancelBtn.setOnClickListener { isDismiss() }
        sendRequestBtn.setOnClickListener {
            sendRequest(bloodGroupName)
        }

        isdialog.show()
    }

    private fun sendRequest(bloodGroupName: String) {
         if (TextUtils.isEmpty(patientNamesTI.text)
             || TextUtils.isEmpty(donateLocationsTI.text)
             || TextUtils.isEmpty(patient_ageTI.text)
             || TextUtils.isEmpty(need_dates.text)
             || TextUtils.isEmpty(concetNum.text)
             || TextUtils.isEmpty(attendNamesTI.text)
             || TextUtils.isEmpty(requsetCauseTI.text)
             || TextUtils.isEmpty(desribeTI.text)
         ){
             AlertDialog.Builder(mActivity)
                 .setTitle("Box Empty")
                 .setMessage("Please fill All box")
                 .setPositiveButton("Ok"){ dialog,_-> dialog.dismiss() }
                 .create()
                 .show()
         }else{
             val describ = bloodDescirb(bloodGroupName)
             uploadRequest(describ,bloodGroupName)
         }


    }
    fun getNeedDateBlood(){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                getNeedDates()
            }
        }
        need_dates.setOnClickListener {
            DatePickerDialog(mActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    private fun getNeedDates() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
       val  needData = sdf.format(cal.getTime())
        need_dates.setText(needData)

    }

    fun bloodDescirb(bloodGroupName:String):String{
        val patientName = patientNamesTI.text.toString()
        val isloaction = donateLocationsTI.text.toString()
        val patientAge = patient_ageTI.text.toString()
        val date = need_dates.text.toString()
        val concet = concetNum.text.toString()
        val attends = attendNamesTI.text.toString()
        val cause = requsetCauseTI.text.toString()
        val shortDescbi = desribeTI.text.toString()
        return "Request For <b> $bloodGroupName</b> Blood Group" +
                "<br> \n \uD83E\uDE78 \uD83E\uDE78 ❤ ❤️" +
                "\uD83D\uDE4F \uD83D\uDE4F \uD83D\uDE4F ❤ ❤" +
                "️\uD83E\uDE78 \uD83E\uDE78" +
                "<br>=============================<br>" +
                "Patient Name : <font color = Navy><b> $patientName </b></font><br>" +
                "Patient Age : <font color = Navy><b> $patientAge </b></font><br>" +
                "Location : <b> $isloaction </b><br>" +
                "Needed Date : <b> $date </b><br>" +
                "Contact : <b> $concet </b><br>" +
                "Attendant Name: <b> $attends</b><br>" +
                "Cause: <b> $cause </b><br>" +
                "Short Describe : <b> $shortDescbi </b><br>" +
                "=============================<br>" +
                "Dear Friends, I humbly request you to donate blood" +
                " and live every moment proudly as you are saving a life." +
                "Please get in touch with us immediately. <br>" +
                "\n \uD83E\uDE78 \uD83E\uDE78 ❤ ❤️" +
                "\uD83D\uDE4F \uD83D\uDE4F \uD83D\uDE4F ❤ ❤" +
                "️\uD83E\uDE78 \uD83E\uDE78 \n"
    }
    private fun uploadRequest(describ:String, bloodGroupName: String){
        val main = JSONObject()
        try {
            main.put("blood", bloodGroupName)
            main.put("location",describ)
            val jsonParser = JsonParser()
            val gsonObject = jsonParser.parse(main.toString()) as JsonObject
            bloodGroupRequestViewModel.doBloodGroupRequest(gsonObject)


            showImageConfirmation("",
                mActivity.getString(R.string.ok),
                R.drawable.green_tick,
                mActivity.getString(R.string.request_post_alert), "",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    isDismiss()
                })


        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    protected fun showImageConfirmation(negativeText: String, positiveText: String, icon: Int, title: String, message: String, listener: DialogInterface.OnClickListener) {
        val regularttf = Typeface.createFromAsset(mActivity.getAssets(), "font/lexend_regular.ttf")
        val mediumttf = Typeface.createFromAsset(mActivity.getAssets(), "font/lexend_medium.ttf")
        mActivity.let {
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
                mActivity.let {
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
            val alertTitle: TextView = mAlertDialog.window!!.findViewById(androidx.appcompat.R.id.alertTitle)
            alertTitle.typeface = regularttf
        }
    }

    fun isDismiss() {
        isdialog.dismiss()
    }

}