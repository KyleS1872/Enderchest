# Enderchest

A simple API that allows the uploading and downloading of zipped maps or any other file so there's no load on the
server.

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

| Property     | Description                                                       | Default Value                                                                            |
|--------------|-------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| server.port  | The port this API/Microservice runs on.                           | 8010                                                                                     |
| maps         | The location of where maps for game modes are stored.             | Default: `/home/mineplex/update/maps` <br> Windows: `C:\update\maps`                     |
| update       | The location of where your latest/updated jar would be found.     | Default: `/home/mineplex/update/` <br> Windows: `C:\update`                              |
| updateScript | The path to where your [update script](#auto-updater) is located. | Default: `/home/mineplex/updateMicroservice.sh` <br> Windows: `C:\updateMicroservice.sh` |

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

An auto updater is also included for local changes. I've not included my update script, so you'll need a custom script
that fetches and restarts your API.

To disable this add an `IgnoreUpdates.dat` file in the same folder as your Jar.

## License

[MIT](https://choosealicense.com/licenses/mit/)