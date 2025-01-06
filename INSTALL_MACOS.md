# Installationsanleitung für macOS

## Voraussetzungen

Homebrew sollte bereits installiert sein. Falls nicht:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

## Erforderliche Pakete

1. Java Development Kit (OpenJDK 11):
```bash
brew install openjdk@11
```

2. Maven für Build-Management:
```bash
brew install maven
```

3. Git für Versionskontrolle:
```bash
brew install git
```

## Environment Setup

1. Java in die Shell-Konfiguration einbinden. Fügen Sie folgende Zeilen in ~/.zshrc oder ~/.bash_profile ein:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
export PATH="$JAVA_HOME/bin:$PATH"
```

2. Shell neu laden:
```bash
source ~/.zshrc  # oder source ~/.bash_profile
```

## Verifizierung der Installation

Überprüfen Sie die Installationen:

```bash
java --version    # Sollte OpenJDK 11.x.x anzeigen
mvn --version     # Sollte Maven 3.x.x anzeigen
git --version     # Sollte Git 2.x.x anzeigen
```

## Projekt Setup

1. Repository klonen:
```bash
git clone https://github.com/scimbe/MyDumpDemo.git
cd MyDumpDemo
```

2. Projekt bauen:
```bash
mvn clean install
```

3. Projekt ausführen:
```bash
mvn exec:java -Dexec.mainClass="editor.SimpleEditor"
```

## Fehlerbehebung

1. Falls Java nicht gefunden wird:
```bash
brew link openjdk@11
```

2. Falls Maven Probleme auftreten:
```bash
brew reinstall maven
```

3. Falls Berechtigungsprobleme auftreten:
```bash
chmod +x mvnw    # Falls ein Maven Wrapper verwendet wird
```

## IDE Setup (Optional)

1. IntelliJ IDEA Installation:
```bash
brew install --cask intellij-idea-ce
```

2. Visual Studio Code Installation:
```bash
brew install --cask visual-studio-code
```