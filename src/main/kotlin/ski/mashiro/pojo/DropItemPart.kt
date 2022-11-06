package ski.mashiro.pojo

import org.bukkit.inventory.ItemStack

/**
 * @author FeczIne
 */
data class DropItemPart(
    val partName : String,
    var percent : Int,
    var items : ArrayList<ItemStack>
) {
}