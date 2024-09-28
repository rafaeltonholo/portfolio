import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

@Composable
actual fun rememberPreviewParam(): State<String?> = remember {
    derivedStateOf {
        val params = URLSearchParams(window.location.search)
        params.get("preview")
    }
}
