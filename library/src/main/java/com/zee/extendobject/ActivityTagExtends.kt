import android.os.Bundle
import com.zee.adapter.LetsGoAdapter
import com.zee.utils.Z1RouterUtils

/**
 *created by zee on 2020/11/27.
 *Activity路由跳转
 */

fun startActivityTag(activityTag: String, module: String = "") {
    startActivityTag(activityTag, module, null)
}


fun startActivityTag(activityTag: String, module: String = "", bundle: Bundle? = null) {
    Z1RouterUtils.startActivity(activityTag, module).putBundle(bundle).letsGo(LetsGoAdapter())
}