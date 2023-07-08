package me.redepicness.bingo.util

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

object FileUtils {
    fun deleteFolder(path: Path?) {
        if(path == null) return
        if (Files.notExists(path)) return
        try {
            Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {

                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    Files.delete(file)
                    return FileVisitResult.CONTINUE
                }

                override fun postVisitDirectory(dir: Path, e: IOException?): FileVisitResult {
                    return if (e == null) {
                        Files.delete(dir)
                        FileVisitResult.CONTINUE
                    } else throw e
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
