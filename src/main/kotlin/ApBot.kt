import com.github.kittinunf.fuel.Fuel
import gg.archipelago.client.ArchipelagoClient
import gg.archipelago.client.Print.APPrint
import gg.archipelago.client.Print.APPrintColor
import gg.archipelago.client.Print.APPrintType
import gg.archipelago.client.events.ArchipelagoEventListener
import gg.archipelago.client.events.ConnectionResultEvent
import gg.archipelago.client.events.ReceiveItemEvent
import gg.archipelago.client.network.ConnectionResult
import gg.archipelago.client.parts.NetworkItem
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.lang.Exception
import java.util.*

class ApBot(
    name: String,
    game: String,
    private val whitelist: List<String>,
    private val webhookUrl: String
) : ArchipelagoClient() {
    private val log = LoggerFactory.getLogger(ApBot::class.java)
    private val blacklist = listOf("has joined", "has left", "Now that you are connected")

    init {
        setName(name)
        setGame(game)
        tags = setOf("Tracker")
        eventManager.registerListener(this)
    }

    override fun onPrint(print: String) {
        log.info(print)
    }

    @Synchronized
    override fun onPrintJson(apPrint: APPrint, type: String?, sending: Int, receiving: NetworkItem?) {
        val string = buildString {
            apPrint.parts.forEach {
                val bold = it.type != APPrintType.text
                if (bold) append("**")
                append(it.text)
                if (bold) append("**")
            }
            append(" ")
            if (receiving?.flags?.and(1) == 1) append("\uD83C\uDF1F")
            if (receiving?.flags?.and(1) == 2) append("⭐")
            if (receiving?.flags?.and(1) == 3) append("⚠️")
        }

        log.info(string)


        if (blacklist.any { string.contains(it) }) return
        if (whitelist.none { string.contains(it) }) return

        val (_, res, s) = Fuel.post(webhookUrl)
            .header("Content-Type", "application/json")
            .body("{\"content\":\"$string\", \"username\": \"Archipelago\", \"avatar_url\": \"https://cdn.discordapp.com/icons/731205301247803413/4429240050ece56392e11783905bbdc3.webp?size=96\"}")
            .responseString()

        if (res.statusCode !in 200..299) log.info(String(res.body().toByteArray()))
        Thread.sleep(400)
    }

    override fun onError(ex: Exception) {
        log.error("Client exception", ex)
    }

    override fun onClose(reason: String?, attemptingReconnect: Int) {
        log.info("Closed: $reason")
    }

    @ArchipelagoEventListener
    fun onItemReceived(event: ReceiveItemEvent) {
        log.info("${event.playerName} found ${event.itemName} at ${event.locationName}")
    }

    @ArchipelagoEventListener
    fun onConnectionResult(event: ConnectionResultEvent) {
        if (event.result == ConnectionResult.Success) {
            log.info("Connected")
        }
    }
}