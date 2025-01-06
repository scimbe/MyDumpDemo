# MyDumpDemo

## Überblick
Dieses Repository demonstriert die Interaktionsmöglichkeiten zwischen dem Claude AI Assistant und JetBrains Entwicklungswerkzeugen. Es dient als praktisches Beispiel für die Integration von KI-gestützter Entwicklung in eine professionelle IDE-Umgebung.

## Simple Editor
Das Projekt enthält einen einfachen Text-Editor mit grundlegenden Funktionen:
- Dateien öffnen und speichern
- Grundlegende Textbearbeitung
- Swing-basierte Benutzeroberfläche

## Projektstruktur
```
├── src
│   ├── main/java/editor
│   │   └── SimpleEditor.java
│   └── test/java/editor
│       └── SimpleEditorTest.java
├── pom.xml
└── .gitignore
```

## Entwicklung
Das Projekt verwendet Maven als Build-Tool. Zum Starten:

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="editor.SimpleEditor"
```

## Status
Dieses Projekt befindet sich in der Entwicklung und wird kontinuierlich erweitert.

## Lizenz
MIT License