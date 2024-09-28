import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tonholo.portfolio.showcase.generated.resources.Res
import dev.tonholo.portfolio.showcase.generated.resources.brasil
import dev.tonholo.portfolio.showcase.icons.Smiley
import dev.tonholo.portfolio.showcase.ui.theme.ShowcaseTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
expect fun rememberPreviewParam(): State<String?>

@Composable
@Preview
fun App() {
    val preview by rememberPreviewParam()
    ShowcaseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            when (preview) {
                "smiley" -> Smiley(modifier = Modifier.align(Alignment.Center))
                "brasil-avg" -> BrasilAvg(modifier = Modifier.align(Alignment.Center))
                "brasil-avg-zoomed" -> BrasilAvgZoomed(modifier = Modifier.align(Alignment.Center))
                else -> Unit
            }
        }
    }
}

@Composable
fun Smiley(
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Smiley,
        contentDescription = null,
        modifier = modifier.size(128.dp),
        tint = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
fun BrasilAvg(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(Res.drawable.brasil),
        contentDescription = null,
        modifier = modifier
            .width(256.dp)
            .height(179.dp),
    )
}
@Composable
fun BrasilAvgZoomed(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(Res.drawable.brasil),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
    )
}
