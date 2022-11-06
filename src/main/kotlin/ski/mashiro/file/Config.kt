package ski.mashiro.file

import org.apache.commons.io.FileUtils
import ski.mashiro.pojo.Config
import ski.mashiro.util.Utils
import java.io.File

/**
 * @author FeczIne
 */
class Config {
    companion object {
        lateinit var config : Config
        private val configFile : File = File(Utils.plugin.dataFolder, "Config.yml")
        fun saveConfig() {
            if (!configFile.exists()) {
                createConfigFile()
                return
            }
            FileUtils.writeStringToFile(configFile, Utils.yamlMapper.writeValueAsString(config), "utf-8")
        }

        fun loadConfig() {
            if (!configFile.exists()) {
                createConfigFile()
            }
            val readValue = Utils.yamlMapper.readValue(configFile, Config::class.java)
            config = if (readValue.version == Utils.plugin.description.version.toDouble()) readValue else Config(Utils.plugin.description.version.toDouble(), readValue.checkUpdate)
        }

        fun createConfigFile() {
            if (!Utils.plugin.dataFolder.exists()) {
                Utils.plugin.dataFolder.mkdir()
            }
            if (!configFile.exists()) {
                if (configFile.createNewFile()) {
                    FileUtils.writeStringToFile(configFile, Utils.yamlMapper.writeValueAsString(Config(Utils.plugin.description.version.toDouble(), true)), "utf-8")
                }
            }
        }
    }
}