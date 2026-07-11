# Jskon

Jskon is a small Skript addon for working with JSON.

It gives you one way to use JSON in Skript:

- read flattened JSON values with `json from %string%`

## Requirements

- Paper
- Skript

## Syntax

### Expression

```skript
json from %string%
```

Use this when you want to read JSON data as a list of values.

## How JSON is flattened

Jskon turns JSON into Skript-friendly paths:

- object keys use `::`
- array indexes start at `1`
- nested data becomes deeper paths

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

Becomes paths like:

- `player::name`
- `player::level`
- `tags::1`
- `tags::2`

## Examples

### Example 1: Read JSON values

Use `json from %string%` when you only want to read the values.

```skript
command /jskontest:
    trigger:
        set {_input} to "{""age"":15,""catchphrase"":""heeyaw"",""nicknames"":[""ftaang"",""bleh""]}"
        set {_json::*} to json from {_input}

        send "&7Root size: %size of {_json::*}%"
        send "&aAge: %{_json::age}%"
        send "&aCatchphrase: %{_json::catchphrase}%"
        send "&aNickname 1: %{_json::nicknames::1}%"
        send "&aNickname 2: %{_json::nicknames::2}%"

        send "&7--- Root values ---"
        loop {_json::*}:
            send "&e%loop-index% = %loop-value%"

        send "&7--- Nicknames ---"
        loop {_json::nicknames::*}:
            send "&b%loop-index% = %loop-value%"
```

## Notes

- Invalid JSON is ignored.
- Empty input is ignored.
- `null` values are stored as `<none>`.
