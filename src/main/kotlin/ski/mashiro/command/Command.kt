package ski.mashiro.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import ski.mashiro.data.DropItemData
import ski.mashiro.util.Utils

/**
 * @author FeczIne
 */
class Command : TabExecutor {

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 !is Player) {
            p0.sendMessage("必须是玩家")
            return true
        }
        if (!p0.hasPermission("playerdrop.admin") && !p0.isOp) {
            p0.sendMessage("[PlayerDrop] 无权限")
            return true
        }
        if (p3.isEmpty()) {
            p0.sendMessage("=====PlayerDrop=====")
            p0.sendMessage("版本：${Utils.plugin.description.version}")
            p0.sendMessage("作者：${Utils.plugin.description.authors}")
            return true
        }
        val cmd = p3[0]
        if ("create" == cmd) {
            if (p3.size != 5 || "add" != p3[2]) {
                p0.sendMessage("[PlayerDrop] 用法错误，/pd create [groupName] add [partName] [percent]")
                return true
            }
            val groupName = p3[1]
            val partName = p3[3]
            val percent = try {
                p3[4].toInt()
            } catch (e : NumberFormatException) {
                p0.sendMessage("[PlayerDrop] percent应为数字")
                return true
            }
            val inventory = p0.inventory
            if (DropItemData.createDropItem(groupName, partName, percent, inventory)) {
                p0.sendMessage("[PlayerDrop] 掉落物组添加成功")
                return true
            }
            p0.sendMessage("[PlayerDrop] 掉落物组添加失败，[groupName]重复")
            return true
        }
        if ("delete" == cmd) {
            if (p3.size != 2) {
                p0.sendMessage("[PlayerDrop] 用法错误，/pd delete [GroupName]")
                return true
            }
            if (DropItemData.delDropItem(p3[1])) {
                p0.sendMessage("[PlayerDrop] 删除成功")
                return true
            }
            p0.sendMessage("[PlayerDrop] 该掉落物组不存在")
            return true
        }
        val type = p3[2]
        if ("group" == cmd) {
            if ("add" == type) {
                if (p3.size != 5) {
                    p0.sendMessage("[PlayerDrop] 用法错误，/pd group [groupName] add [partName] [percent]")
                    return true
                }
                val groupName = p3[1]
                val partName = p3[3]
                val percent = try {
                    p3[4].toInt()
                } catch (e : NumberFormatException) {
                    p0.sendMessage("[PlayerDrop] percent应为数字")
                    return true
                }
                val inventory = p0.inventory

                if (DropItemData.addDropItemPart(groupName, partName, percent, inventory)) {
                    p0.sendMessage("[PlayerDrop] 掉落物类别添加成功")
                    return true
                }
                p0.sendMessage("[PlayerDrop] 该掉落物组不存在")
                return true
            }
            if ("del" == type) {
                if (p3.size != 4) {
                    p0.sendMessage("[PlayerDrop] 用法错误，/pd group [groupName] del [partName]")
                    return true
                }
                val groupName = p3[1]
                val partName = p3[3]
                when (DropItemData.delDropItemPart(groupName, partName)) {
                    1 -> p0.sendMessage("[PlayerDrop] 掉落物类别删除成功")
                    -1 -> p0.sendMessage("[PlayerDrop] 掉落物类别不能少于1个")
                    0 -> p0.sendMessage("[PlayerDrop] 该掉落物组不存在")
                }
                return true
            }
            p0.sendMessage("[PlayerDrop] 用法错误")
            p0.sendMessage("[PlayerDrop] /pd group [groupName] add [partName] [percent]")
            p0.sendMessage("[PlayerDrop] /pd group [groupName] del [partName]")
            return true
        }
        if ("help" == cmd) {
            p0.sendMessage("=====PlayerDrop=====")
            p0.sendMessage("/pd create [groupName] add [partName] [percent]")
            p0.sendMessage("/pd delete [GroupName]")
            p0.sendMessage("/pd group [groupName] add [partName] [percent]")
            p0.sendMessage("/pd group [groupName] del [partName]")
            return true
        }
        p0.sendMessage("[PlayerDrop] 用法错误，输入 /pd help 查看")
        return true
    }

    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): MutableList<String>? {
        if (p3.isEmpty()) {
            return arrayListOf("create", "delete", "group", "help")
        }
        if (p3.size == 2) {
            if (p3[0] == "create") {
                return arrayListOf("add")
            }
            if (p3[0] == "group") {
                return arrayListOf("add", "del")
            }
        }
        return null
    }
    
}