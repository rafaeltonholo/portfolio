package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.refScope
import com.varabyte.kobweb.compose.dom.registerRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun Details(
    isOpened: Boolean,
    summary: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TagElement(
        tagName = "details",
        applyAttrs = modifier.toAttrs(),
    ) {
        registerRefScope(
            refScope {
                ref(isOpened) {
                    if (isOpened) {
                        val attr = document.createAttribute("open")
                        it.setAttributeNode(attr)
                    } else {
                        it.removeAttribute("open")
                    }
                }
            }
        )

        TagElement<HTMLElement>(
            "summary",
            applyAttrs = null,
        ) {
            summary()
        }

        content()
    }
}
