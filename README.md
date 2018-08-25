# HumbleView

## Features

The following features are demonstrated in the sample application.

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
 
 
#### Loaded scale types feature
  
The Activity ScaleTypeActivity shows the possibility to set different scales types,
on for the loaded image and one for app:srcCompat, used as placeholder:
```
<spilab.net.humbleimageview.HumbleImageView
     app:srcCompat="@drawable/ic_photo_black_48px"
     android:scaleType="center"
     app:url="https://c1.staticflickr.com/1/501/20080796120_fa1b37a709_h.jpg"
     app:loadedImageScaleType="centerCrop"
```
When the loading and transition of image set thought the url is complete,
android:scaleType will be replaced by app:loadedImageScaleType.
By default, the loaded image scale equals the image scale.
```
You can set the loaded image scale type in your layout XML:
<spilab.net.humbleimageview.HumbleImageView
     ...
     app:loadedImageScaleType="centerCrop"
```
Or in your code:
```
findViewById<HumbleImageView>(R.id.scaleLoadedImageFromCode).setLoadedImageScaleType(ScaleType.CENTER_CROP)
```