package ski.mashiro.file

import org.apache.commons.io.FileUtils
import org.bukkit.inventory.ItemStack
import ski.mashiro.pojo.DropItem
import ski.mashiro.pojo.DropItemPart
import ski.mashiro.pojo.DropItemPartStore
import ski.mashiro.pojo.DropItemStore
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
                    val newFile = File(dropItemFolder, "${entry.key}.yml")
                    if (newFile.createNewFile()) {
                        FileUtils.writeStringToFile(newFile, Utils.yamlMapper.writeValueAsString(convertCommonToStore(entry.value)), "utf-8")
                    }
                }
                return
            }
            for (entry in dropItemList.entries) {
                var flag = false
                for (file in fileList) {
                    if ("${entry.key}.yml" == file.name) {
                        FileUtils.writeStringToFile(file, Utils.yamlMapper.writeValueAsString(convertCommonToStore(entry.value)), "utf-8")
                        flag = true
                        break
                    }
                }
                if (!flag) {
                    val newFile = File(dropItemFolder, "${entry.key}.yml")
                    if (newFile.createNewFile()) {
                        FileUtils.writeStringToFile(newFile, Utils.yamlMapper.writeValueAsString(convertCommonToStore(entry.value)), "utf-8")
                    }
                }
            }
        }

        private fun convertCommonToStore(dropItem : DropItem) : DropItemStore {
            val dropItemStoreList = ArrayList<DropItemPartStore>(dropItem.parts.size)
            for (part in dropItem.parts) {
                val toStrList = ArrayList<String>(part.items.size)
                for (item in part.items) {
                    toStrList.add(Utils.itemStackSerialize(item))
                }
                dropItemStoreList.add(DropItemPartStore(part.partName, part.percent, toStrList))
            }
            return DropItemStore(dropItemStoreList)
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
                    dropItemList[file.name.substring(0, file.name.indexOf(".yml"))] = convertStoreToCommon(Utils.yamlMapper.readValue(file, DropItemStore::class.java))
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun convertStoreToCommon(dropItemStore: DropItemStore) : DropItem {
            val dropItemList = ArrayList<DropItemPart>(dropItemStore.parts.size)
            for (part in dropItemStore.parts) {
                val itemStackList = ArrayList<ItemStack>(part.items.size)
                for (item in part.items) {
                    Utils.itemStackDeserialize(item)?.let { itemStackList.add(it) }
                }
                dropItemList.add(DropItemPart(part.partName, part.percent, itemStackList))
            }
            return DropItem(dropItemList)
        }

    }
}