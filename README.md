# Seabound

**Seabound** is a PaperMC plugin that reverses the breathing system for designated players. Instead of breathing on land and drowning underwater, affected players thrive underwater and suffocate when on dry land.

---

## Features

- 🌊 **Underwater Breathing**: Selected players can breathe freely underwater without losing air bubbles.
- 💨 **Land Suffocation**: Selected players consume air while on land and begin taking drowning damage once their air reaches zero.
- 🌧️ **Rain Breathing**: Being in rain allows selected players to breathe normally on land.
- 🧪 **Potions & Conduits**: The `Water Breathing` effect and `Conduit Power` protect selected players on land.
- 🪖 **Respiration Support**: The `Respiration` enchantment reduces the chance of losing air on land, just like it does underwater in vanilla Minecraft.
- 🛡️ **Targeted Effect**: Only selected players are affected; all other players retain standard Minecraft breathing mechanics.
- 💾 **Persistence**: Selected players are stored by UUID in `config.yml` and persist across server restarts.
- 🎮 **GameMode Aware**: Players in Creative or Spectator mode are immune to air loss and drowning.

---

## Commands & Permissions

All commands require operator status (`op`) or the permission `seabound.admin`.

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/seabound add <player>` | Enables the reverse breathing system for a player. | `seabound.admin` (op) |
| `/seabound remove <player>` | Disables reverse breathing and restores player's air. | `seabound.admin` (op) |
| `/seabound list` | Lists all players currently using reverse breathing. | `seabound.admin` (op) |

---

## Configuration

Settings are saved in `plugins/Seabound/config.yml`:

```yaml
# Seabound configuration file
# List of UUIDs of players with the reverse breathing system enabled.
players:
  - "00000000-0000-0000-0000-000000000000"
```

---

## Requirements

- **Server**: PaperMC 26.2+
- **Java**: Java 25+

---

## Building from Source

To compile and package the plugin JAR:

```bash
# On Linux/macOS
./gradlew build

# On Windows
.\gradlew.bat build
```

The resulting JAR file will be located in `build/libs/`.

To launch a test server with PaperMC:

```bash
.\gradlew.bat runServer
```
