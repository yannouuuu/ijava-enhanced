# 🚀 iJava Enhanced - Installeur & Extension VS Code

**iJava Enhanced** est un outil complet qui combine :
- 🔧 **Un installeur automatique** pour iJava sur votre système (Windows, Linux, macOS)
- 🎨 **Une extension VS Code** avec autocomplétion intelligente et snippets pratiques

Installez facilement **iJava** sur votre système et profitez d'une expérience de développement optimisée dans VS Code !

---

## 📦 Installation

### ⚡ Installation ultra-rapide (one-liner)

**Windows (PowerShell) :**
```powershell
irm https://raw.githubusercontent.com/yannouuuu/ijava-enhanced/main/install_windows.ps1 | iex
```

**Linux :**
```bash
curl -fsSL https://raw.githubusercontent.com/yannouuuu/ijava-enhanced/main/install_linux.sh | bash
```

**macOS :**
```bash
curl -fsSL https://raw.githubusercontent.com/yannouuuu/ijava-enhanced/main/install_mac.sh | bash
```

### 🎯 Installation manuelle (depuis le dépôt cloné)

Si vous préférez cloner d'abord le dépôt :

```bash
git clone https://github.com/yannouuuu/ijava-enhanced.git
cd ijava-enhanced
```

Puis lancez le script correspondant à votre système :

**Windows :**
```powershell
.\install_windows.ps1
```

**Linux :**
```bash
chmod +x install_linux.sh
./install_linux.sh
```

**macOS :**
```bash
chmod +x install_mac.sh
./install_mac.sh
```

Les scripts d'installation vont :
- ✅ Vérifier la présence de Java sur votre système
- ✅ Télécharger la dernière version d'iJava
- ✅ Configurer les wrappers et aliases
- ✅ Ajouter iJava à votre PATH

### 🗑️ Désinstallation

Pour désinstaller complètement iJava de votre système, utilisez simplement :

**Toutes les plateformes :**
```bash
ijava uninstall
```

Le script de désinstallation va :
- 🗑️ Supprimer tous les fichiers iJava (`~/.ijava` ou `%USERPROFILE%\.ijava`)
- 🧹 Nettoyer les profils PowerShell/Bash/Zsh
- 🔗 Retirer iJava du PATH
- ✨ Supprimer tous les aliases

**Note :** Après la désinstallation, redémarrez votre terminal pour que les changements prennent effet.

---

## 🔌 Installation de l'extension VS Code

Une fois iJava installé sur votre système, installez l'extension VS Code pour profiter de l'autocomplétion et des snippets :

### Option 1 : Depuis le marketplace (bientôt disponible)

Recherchez **"iJava Tools"** dans le marketplace VS Code ou installez via :

```bash
code --install-extension yannouuuu.ijava-tools
```

### Option 2 : Installation manuelle (VSIX)

```bash
# Clonez le dépôt
git clone https://github.com/yannouuuu/ijava-enhanced.git
cd ijava-enhanced

# Installez les dépendances
npm install

# Compilez l'extension
npm run compile

# Packagez l'extension
npm run package

# Installez l'extension dans VS Code
code --install-extension ijava-tools-0.0.1.vsix
```

### Option 3 : Développement local

Si vous souhaitez contribuer ou personnaliser l'extension :

```bash
git clone https://github.com/yannouuuu/ijava-enhanced.git
cd ijava-enhanced
npm install
npm run compile
code .
```

Puis appuyez sur `F5` pour lancer l'extension en mode debug.

---

## ✨ Fonctionnalités

### 🔧 Installeur système iJava

L'installeur automatique s'occupe de :
- 📥 Télécharger la dernière version d'iJava depuis le site officiel
- ☕ Vérifier que Java est installé sur votre système
- 📁 Créer le répertoire d'installation (`~/.ijava` ou `%USERPROFILE%\.ijava`)
- 🔗 Configurer les wrappers pour une utilisation facile (`ijava` dans le terminal)
- 🛤️ Ajouter iJava à votre PATH automatiquement
- ⚙️ Créer les aliases pour PowerShell/Bash/Zsh selon votre OS

### 💻 Extension VS Code

#### 🎯 Autocomplétion intelligente pour iJava

L'extension détecte automatiquement les commandes iJava dans vos fichiers shell, PowerShell et batch et propose l'autocomplétion :

