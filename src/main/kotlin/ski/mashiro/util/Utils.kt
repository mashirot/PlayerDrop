package ski.mashiro.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.bukkit.plugin.Plugin

/**
 * @author FeczIne
 */
class Utils {
    companion object {
        lateinit var plugin : Plugin
        val yamlMapper : ObjectMapper = ObjectMapper(YAMLFactory()).registerModule(kotlinModule())
    }
}