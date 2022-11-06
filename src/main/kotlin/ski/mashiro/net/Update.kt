package ski.mashiro.net

import org.bukkit.plugin.Plugin
import ski.mashiro.file.Config
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class Update {
    companion object {

        private val url = URL("https://update.check.mashiro.ski/PlayerDrop.txt")

        fun checkUpdate(plugin : Plugin) {
            val connection : HttpsURLConnection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            if (connection.responseCode != HttpsURLConnection.HTTP_OK) {
                plugin.logger.info("无法连接至更新服务器")
                return
            }
            val bf = BufferedReader(InputStreamReader(connection.inputStream, "utf-8"))
            bf.run {
                if (bf.readLine() == "${Config.config.version}") {
                    plugin.logger.info("当前为最新版本，感谢您的使用")
                    return
                }
                plugin.logger.info("当前有更新可用，请前往MCBBS发布贴下载")
            }
        }

    }
}