import com.neovisionaries.ws.client.*
import gg.archipelago.client.ArchipelagoClient
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.util.*

private val config = Properties().apply { load(FileInputStream("config.properties")) }

fun main(args: Array<String>) {
    ApBot(
        config.getProperty("name"),
        config.getProperty("game"),
        config.getProperty("whitelist").split(",").map { it.trim() },
        config.getProperty("webhookUrl")
    ).connect(config.getProperty("archipelagoUrl"))
}