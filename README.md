# Seabound

**Seabound** is a PaperMC plugin that reverses the breathing system and enhances aquatic movement for designated players. Instead of breathing on land and drowning underwater, affected players thrive underwater, suffocate when on dry land, and swim with dolphin-like speed.

---

## Features

- 🌊 **Underwater Breathing**: Selected players can breathe freely underwater without losing air bubbles.
- 💨 **Land Suffocation**: Selected players consume air while on land and begin taking drowning damage once their air reaches zero.
- 🌧️ **Rain Breathing**: Being in rain allows selected players to breathe normally on land.
- 🧪 **Potions & Conduits**: The `Water Breathing` effect and `Conduit Power` protect selected players on land.
- 🪖 **Respiration Support**: The `Respiration` enchantment reduces the chance of losing air on land, just like it does underwater in vanilla Minecraft.
- 🐬 **Fast Swimming**: Selected players swim with enhanced speed and agility in water, with seamless spacebar ascent and zero desync.
- 👁️ **Enhanced Underwater Vision**: Crystal-clear underwater night vision without annoying particles or potion icons. Players can toggle it at will via `/seabound vision`, `/seavision`, or pressing **Shift + F** while submerged.
- 🔱 **Neutral Guardians**: Guardians and Elder Guardians will not attack Seabound players unless provoked (retaliation mode). Seabound players are also protected from Elder Guardian's `Mining Fatigue` curse.
- 🛡️ **Targeted Effect**: Only selected players are affected; all other players retain standard Minecraft breathing and movement mechanics.
- 💾 **Persistence**: Selected players, vision preferences, and settings are stored in `config.yml` and persist across server restarts.
- 🎮 **GameMode Aware**: Players in Creative or Spectator mode are immune to air loss.

---

## Commands & Permissions

Administrative commands require operator status (`op`) or the permission `seabound.admin`. Player toggles are accessible to all Seabound players.

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/seabound vision` *(or `/seavision`)* | Toggles underwater vision for the executing Seabound player. | Available to Seabound players |
| `/seabound add <player>` | Enables the reverse breathing system for a player. | `seabound.admin` (op) |
| `/seabound remove <player>` | Disables reverse breathing and restores player's air. | `seabound.admin` (op) |
| `/seabound list` | Lists all players currently using reverse breathing. | `seabound.admin` (op) |
| `/seabound info` | Displays an interactive guide with recommended and current configuration values. | `seabound.admin` (op) |
| `/seabound config get <key>` | Views the current value of a configuration parameter. | `seabound.admin` (op) |
| `/seabound config set <key> <value>` | Updates a configuration parameter and saves it to `config.yml`. | `seabound.admin` (op) |
| `/seabound config reset` | Resets all parameters to default recommended values. | `seabound.admin` (op) |
| `/seabound config reload` | Reloads configuration values from `config.yml`. | `seabound.admin` (op) |

---

## Configuration

Settings are saved in `plugins/Seabound/config.yml`:

```yaml
# Seabound Configuration
players:
  - "00000000-0000-0000-0000-000000000000"

movement:
  fast-swimming:
    enabled: true
    speed-multiplier: 1.25

vision:
  enabled: true
  default-enabled: true
  hotkey-toggle: true

guardians:
  enabled: true
  retaliate: true
  protect-from-fatigue: true

player-vision: {}
```

### Configuration Parameters Guide

| Parameter | Default | Recommended Range | Description & Tips |
| :--- | :---: | :---: | :--- |
| `fast-swimming.enabled` | `true` | `true` / `false` | Enables/disables enhanced swim speed in water. |
| `fast-swimming.speed-multiplier` | `1.25` | `1.15` - `1.50` | Swim boost multiplier. |
| `vision.enabled` | `true` | `true` / `false` | Master toggle for clear underwater night vision. |
| `vision.default-enabled` | `true` | `true` / `false` | Default vision state for newly added Seabound players. |
| `vision.hotkey-toggle` | `true` | `true` / `false` | Allow players to toggle vision with **Shift + F** while submerged. |
| `guardians.enabled` | `true` | `true` / `false` | Enable/disable custom guardian passivity mechanics. |
| `guardians.retaliate` | `true` | `true` / `false` | When `true`, guardians are **neutral** (defend if attacked). When `false`, **passive**. |
| `guardians.protect-from-fatigue` | `true` | `true` / `false` | Prevents Elder Guardian's `Mining Fatigue` curse on Seabound players. |

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
