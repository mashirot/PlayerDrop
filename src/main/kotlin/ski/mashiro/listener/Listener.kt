package ski.mashiro.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import ski.mashiro.data.DropItemData
import ski.mashiro.file.DropItem

/**
 * @author FeczIne
 */
class Listener : Listener {

    @EventHandler
    fun playerDeathEvent(e : EntityDeathEvent) {
        if (e.entity !is Player || e.entity.killer == null) {
            return
        }
        if (DropItem.dropItemList.isEmpty()) {
            return
        }
        val player = e.entity as Player
        for (entry in DropItem.dropItemList.entries) {
            if (player.hasPermission("playerdrop.drop.${entry.key}")) {
                DropItemData.dropItem(player.location, entry.key)
                break
            }
        }
    }

}