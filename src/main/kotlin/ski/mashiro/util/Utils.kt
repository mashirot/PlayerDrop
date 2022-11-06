package ski.mashiro.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.bukkit.Material
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin


/**
 * @author FeczIne
 */
class Utils {
    companion object {
        lateinit var plugin : Plugin
        val yamlMapper : ObjectMapper = ObjectMapper(YAMLFactory()).registerModule(kotlinModule())

        fun copyItemStackList(initList: List<ItemStack?>) : ArrayList<ItemStack> {
            val resultList = ArrayList<ItemStack>(initList.size)
            for (itemStack in initList) {
                if (itemStack == null) {
                    continue
                }
                resultList.add(itemStack.clone())
            }
            return resultList
        }

        fun itemStackSerialize(itemStack: ItemStack?): String {
            val yml = YamlConfiguration()
            yml["item"] = itemStack
            return yml.saveToString()
        }

        fun itemStackDeserialize(str: String?): ItemStack? {
            val yml = YamlConfiguration()
            val item: ItemStack? = try {
                yml.loadFromString(str!!)
                yml.getItemStack("item")
            } catch (ex: InvalidConfigurationException) {
                ItemStack(Material.AIR, 1)
            }
            return item
        }

    }
}