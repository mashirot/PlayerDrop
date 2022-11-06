package ski.mashiro.data

import org.bukkit.Location
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import ski.mashiro.file.DropItem
import ski.mashiro.pojo.DropItemPart
import java.util.*
import kotlin.collections.ArrayList

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
            partList.add(DropItemPart(partName, percent, inventory.contents.asList() as ArrayList<ItemStack>))
            DropItem.dropItemList[groupName] = ski.mashiro.pojo.DropItem(partList)
            return true
        }

        fun addDropItemPart(groupName: String, partName: String, percent: Int, inventory: Inventory) : Boolean {
            if (DropItem.dropItemList.isEmpty() || !DropItem.dropItemList.contains(groupName)) {
                return false
            }
            for (entry in DropItem.dropItemList.entries) {
                if (groupName == entry.key) {
                    var flag = false
                    for (part in entry.value.parts) {
                        if (partName == part.partName) {
                            flag = true
                        }
                    }
                    if (!flag) {
                        entry.value.parts.add(DropItemPart(partName, percent, inventory.contents.asList() as ArrayList<ItemStack>))
                    }
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
                }
            }
            return 0
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