package example.cketti.localeoverride

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnLayout
import java.util.*

class MainActivity : Activity() {
    // Override locale by replacing the base context
    override fun attachBaseContext(baseContext: Context) {
        val configuration = Configuration(baseContext.resources.configuration).apply {
            setLocales(LocaleList(LocaleManager.locale))
        }
        super.attachBaseContext(baseContext.createConfigurationContext(configuration))
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            LocaleManager.toggle()
            ActivityCompat.recreate(this@MainActivity)
        }

        findViewById<View>(R.id.my_root_view).doOnLayout { view ->
            findViewById<TextView>(R.id.configurationLayoutDirection).text =
                "Configuration layout direction: ${resources.configuration.layoutDirection.displayText()}"

            findViewById<TextView>(R.id.viewLayoutDirection).text =
                "View layout direction: ${view.layoutDirection.displayText()}"
        }
    }
}

object LocaleManager {
    var locale: Locale = Locale("en")
        private set

    fun toggle() {
        locale = if (locale.language != "en") Locale("en") else Locale("ar")
    }
}

fun Int.displayText(): String {
    return when {
        this == View.LAYOUT_DIRECTION_LTR -> "LTR"
        this == View.LAYOUT_DIRECTION_RTL -> "RTL"
        else -> "unknown"
    }
}
