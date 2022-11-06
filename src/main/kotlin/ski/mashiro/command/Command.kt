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
        if (p3.isEmpty()) {
            p0.sendMessage("=====PlayerDrop=====")
            p0.sendMessage("版本：${Utils.plugin.description.version}")
            p0.sendMessage("作者：${Utils.plugin.description.authors}")
            return true
        }
        if (p0 !is Player) {
            p0.sendMessage("[PlayerDrop] 必须是玩家")
            return true
        }
        if (!p0.hasPermission("playerdrop.admin") && !p0.isOp) {
            p0.sendMessage("[PlayerDrop] 无权限")
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
        if ("group" == cmd) {
            if (p3.size == 1) {
                p0.sendMessage("[PlayerDrop] 用法错误，/pd help 查看帮助")
                return true
            }
            val type = p3[2]
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
                p0.sendMessage("[PlayerDrop] 该掉落物组不存在或类别名重复")
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
                    -2 -> p0.sendMessage("[PlayerDrop] 该掉落物类别不存在")
                }
                return true
            }
            if ("modify" == type) {
                if (p3.size != 5) {
                    p0.sendMessage("[PlayerDrop] 用法错误，/pd group [groupName] modify [partName] [percent]")
                    return true
                }
                val groupName = p3[1]
                val partName = p3[3]
                val percent = try {
                    p3[3].toInt()
                } catch (e : NumberFormatException) {
                    p0.sendMessage("[PlayerDrop] percent应为数字")
                    return true
                }
                val inventory = p0.inventory

                if (DropItemData.modifyDropItemPart(groupName, partName, percent, inventory)) {
                    p0.sendMessage("[PlayerDrop] 掉落物类别修改成功")
                    return true
                }
                p0.sendMessage("[PlayerDrop] 该掉落物组不存在")
                return true
            }
            p0.sendMessage("[PlayerDrop] 用法错误")
            p0.sendMessage("[PlayerDrop] /pd group [groupName] add [partName] [percent]")
            p0.sendMessage("[PlayerDrop] /pd group [groupName] del [partName]")
            p0.sendMessage("[PlayerDrop] /pd group [groupName] modify [partName] [percent]")
            return true
        }
        if ("give" == cmd) {
            if (p3.size != 3) {
                p0.sendMessage("[PlayerDrop] 用法错误，/pd give [groupName] [partName]")
                return true
            }
            val groupName = p3[1]
            val partName = p3[2]
            val dropItemPart = DropItemData.getDropItemPart(groupName, partName)
            if (dropItemPart != null) {
                p0.sendMessage("[PlayerDrop] 给予成功，如背包空间不足会生成在脚下")
                for (itemStack in dropItemPart) {
                    var flag = true
                    for (i in 0..35) {
                        if (p0.inventory.getItem(i) == null) {
                            flag = false
                            p0.inventory.setItem(i, itemStack)
                            break
                        }
                    }
                    if (flag) {
                        p0.world.dropItem(p0.location, itemStack)
                    }
                }
                return true
            }
            p0.sendMessage("[PlayerDrop] 该掉落物组或类别不存在")
            return true
        }
        if ("list" == cmd) {
            if (p3.size == 1) {
                val listDropItem = DropItemData.listDropItem()
                if (listDropItem == null) {
                    p0.sendMessage("[PlayerDrop] 不存在掉落物组，请新建")
                    return true
                }
                p0.sendMessage("=====PlayerDrop=====")
                p0.sendMessage("=======掉落物组=======")
                var index = 1
                for (item in listDropItem) {
                    p0.sendMessage("${index++}. $item")
                }
                return true
            }
            if (p3.size == 2) {
                val groupName = p3[1]
                val listDropItemPart = DropItemData.listDropItemPart(groupName)
                if (listDropItemPart == null) {
                    p0.sendMessage("[PlayerDrop] 该掉落物组不存在")
                    return true
                }
                p0.sendMessage("=====PlayerDrop=====")
                p0.sendMessage("掉落物组：$groupName")
                var index = 1
                for ((name,percent) in listDropItemPart) {
                    p0.sendMessage("${index++}. 类别：$name 掉落概率：$percent%")
                }
                return true
            }
            p0.sendMessage("[PlayerDrop] 用法错误")
            p0.sendMessage("[PlayerDrop] /pd list")
            p0.sendMessage("[PlayerDrop] /pd list [groupName]")
            return true
        }
        if ("help" == cmd) {
            p0.sendMessage("=====PlayerDrop=====")
            p0.sendMessage("/pd create [groupName] add [partName] [percent]")
            p0.sendMessage("/pd delete [GroupName]")
            p0.sendMessage("/pd group [groupName] add [partName] [percent]")
            p0.sendMessage("/pd group [groupName] modify [partName] [percent]")
            p0.sendMessage("/pd group [groupName] del [partName]")
            p0.sendMessage("/pd give [groupName] [partName]")
            p0.sendMessage("/pd list")
            p0.sendMessage("/pd list [groupName]")
            return true
        }
        p0.sendMessage("[PlayerDrop] 用法错误，输入 /pd help 查看")
        return true
    }

    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): MutableList<String>? {
        if (p3.size == 1) {
            return arrayListOf("create", "delete", "group", "give", "list", "help")
        }
        if (p3.size == 3) {
            if (p3[0] == "create") {
                return arrayListOf("add")
            }
            if (p3[0] == "group") {
                return arrayListOf("add", "del", "modify")
            }
        }
        return null
    }

}