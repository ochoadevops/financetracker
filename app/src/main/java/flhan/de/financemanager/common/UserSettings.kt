package flhan.de.financemanager.common

import android.content.SharedPreferences

/**
 * Created by Florian on 03.10.2017.
 */
interface UserSettings {
    fun getUserId(): String
    fun setUserId(id: String)
    fun setHouseholdId(id: String)
    fun getHouseholdId(): String
}

class UserSettingsImpl(
        val sharedPreferences: SharedPreferences
) : UserSettings {
    val userIdKey = "userId"
    val householdIdKey = "householdId"


    override fun getHouseholdId(): String {
        return sharedPreferences.getString(householdIdKey, "")
    }

    override fun setHouseholdId(id: String) {
        sharedPreferences.edit().putString(householdIdKey, id).commit()
    }

    override fun setUserId(id: String) {
        sharedPreferences.edit().putString(userIdKey, id).commit()
    }

    override fun getUserId(): String {
        return sharedPreferences.getString(userIdKey, "")
    }
}