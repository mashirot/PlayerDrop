package ski.mashiro.pojo

/**
 * @author FeczIne
 */
data class DropItemPartStore(
    val partName : String,
    var percent : Int,
    var items : ArrayList<String>
) {
}