package ua.frist008.action.record.core.ui.navigation

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.navigation.NavHostController
import timber.log.Timber
import ua.frist008.action.record.core.util.media.MimeType
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.NewRoot

class Router(private val navController: NavHostController) : (NavCommand) -> Unit {

    override operator fun invoke(command: NavCommand) {
        when (command) {
            is NavCommand.BackCommand -> {
                val isPopped =
                    if (command.backToScreen == null) {
                        navController.popBackStack()
                    } else {
                        navController.popBackStack(
                            route = command,
                            inclusive = true,
                        )
                    }

                if (!isPopped) {
                    // For enableOnBackInvokedCallback=true from Manifest
                    val activity = navController.context as? ComponentActivity
                    activity?.onBackPressedDispatcher?.onBackPressed()
                        ?: Timber.e("Can't handle command $command")
                }
            }

            is NewRoot -> navController.navigate(command.newRootScreen) {
                popUpTo(navController.graph.id) {
                    inclusive = command.isReplaceScreen
                }
            }

            is NavCommand.Link -> {
                val customTabsIntent = CustomTabsIntent
                    .Builder()
                    .setShareState(CustomTabsIntent.SHARE_STATE_ON)
                    .setShowTitle(true)
                    .build()

                customTabsIntent.launchUrl(navController.context, Uri.parse(command.url))
            }

            is NavCommand.App -> {
                val intent = CustomTabsIntent.Builder()
                    .setShareState(CustomTabsIntent.SHARE_STATE_ON)
                    .build()
                intent.intent.setPackage(command.appPackage)

                intent.launchUrl(navController.context, Uri.parse(command.url))
            }

            is NavCommand.Share -> {
                val context = navController.context
                val subject = context.getString(command.messageRes)

                val intent = ShareCompat.IntentBuilder(navController.context)
                    .setType((MimeType.PLAIN_TEXT))
                    .setSubject(subject)
                    .setText(command.url)
                    .intent

                context.startActivity(intent)
            }

            else -> navController.navigate(command) {
                val route = navController.currentBackStackEntry?.destination?.route
                popUpTo(route ?: return@navigate) {
                    inclusive = command.isReplaceScreen
                }
            }
        }
    }
}
