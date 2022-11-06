package ski.mashiro.pojo

import org.bukkit.inventory.ItemStack

/**
 * @author FeczIne
 */
data class DropItemPart(
    val partName : String,
    val percent : Int,
    val items : ArrayList<ItemStack>
) {
}