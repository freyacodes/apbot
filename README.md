# apbot
Discord webhook-based Archipelago tracker

This tracker connects to an Archipelago multiworld and posts messages to a Discord webhook.

![image](https://user-images.githubusercontent.com/2582617/220982175-d6b8c203-a6a2-4801-98b8-4b12945151c1.png)


## Building
It can be compiled with `./gradlew build` (Unix-like) or `gradlew.bar build` (Windows).
The compiled application will be `build/libs/apbot-1.0-SNAPSHOT-all.jar`


## Configuration
The bot must be configured using a file called `config.properties` like so:

```properties
archipelagoUrl=ws://archipelago.gg:12345
webhookUrl=
whitelist=MyFactorioWorld, MyOtherWorld
name=MyFactorioWorld
game=Factorio
```

`archipelagoUrl`: URL of the Archipelago server, typically `ws://archipelago.gg` with a port specific to your multiworld

`webhookUrl`: Discord Webhook url. See https://support.discord.com/hc/en-us/articles/228383668-Intro-to-Webhooks

`whitelist`: A comma-separated list of filters. If `foo, bar` is specified, then any message containing `foo` or `bar` are forwarded.
 
`name`: Player name that Archipelago will connect as. Can be any of the worlds in the multiworld

`game`: The game that matches the specified player name

## Running
Once configured, you can run the JAR file:

`java -jar apbot-1.0-SNAPSHOT-all.jar`
