# HumbleView

## Features

The features listed below refers to the sample application.

#### Offline mode

The Activity OfflineActivity shows an example of the offline feature of Humble image view.
The two images visible in this example use the offline cache,
which ensures that these images will be always be available.

Simply add app:offlineCache="true" in your layout to made the image available offline:
```
<spilab.net.humbleimageview.HumbleImageView
        android:id="...
        ...
        app:offlineCache="true"/>
 ```
 Or dynamically set the offline cache feature from your code:
 ```
 findViewById<HumbleImageView>(R.id.offlineImageFromCode).setOfflineCache(true)
 ```