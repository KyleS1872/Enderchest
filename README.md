# Enderchest

A simple API that allows the uploading and downloading of zipped maps or any other file so there's no load on the server.

This was originally made as a replacement for the internal Mineplex Enderchest API.

## Usage

Run the application like an executable jar.

```bash
java -jar Enderchest.jar
```

If you would like to add a custom configuration do the following.

```bash
java -jar Enderchest.jar --maps="/home/mineplex/update/maps/"
```

## Configurable Arguments

```
server.port - Port of API (Default: 8010)
maps - Location of Maps
update - Location of Updated Jar
updateScript - Path of update script
```

## Endpoints

Getting a map - returns a random map for that type given.
```http request
GET /map/{mapType}/next
```

Uploading a map - uploads your map with the type given.
```http request
POST /map/{mapType}/upload?name=Your_Map.zip
```

## Auto Updater

An auto updater is also included for local changes. I've not included my update script, so you'll need a custom script that fetches and restarts your API.

To disable this add an `IgnoreUpdates.dat` file in the same folder as your Jar.

## License

[MIT](https://choosealicense.com/licenses/mit/)