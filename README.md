# DeathSwap

DeathSwap est un plugin Minecraft **Spigot 1.8.8** basé sur le mini-jeu DeathSwap.

Le principe est simple :  
toutes les X secondes, les joueurs **échangent de position**.  
Le but est de piéger les autres joueurs pour provoquer leur mort.

---

## ✨ Fonctionnalités

- Système de swap automatique
- Timer configurable
- WorldBorder configurable
- Système de kits personnalisés
- Lobby configurable
- Compatible Spigot / Paper 1.8.8

---

## 📦 Installation

1. Télécharger le fichier `DeathSwap.jar`
2. Placer le fichier dans le dossier `plugins`
3. Démarrer ou redémarrer le serveur
4. Configurer les fichiers dans `plugins/DeathSwap/`

---

## ⌨️ Commandes

| Commande | Description |
|--------|------------|
| `/deathswap start` | Démarrer une partie |
| `/deathswap stop` | Arrêter la partie |
| `/deathswap kits` | Voir les kits disponibles |
| `/deathswap kits <nom>` | Sélectionner un kit |

---


## ⚙️ Configuration

### 📁 `config.yml`

```yml
prefix: "&6&lDeathSwap&7 »"

# Timer entre swap (en secondes)
timer: 45

# Taille de la map
border: 300

# Coordonnées du lobby
lobby:
  x:
  y:
  z:
  ```
---


## ⚙️ Configuration des Kits

### 📁 `kits.yml`

```yml
kits:
  # Nom du kit
  Mineur:
    # Description du kit
    description:
      - "Équipement de mineur"
    # Items présents dans le kit
    items:
      - material: STONE_PICKAXE
        amount: 1
      - material: COOKED_CHICKEN
        amount: 16
```

---

## ⚙️ Compilation / Build

Si tu veux compiler le plugin toi-même avec **Gradle**, utilise la commande suivante dans le terminal à la racine du projet :

```bash
./gradlew build