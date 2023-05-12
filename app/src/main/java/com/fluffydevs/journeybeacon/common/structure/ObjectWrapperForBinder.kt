package com.fluffydevs.journeybeacon.common.structure

import android.os.Binder

/**
 *
 *  Used to pass objects of any kind through the Bundle
 */
class ObjectWrapperForBinder(val data: Any) : Binder()