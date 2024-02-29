package serrano.kimberly.sharedpreferences
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import serrano.kimberly.sharedpreferences.PreferenceHelper.defaultPreference
import serrano.kimberly.sharedpreferences.PreferenceHelper.password
import serrano.kimberly.sharedpreferences.PreferenceHelper.userId
import serrano.kimberly.sharedpreferences.PreferenceHelper.clearValues
import serrano.kimberly.sharedpreferences.PreferenceHelper.customPreference


class MainActivity : AppCompatActivity(), View.OnClickListener {

    val CUSTOM_PREF_NAME = "User_data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener(this)
        val btnClear = findViewById<Button>(R.id.btnClear)
        btnClear.setOnClickListener(this)
        val btnShow= findViewById<Button>(R.id.btnShow)
        btnShow.setOnClickListener(this)
        val btnShowDefault = findViewById<Button>(R.id.btnShowDefault)
        btnShowDefault.setOnClickListener(this)

        val defaultPrefs = defaultPreference(this)
        defaultPrefs.userId = 1234
        defaultPrefs.password = "JournalDev"


    }

    override fun onClick(v: View?) {
        val inPassword = findViewById<EditText>(R.id.inPassword)
        val inUserId = findViewById<EditText>(R.id.inUserId)
        val prefs = customPreference(this, CUSTOM_PREF_NAME)
        when (v?.id) {
            R.id.btnSave -> {
                prefs.password = inPassword.text.toString()
                prefs.userId = inUserId.text.toString().toInt()
            }
            R.id.btnClear -> {
                prefs.clearValues

            }
            R.id.btnShow -> {
                inUserId.setText(prefs.userId.toString())
                inPassword.setText(prefs.password)
            }
            R.id.btnShowDefault -> {

                val defaultPrefs = defaultPreference(this)
                inUserId.setText(defaultPrefs.userId.toString())
                inPassword.setText(defaultPrefs.password)
            }
        }
    }


}

object PreferenceHelper {

    val USER_ID = "USER_ID"
    val USER_PASSWORD = "PASSWORD"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    inline fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        val value = pair.second
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    var SharedPreferences.userId
        get() = getInt(USER_ID, 0)
        set(value) {
            editMe {
                it.putInt(USER_ID, value)
            }
        }

    var SharedPreferences.password
        get() = getString(USER_PASSWORD, "")
        set(value) {
            editMe {
                //it.put(USER_PASSWORD to value)
                it.putString(USER_PASSWORD, value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                /*it.remove(USER_ID)
                it.remove(USER_PASSWORD)*/
                it.clear()
            }
        }
}