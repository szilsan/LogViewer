package com.ftl.logview.logic
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds

/**
 * To watch file changing
 *
 */
class FileChangeWatcher(file: File) {
  val watcher = FileSystems.getDefault().newWatchService()
  val dir = FileSystems.getDefault().getPath(file.getParent())
  val keyModify = dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY)
  val keyDelete = dir.register(watcher, StandardWatchEventKinds.ENTRY_DELETE)
}