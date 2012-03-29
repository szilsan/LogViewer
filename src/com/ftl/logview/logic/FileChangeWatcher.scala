package com.ftl.logview.logic
import java.nio.file.FileSystems
import java.io.File
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds

/**
 * To watch file changing
 * 
 */
class FileChangeWatcher(file:File) {
	val watcher = FileSystems.getDefault().newWatchService()
	val dir = FileSystems.getDefault().getPath(file.getAbsolutePath())
	val key = dir.register(watcher)
}