package spilab.net.humbleimageview.features.transform

class BitmapTransformationFactory {

    companion object {
        fun createInstance(className: String, values: String): BitmapTransformation {
            val transformation = Class.forName(className).newInstance() as BitmapTransformation
            transformation.setValues(values)
            return transformation
        }
    }
}