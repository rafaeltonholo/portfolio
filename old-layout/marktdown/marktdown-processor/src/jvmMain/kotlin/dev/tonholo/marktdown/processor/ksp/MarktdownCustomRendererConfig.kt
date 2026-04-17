package dev.tonholo.marktdown.processor.ksp

import java.nio.file.Path

data class MarktdownCustomRendererConfig(
    val packageName: String,
    val defaultRendererPath: Path,
)