- `ijava init` - Initialise un projet iJava
- `ijava compile` - Compile les sources
- `ijava test` - Lance les tests
- `ijava execute` - Exécute le programme principal
- `ijava status` - Affiche l'état du projet
- `ijava start` - Démarre un service
- `ijava stop` - Arrête le service
- `ijava help` - Affiche l'aide

**Commandes ajoutées par le wrapper d'installation :**
- `ijava --info` - Informations sur le wrapper et le toolkit
- `ijava update` / `ijava self-update` - Met à jour le toolkit iJava
- `ijava uninstall` - Désinstalle complètement iJava du système

#### 📝 Snippets Java optimisés

Plus de **50 snippets** pour accélérer votre développement Java :

**Affichage :**
- `ipr` / `println` → `println(message);`
- `prn` / `println0` → `println();`
- `iprint` / `print` → `print(message);`

**Boucles :**
- `for` → Boucle for classique
- `fori` → Boucle for indexée
- `fore` / `foreach` → Boucle for-each
- `while` → Boucle while
- `dow` → Boucle do-while

**Structures :**
- `if` → Condition if
- `charat` / `icar` → Accès caractère dans une chaîne

... et bien plus encore !

#### 🔧 Commandes VS Code

- **Run iJava Command** : Exécute une commande iJava dans un terminal intégré
- **iJava: Show Info** : Affiche les informations sur l'extension

---

## 🎨 Améliorations & fonctionnalités

### 🆕 Installeur système
- ✅ Installation automatique d'iJava en une commande
- ✅ Support multi-plateforme (Windows, Linux, macOS)
- ✅ Vérification automatique de Java
- ✅ Configuration automatique du PATH
- ✅ Création de wrappers et aliases
- ✅ Téléchargement depuis le site officiel

### 🎨 Extension VS Code
- ✅ Autocomplétion complète des commandes iJava
- ✅ Support multi-langages (Shell, PowerShell, Batch)
- ✅ Terminal intégré réutilisable
- ✅ Barre de statut avec indicateur iJava
- ✅ Plus de 50 snippets Java optimisés

### 🔨 Améliorations
- 🎯 Détection intelligente des fichiers `.java` pour compilation
- 🚀 Suggestions contextuelles basées sur la commande tapée
- 📋 Documentation intégrée pour chaque commande
- 🔄 Gestion optimisée du terminal (réutilisation)
- 💾 Support de l'autocomplétion des fichiers

### 🎭 Interface utilisateur
- 📊 Barre de statut interactive
- 🖥️ Terminal dédié pour les commandes iJava
- 📝 Descriptions détaillées des commandes
- 🎨 Icônes et marqueurs visuels

### ⚡ Performance
- 🏃 Activation conditionnelle selon le langage
- 💡 Chargement optimisé au démarrage
- 🔍 Recherche de fichiers asynchrone

---

## 🛠️ Développement

### Prérequis
- Node.js >= 16
- VS Code >= 1.80.0
- TypeScript

### Commandes de développement

```bash
# Compiler le projet
npm run compile

# Mode watch (recompilation automatique)
npm run watch

# Vérifier les erreurs TypeScript
npm run lint

# Créer le package VSIX
npm run package
```

---

## 📄 Licence

MIT - Voir le fichier LICENSE pour plus de détails

---

## 👤 Auteur

**Yann Renard**
- GitHub: [@yannouuuu](https://github.com/yannouuuu)
**Yann Secq** (source [ijava](https://www.iut-info.univ-lille.fr/~yann.secq/ijava/))
- LinkedIn: [@yannsecq](https://www.linkedin.com/in/yannsecq)

---

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :

1. 🍴 Fork le projet
2. 🔨 Créer une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. 💾 Commit vos changements (`git commit -m 'feat: add AmazingFeature'`)
4. 📤 Push vers la branche (`git push origin feature/AmazingFeature`)
5. 🎉 Ouvrir une Pull Request

---

## 📝 Notes

**iJava Enhanced** facilite l'installation et l'utilisation du toolkit **iJava** :
- 🔧 Utilisez les scripts d'installation pour installer iJava sur votre système
- 💻 Installez ensuite l'extension VS Code pour une expérience optimale
- ☕ Assurez-vous que Java (JDK 8 ou supérieur) est installé avant de lancer l'installeur

### 🆘 Aide et support

Si vous rencontrez des problèmes :
1. Vérifiez que Java est installé : `java -version`
2. Consultez les logs d'installation
3. Ouvrez une issue sur [GitHub](https://github.com/yannouuuu/ijava-enhanced/issues)

**Bon développement avec iJava ! 🎉**
