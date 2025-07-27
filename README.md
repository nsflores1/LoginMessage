# LoginMessage
*Minecraft: give the user a rulebook & a short rules message*

This plugin adds a hook on user login to give them a Written Book item with rules strings formatted in the [MiniMessage](https://docs.advntr.dev/minimessage/index.html) style. It can also do this on login, both for an arbitrary number of strings. Book and rule strings can be reloaded dynamically with `/rulesreload` during server runtime.

This plugin was written with a specific server in mind, and will only be updated for that server. Don't expect it to work on yours, and it definitely won't run for any Minecraft version older than 1.21.1. If you use it, and it breaks, you get to keep both pieces. Also, make sure to change the config files in `src/main/resources/`, they're server-specific.

Pull requests welcome for bug fixes; new features also welcome, but significant additions in complexity not welcome.

