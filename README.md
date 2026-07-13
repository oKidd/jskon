# Jskon

Jskon turns JSON into Skript lists.

## What it does

It takes JSON text and flattens it into Skript-friendly list values.

## Requirements

- Paper
- Skript

## Quick use

If you already have JSON as text, use this:

```skript
set {_json::*} to json from {_input}
```

That gives you the JSON values in a list.

## Example

```skript
command /jskontest:
    trigger:
        set {_input} to "{""age"":15,""catchphrase"":""heeyaw"",""nicknames"":[""ftaang"",""bleh""]}"
        set {_json::*} to json from {_input}

        send "&7Value count: %size of {_json::*}%"
        send "&aAge: %{_json::age}%"
        send "&aCatchphrase: %{_json::catchphrase}%"
        send "&aNickname 1: %{_json::nicknames::1}%"
        send "&aNickname 2: %{_json::nicknames::2}%"
```

## How the data is shaped

Jskon flattens JSON so it is easy to use in Skript:

- object keys use `::`
- array indexes start at `1`
- nested data adds more `::`

Example:

```json
{
  "player": {
    "name": "oKidd",
    "level": 15
  },
  "tags": ["mc", "skript"]
}
```

Becomes:

- `player::name`
- `player::level`
- `tags::1`
- `tags::2`

## Notes

- Invalid JSON is ignored.
- Empty input is ignored.
- `null` values are omitted from the result.
