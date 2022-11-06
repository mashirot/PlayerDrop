package ski.mashiro.data

import org.bukkit.Location
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import ski.mashiro.file.DropItem
import ski.mashiro.pojo.DropItemPart
import ski.mashiro.util.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author FeczIne
 */
class DropItemData {
    companion object {

        private val random = Random()

        fun createDropItem(groupName: String, partName : String, percent : Int, inventory : Inventory) : Boolean {
            if (DropItem.dropItemList.contains(groupName)) {
                return false
            }
            val partList = ArrayList<DropItemPart>()
            val inventoryList = Utils.copyItemStackList(inventory.contents.asList())
            partList.add(DropItemPart(partName, percent, inventoryList))
            DropItem.dropItemList[groupName] = ski.mashiro.pojo.DropItem(partList)
            return true
        }

        fun delDropItem(str : String) : Boolean {
            val it = DropItem.dropItemList.entries.iterator()
            while (it.hasNext()) {
                if (it.next().key == str) {
                    it.remove()
                    return true
                }
            }
            return false
        }

        fun addDropItemPart(groupName : String, partName : String, percent: Int, inventory : Inventory) : Boolean {
            if (DropItem.dropItemList.isEmpty() || !DropItem.dropItemList.contains(groupName)) {
                return false
            }
            for (entry in DropItem.dropItemList.entries) {
                if (groupName == entry.key) {
                    for (part in entry.value.parts) {
                        if (partName == part.partName) {
                            return false
                        }
                    }
                    val inventoryList = Utils.copyItemStackList(inventory.contents.asList())
                    entry.value.parts.add(DropItemPart(partName, percent, inventoryList))
                    return true
                }
            }
            return false
        }

        fun delDropItemPart(groupName: String, partName: String) : Int {
            if (DropItem.dropItemList.isEmpty()) {
                return 0
            }
            for (entry in DropItem.dropItemList.entries) {
                if (groupName == entry.key) {
                    if (entry.value.parts.size <= 1) {
                        return -1
                    }
                    val it = entry.value.parts.listIterator()
                    while (it.hasNext()) {
                        if (partName == it.next().partName) {
                            it.remove()
                            return 1
                        }
                    }
                    return -2
                }
            }
            return 0
        }

        fun modifyDropItemPart(groupName : String, partName : String, percent : Int, inventory : Inventory) : Boolean {
            if (DropItem.dropItemList.isEmpty()) {
                return false
            }
            for (entry in DropItem.dropItemList.entries) {
                if (groupName == entry.key) {
                    for (part in entry.value.parts) {
                        if (partName == part.partName) {
                            part.percent = percent
                            part.items = Utils.copyItemStackList(inventory.contents.asList())
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun getDropItemPart(groupName: String, partName: String) : ArrayList<ItemStack>? {
            if (DropItem.dropItemList.isEmpty()) {
                return null
            }
            for (entry in DropItem.dropItemList.entries) {
                if (groupName == entry.key) {
                    for (part in entry.value.parts) {
                        if (partName == part.partName) {
                            return part.items
                        }
                    }
                }
            }
            return null
        }

        fun listDropItem() : List<String>? {
            if (DropItem.dropItemList.isEmpty()) {
                return null
            }
            val dropItemList = ArrayList<String>(DropItem.dropItemList.size)
            for (entry in DropItem.dropItemList.entries) {
                dropItemList.add(entry.key)
            }
            return dropItemList
        }

        fun listDropItemPart(groupName : String) : Map<String, Int>? {
            if (DropItem.dropItemList.isEmpty() || !DropItem.dropItemList.contains(groupName)) {
                return null
            }
            val parts = DropItem.dropItemList[groupName]!!.parts
            val dropItemPartList = HashMap<String, Int>(parts.size)
            for (part in parts) {
                dropItemPartList[part.partName] = part.percent
            }
            return dropItemPartList
        }

        fun dropItem(location : Location, groupName : String) {
            val dropItems = DropItem.dropItemList[groupName]
            val percent = random.nextInt(100)
            for (dropItemPart in dropItems!!.parts) {
                if (percent <= dropItemPart.percent) {
                    for (item in dropItemPart.items) {
                        location.world!!.dropItem(location, item)
                    }
                }
            }
        }

    }
}