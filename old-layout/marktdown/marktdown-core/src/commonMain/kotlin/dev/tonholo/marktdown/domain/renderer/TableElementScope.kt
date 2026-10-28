package dev.tonholo.marktdown.domain.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.tonholo.marktdown.domain.content.TableContent
import dev.tonholo.marktdown.domain.content.TableElement

sealed interface TableElementScope : MarktdownElementScope<TableElement> {

    companion object {
        @Composable
        operator fun invoke(
            element: TableElement,
            drawHeaderContent: @Composable (header: TableContent.Header) -> Unit,
            drawRowContent: @Composable (row: TableContent.Row) -> Unit,
            drawContent: @Composable TableContentScope.() -> Unit,
        ): TableElementScope {
            val tableContentScope = remember(element) {
                TableContentScopeImpl(
                    headerScope = TableHeaderScopeImpl(
                        element = element.header,
                        drawContent = { drawHeaderContent(element.header) },
                    ),
                    rowsScope = element.rows.map { row ->
                        TableRowScopeImpl(
                            element = row,
                            drawContent = { drawRowContent(row) },
                        )
                    },
                )
            }

            return TableElementScopeImpl(
                element = element,
                drawContent = { tableContentScope.drawContent() },
            )
        }
    }
}

sealed interface TableContentScope {
    val headerScope: TableHeaderScope
    val rowsScope: List<TableRowScope>
}

class TableContentScopeImpl(
    override val headerScope: TableHeaderScope,
    override val rowsScope: List<TableRowScope>,
) : TableContentScope

sealed interface TableHeaderScope : MarktdownElementScope<TableContent.Header> {
    companion object {
        @Composable
        operator fun invoke(
            element: TableContent.Header,
            drawContent: @Composable () -> Unit,
        ): TableHeaderScope = TableHeaderScopeImpl(
            element = element,
            drawContent = remember { drawContent },
        )
    }
}

sealed interface TableRowScope : MarktdownElementScope<TableContent.Row> {
    companion object {
        @Composable
        operator fun invoke(
            element: TableContent.Row,
            drawContent: @Composable () -> Unit,
        ): TableRowScope = TableRowScopeImpl(
            element = element,
            drawContent = remember { drawContent },
        )
    }
}

private class TableElementScopeImpl(
    override val element: TableElement,
    override val drawContent: @Composable () -> Unit
) : TableElementScope

private class TableHeaderScopeImpl(
    override val element: TableContent.Header,
    override val drawContent: @Composable () -> Unit
) : TableHeaderScope

private class TableRowScopeImpl(
    override val element: TableContent.Row,
    override val drawContent: @Composable () -> Unit
) : TableRowScope
