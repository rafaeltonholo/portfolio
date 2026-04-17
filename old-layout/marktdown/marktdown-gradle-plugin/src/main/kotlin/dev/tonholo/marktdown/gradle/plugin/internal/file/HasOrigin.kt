package dev.tonholo.marktdown.gradle.plugin.internal.file

import org.gradle.api.file.Directory

interface HasOrigin {
    /**
     * The origin directory.
     */
    val origin: Directory?

    /**
     * The origin's source directory.
     */
    fun srcDir(dir: Directory)
}
