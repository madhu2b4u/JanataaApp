package com.malkinfo.janataaapp.helpers

import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private val CALENDAR_DATE_FORMAT = "yyyy-M-dd"
    private val ENCRYPTION_DATE_FORMAT = "yyyy-MM-dd"
    private val READABLE_DATE_FORMAT = "MM-dd-yyyy"

    private val EXPENSE_DATA_FORMAT = "MMM dd,yyyy"
    private val CASE_UPDATE_DATE_FORMAT = "MMM dd, yyyy"
    private val EVENT_CREATE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val CURRNET_YEAR = "yyyy"

    private val FILTER_FORMAT = "MM/dd/yyyy hh:mm a"

    private val DOB_FORMAT = "MM/dd/yy"
    private val SLASHED_FORMAT = "M/dd/yy"

    private val BIRTHDATE_FORMAT = "yyyy-MM-dd"

    private val SERVICE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"

    private val EXP_READABLE = "MM/yyyy"
    private val EXP_SERVER = "MMyyyy"

    private val IMG_FILE_NAME_FORMAT = "'IMG'-MM-dd-yyyy"

    private val DOCUMENT_CREATE_DATE_FORMAT = "MMM dd,yyyy"
    private val DOCUMENT_MONTHYEAR_FORMAT = "MMMM yyyy"
    private val GET_FULL_FORMAT = "MMM dd yyyy hh:mm aa"
    private val GET_DATE = "dd-MM-yyyy"
    private val DATE = "dd"
    private val MONTH = "MMM"
    private val TIME_FORMAT = "hh:mm a"
    private val MONTH_DATE = "MMM dd"
    private val DATE_TIME = "dd MMM, hh:mm"


