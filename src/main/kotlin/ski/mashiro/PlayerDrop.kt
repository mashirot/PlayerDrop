package ski.mashiro

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import ski.mashiro.command.Command
import ski.mashiro.file.Config
import ski.mashiro.file.DropItem
import ski.mashiro.listener.Listener
import ski.mashiro.net.Update
import ski.mashiro.util.Utils

/**
 * @author FeczIne
 */
class PlayerDrop : JavaPlugin() {
    override fun onLoad() {
        this.logger.info("加载中")
        Utils.plugin = this
        Config.createConfigFile()
    }

    override fun onEnable() {
        Config.loadConfig()
        DropItem.loadDropItems()
        Bukkit.getPluginCommand("playerdrop")!!.setExecutor(Command())
        Bukkit.getPluginCommand("playerdrop")!!.tabCompleter = Command()
        Bukkit.getPluginManager().registerEvents(Listener(), this)
        this.logger.info("加载完成")
        Update.checkUpdate(this)
    }

    override fun onDisable() {
        Config.saveConfig()
        DropItem.saveDropItems()
        this.logger.info("已卸载")
    }
}