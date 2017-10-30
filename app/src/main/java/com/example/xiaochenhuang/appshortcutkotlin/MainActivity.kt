package com.example.xiaochenhuang.appshortcutkotlin

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import java.util.*

class MainActivity : AppCompatActivity() {

    var URL_PREFIX = "https://www."
    var URL_SUFFIX = ".com"
    var SHORTCUT_ID_1 = "Dynamic Shortcut 1"
    var SHORTCUT_ID_2 = "Dynamic Shortcut 2"

    lateinit var shortcutManager: ShortcutManager
    lateinit var context: Context

    @BindView(R.id.add_website_edit_text)
    lateinit var websiteEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        shortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
        context = baseContext as Context
    }

    @OnClick(R.id.create_dynamic_shortcut)
    internal fun createDynamicShortcut() {
        var dynamicShortcuts = shortcutManager.dynamicShortcuts
        var number = shortcutManager.dynamicShortcuts.size + 1
        var label = websiteEditText.text.toString()
        var url = URL_PREFIX + label + URL_SUFFIX

        var shortcutInfo = ShortcutInfo.Builder(this, getString(R.string.dynamic_shortcut_id, number))
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                .setShortLabel(label)
                .setLongLabel(label)
                .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                .build()

        if (hasDynamicShortcuts()) {
            shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcutInfo))
        } else {
            dynamicShortcuts.add(shortcutInfo)
            shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcutInfo))
        }
    }

    @OnClick(R.id.set_rank_button)
    internal fun setRank() {
        var dynamicShortcuts = arrayListOf<ShortcutInfo>()
        var shortcutInfo1 = ShortcutInfo.Builder(this, SHORTCUT_ID_1).setRank(1).build()
        var shortcutInfo2 = ShortcutInfo.Builder(this, SHORTCUT_ID_2).setRank(2).build()
        dynamicShortcuts.add(shortcutInfo1)
        dynamicShortcuts.add(shortcutInfo2)
        shortcutManager.updateShortcuts(dynamicShortcuts)
    }

    @OnClick(R.id.create_pinned_shortcut_button)
    internal fun createPinnedShortcut() {
        if (shortcutManager.isRequestPinShortcutSupported) {
            var pinnedShortcutInfo = ShortcutInfo.Builder(context, SHORTCUT_ID_1).build()
            shortcutManager.requestPinShortcut(pinnedShortcutInfo, null)

            //Create the PendingIntent object only if your app needs to be notified
            //Configure the intent so that your app's broadcast receiver gets the callback successfully.
            //var pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinnedShortcutInfo);
            //var successCallback = PendingIntent.getBroadcast(context, 0, pinnedShortcutCallbackIntent, 0);
            //shortcutManager.requestPinShortcut(pinnedShortcutInfo, successCallback.getIntentSender());
        }
    }

    @OnClick(R.id.remove_button)
    internal fun removeShorcut() {
        var shortcutIds = Collections.singletonList(shortcutManager.dynamicShortcuts.get(0).id)
        //disable pinned shortcuts, showing user a custom error message when they try to select the disabled shortcuts
        shortcutManager.disableShortcuts(shortcutIds, getString(R.string.disable_pinned_shortcuts_error_message))
        shortcutManager.removeDynamicShortcuts(shortcutIds)
    }

    private fun hasDynamicShortcuts(): Boolean {
        return shortcutManager.dynamicShortcuts.size != 0
    }
}
