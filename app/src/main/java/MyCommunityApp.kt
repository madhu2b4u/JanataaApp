import android.app.Application
import android.content.Context
import com.malkinfo.janataaapp.MyCommunityAppLifeCycleTracker
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.constants.MyCommunityAppEnv
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.models.Matrimony.MatrimonyUserItem
import com.malkinfo.janataaapp.models.Matrimony.PartnerPrefereneItem
import com.malkinfo.janataaapp.models.launch.User
import com.malkinfo.janataaapp.utils.JsonUtils


class MyCommunityApp  : Application() {

    companion object {
        var DEBUG = true
        var user: User? = null
        var matrimonyUser: MatrimonyUserItem? = null
        var partnerPreferenceDetails: PartnerPrefereneItem? = null

        fun setUser(context: Context, user: User) {
            Companion.user = user

            SharedPrefManager.getInstance(context)
                .setPreference(MyCommunityAppConstants.USER_DATA,JsonUtils.toJson(user))
        }

        fun getUser(context: Context): User? {
            if (user == null) {
                val userString = SharedPrefManager.getInstance(context)
                    .getPreferenceDefNull(MyCommunityAppConstants.USER_DATA)

                userString?.let {
                    user = JsonUtils.parseJson<User>(it)
                }
            }
            return user
        }


        fun setMatrimonyUser(context: Context, matrimonyUser: MatrimonyUserItem) {
            Companion.matrimonyUser = matrimonyUser

            SharedPrefManager.getInstance(context)
                .setPreference(MyCommunityAppConstants.MATRIMONY_USER_DATA, JsonUtils.toJson(matrimonyUser))
                  }

        fun getMatrimonyUser(context: Context): MatrimonyUserItem? {
            if (matrimonyUser == null) {
                val userString = SharedPrefManager.getInstance(context)
                    .getPreferenceDefNull(MyCommunityAppConstants.MATRIMONY_USER_DATA)

                userString?.let {
                    matrimonyUser = JsonUtils.parseJson<MatrimonyUserItem>(it)
                }
            }
            return matrimonyUser
        }

        fun setPartnerPreference(context: Context, partnerPreferenceDetails: PartnerPrefereneItem) {
            Companion.partnerPreferenceDetails = partnerPreferenceDetails

            SharedPrefManager.getInstance(context).setPreference(MyCommunityAppConstants.PARTNER_PREFERENCE_DATA, JsonUtils.toJson(partnerPreferenceDetails))



        }

        fun getPartnerPreference(context: Context): PartnerPrefereneItem? {
            if (partnerPreferenceDetails == null) {
                val userString = SharedPrefManager.getInstance(context)
                    .getPreferenceDefNull(MyCommunityAppConstants.PARTNER_PREFERENCE_DATA)

                userString?.let {
                    partnerPreferenceDetails = JsonUtils.parseJson<PartnerPrefereneItem>(it)
                }
            }
            return partnerPreferenceDetails
        }

    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(MyCommunityAppLifeCycleTracker())
        DEBUG = !MyCommunityAppEnv.PROD_MODE.equals(getString(R.string.environment))

    }




}