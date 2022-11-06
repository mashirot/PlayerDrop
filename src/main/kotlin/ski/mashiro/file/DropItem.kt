package ski.mashiro.file

import org.apache.commons.io.FileUtils
import ski.mashiro.pojo.DropItem
import ski.mashiro.util.Utils
import java.io.File

/**
 * @author FeczIne
 */
class DropItem {
    companion object {

        val dropItemList = HashMap<String, DropItem>()
        private val dropItemFolder = File(Utils.plugin.dataFolder, "Category")

        fun saveDropItems() {
            if (!dropItemFolder.exists()) {
                dropItemFolder.mkdir()
                return
            }
            if (!dropItemFolder.isDirectory) {
                dropItemFolder.delete()
                dropItemFolder.mkdir()
                return
            }
            if (dropItemList.isEmpty()) {
                return
            }
            val fileList = dropItemFolder.listFiles()
            if (fileList == null || fileList.isEmpty()) {
                for (entry in dropItemList.entries) {
                    val newFile = File(dropItemFolder, entry.key)
                    if (newFile.createNewFile()) {
                        FileUtils.writeStringToFile(newFile, Utils.yamlMapper.writeValueAsString(entry.value), "utf-8")
                    }
                }
                return
            }
            for (entry in dropItemList.entries) {
                var flag = false
                for (file in fileList) {
                    if (entry.key == file.name) {
                        FileUtils.writeStringToFile(file, Utils.yamlMapper.writeValueAsString(entry.value), "utf-8")
                        flag = true
                        break
                    }
                }
                if (!flag) {
                    val newFile = File(dropItemFolder, entry.key)
                    if (newFile.createNewFile()) {
                        FileUtils.writeStringToFile(newFile, Utils.yamlMapper.writeValueAsString(entry.value), "utf-8")
                    }
                }
            }
        }

        fun loadDropItems() {
            if (!dropItemFolder.exists()) {
                dropItemFolder.mkdir()
                return
            }
            if (!dropItemFolder.isDirectory) {
                dropItemFolder.delete()
                dropItemFolder.mkdir()
                return
            }
            val fileList = dropItemFolder.listFiles()
            if (fileList == null || fileList.isEmpty()) {
                return
            }
            for (file in fileList) {
                try {
                    dropItemList[file.name] = Utils.yamlMapper.readValue(file, DropItem::class.java)
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
}