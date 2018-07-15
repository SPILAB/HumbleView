# HumbleView

## Features

The features listed below refers to the sample application.

#### Offline mode feature

The Activity OfflineActivity shows an example of the offline feature of Humble image view.
The two images visible in this example use the offline cache,
which ensures that these images will be always be available.

Simply add app:offlineCache="true" in your layout to made the image available offline:
```
<spilab.net.humbleimageview.HumbleImageView
        ...
        app:offlineCache="true"/>
 ```
 Or dynamically set the offline cache feature from your code:
 ```
 findViewById<HumbleImageView>(R.id.offlineImageFromCode).setOfflineCache(true)
 ```
 
 
#### Two scales types feature
  
The Activity ScaleTypeActivity shows the possibility to set two different scales types,
One for your placeholder:
```
app:srcCompat="@drawable/ic_..."
android:scaleType="center"
 ```
And another one for the loaded image:
```
app:url="https://..."
app:loadedImageScaleType="centerCrop"
```

By default the loaded image will use the same scale type than the place holder:

You can set the loaded image scale type in your layout XML:
```
<spilab.net.humbleimageview.HumbleImageView
        ...
        app:loadedImageScaleType="centerCrop"
```
Or in your code:
```
findViewById<HumbleImageView>(R.id.scaleLoadedImageFromCode).setLoadedImageScaleType(ScaleType.CENTER_CROP)
```