//    fun getDateFromTZ(tzString: String): Date {
//        val sdf = SimpleDateFormat(READABLE_DATE_FORMAT, Locale.getDefault())
//        //sdf.timeZone = TimeZone.getTimeZone("UTC")
//        return sdf.parse(tzString)
//    }
//
//    fun getMilliSecondToRedableDateFormat(milliSeconds: Long): String {
//        val formatter = SimpleDateFormat(READABLE_DATE_FORMAT)
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = milliSeconds*1000
//        return formatter.format(calendar.time)
//    }
//
//    fun getMilliSecondToConnectionDateFormat(milliSeconds: Long): String {
//        val formatter = SimpleDateFormat(CONNECTION_DATE_FORMAT)
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = milliSeconds*1000
//        return formatter.format(calendar.time)
//    }
//
//    fun getCurrentTimeInMillis() : Long {
//        return System.currentTimeMillis()
//    }

    fun getFullFormatDate(time: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        return DateFormat.format(GET_FULL_FORMAT, calendar).toString()
    }

    fun getCurrentDate(time: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        return DateFormat.format(GET_DATE, calendar).toString()
    }

    fun getOnlyDate(time: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        return DateFormat.format(DATE, calendar).toString()
    }

    fun getOnlyMonth(time: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        return DateFormat.format(MONTH, calendar).toString()
    }

    fun getTime(time: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        return DateFormat.format(TIME_FORMAT, calendar).toString().toLowerCase()
    }

    fun getMonthAndDate(time: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        return DateFormat.format(MONTH_DATE, calendar).toString()
    }

    fun getDateAndTime(time: Long): String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time
        return DateFormat.format(DATE_TIME, calendar).toString()
    }

    fun getFormattedDate(calDate: String): String {
        try {
            val cal = Calendar.getInstance()
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(CALENDAR_DATE_FORMAT, Locale.US)

            val date = dateFormat.parse(calDate)

            cal.time = date

            val expectedDateFormat = SimpleDateFormat(READABLE_DATE_FORMAT, Locale.US)

            return expectedDateFormat.format(cal.time)

        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }

    fun getBirthDate(dob: String): String {
        try {
            val cal = Calendar.getInstance()
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(
                DOB_FORMAT, Locale.US
            )
            val date = dateFormat.parse(dob)
            cal.time = date
            val expectedDateFormat = SimpleDateFormat(
                BIRTHDATE_FORMAT, Locale.US
            )
            return expectedDateFormat.format(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }


    fun getDateFromString(date: String) : String {

        val instance = Calendar.getInstance()
        var currentMonth = instance[Calendar.MONTH]
        var currentYear = instance[Calendar.YEAR]
        var currentDay = instance[Calendar.DATE]

        try {
            val cal = Calendar.getInstance()
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(
                TIME_FORMAT, Locale.US
            )
            val date = dateFormat.parse(date)
            cal.time = date
            cal.set(Calendar.YEAR,currentYear)
            cal.set(Calendar.MONTH,currentMonth)
            cal.set(Calendar.DATE,currentDay)
            val expectedDateFormat = SimpleDateFormat(
                SERVICE_FORMAT, Locale.US
            )
            return expectedDateFormat.format(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }

    }



    fun getSlashedDate(calDate: String): String {

        try {
            val cal = Calendar.getInstance()
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(
                CALENDAR_DATE_FORMAT, Locale.US
            )
            val date = dateFormat.parse(calDate)
            cal.time = date
            val expectedDateFormat = SimpleDateFormat(
                DOB_FORMAT, Locale.US
            )
            return expectedDateFormat.format(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }

    fun getServiceData(service: String): String {
        try {
            val cal = Calendar.getInstance()
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(SERVICE_FORMAT, Locale.US)
            val date = dateFormat.parse(service)
            cal.time = date
            val expectedDateFormat = SimpleDateFormat(
                READABLE_DATE_FORMAT, Locale.US
            )
            return expectedDateFormat.format(cal.time)

        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }

    fun getFilterCalendar(calDate: String): Calendar {
        val cal = Calendar.getInstance()
        try {
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(
                READABLE_DATE_FORMAT, Locale.US
            )
            val date = dateFormat.parse(calDate)
            cal.time = date
            return cal
        } catch (e: ParseException) {
            e.printStackTrace()
            return cal
        }
    }

    fun getEndFilterCalendar(calDate: String): Calendar {
        val cal = Calendar.getInstance()
        try {
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(
                READABLE_DATE_FORMAT, Locale.US
            )
            val date = dateFormat.parse(calDate)
            cal.time = date
            cal.set(Calendar.HOUR, 12)
            return cal
        } catch (e: ParseException) {
            e.printStackTrace()
            return cal
        }
    }

    fun getMonthDateYearFormat(timeStr: String): String {
        try {
            val cal = Calendar.getInstance()
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(
                ENCRYPTION_DATE_FORMAT, Locale.US
            )
            val date = dateFormat.parse(timeStr)
            cal.time = date
            val expectedDateFormat = SimpleDateFormat(
                CASE_UPDATE_DATE_FORMAT, Locale.US
            )
            return expectedDateFormat.format(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }

    fun getEncryptionFormat(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat(
            ENCRYPTION_DATE_FORMAT, Locale.US
        )

        return dateFormat.format(calendar.time)
    }

    fun getFilterFormat(calendar: Calendar): String {
        val expectedDateFormat = SimpleDateFormat(
            FILTER_FORMAT, Locale.US
        )
        return expectedDateFormat.format(calendar.time)
    }

    fun getEndFilterFormat(calendar: Calendar): String {
        val expectedDateFormat = SimpleDateFormat(
            FILTER_FORMAT, Locale.US
        )
        return expectedDateFormat.format(calendar.time)
    }

    fun getTodayDate(): String {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(
            CASE_UPDATE_DATE_FORMAT, Locale.US
        )
        return dateFormat.format(cal.time)

    }
    fun getTodaySlashFormatDate(): String {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(
            SLASHED_FORMAT, Locale.US
        )
        return dateFormat.format(cal.time)

    }

    fun getCurrentYear():String{
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(
            CURRNET_YEAR, Locale.US
        )
        return dateFormat.format(cal.time)

    }

    fun getExpReadable(expCal: Calendar): String {
        val dateFormat = SimpleDateFormat(
            EXP_READABLE, Locale.US
        )
        return dateFormat.format(expCal.time)
    }

    fun getExpServer(expCal: Calendar): String {
        val dateFormat = SimpleDateFormat(
            EXP_SERVER, Locale.US
        )
        return dateFormat.format(expCal.time)
    }

    fun getImageFileName(): String {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(
            IMG_FILE_NAME_FORMAT, Locale.US
        )
        return dateFormat.format(cal.time)
    }

    fun getDocumentMonthYear(createdDate: String?): String {
        try {
            createdDate?.let {
                val cal = Calendar.getInstance()
                // Date date = new Date();
                val dateFormat = SimpleDateFormat(
                    DOCUMENT_CREATE_DATE_FORMAT, Locale.US
                )
                val date = dateFormat.parse(it)
                cal.time = date
                val expectedDateFormat = SimpleDateFormat(
                    DOCUMENT_MONTHYEAR_FORMAT, Locale.US
                )
                return expectedDateFormat.format(cal.time)
            }
        } catch (e: ParseException) {
            return ""
        }
        return ""
    }

    fun parseFromCreatedDate(createdDate: String?): Long? {
        try {
            createdDate?.let {
                // Date date = new Date();
                val dateFormat = SimpleDateFormat(
                    DOCUMENT_CREATE_DATE_FORMAT, Locale.US
                )
                val date = dateFormat.parse(it)
                return date?.time
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun calendarFromBirthData(birthDate: String): Calendar {
        val cal = Calendar.getInstance()
        try {
            // Date date = new Date();
            val dateFormat = SimpleDateFormat(
                BIRTHDATE_FORMAT, Locale.US
            )
            val date = dateFormat.parse(birthDate)
            cal.time = date
            return cal
        } catch (e: ParseException) {
            e.printStackTrace()
            return cal
        }

    }

    //Feb 10,2020
    fun getDateFromCreatedDateString(createdDate: String?): Date? {
        createdDate?.let {
            try {
                val dateFormat = SimpleDateFormat(
                    EXPENSE_DATA_FORMAT, Locale.US
                )
                return dateFormat.parse(it)
            }catch ( exp: ParseException){
                exp.printStackTrace()
                return null
            }
        }
        return null
    }

    //Feb 10,2020
    fun getDate(createdDate: String?): Date? {
        createdDate?.let {
            try {
                val dateFormat = SimpleDateFormat(
                    SERVICE_FORMAT, Locale.US
                )
                return dateFormat.parse(it)
            }catch ( exp: ParseException){
                exp.printStackTrace()
                return null
            }
        }
        return null
    }

}